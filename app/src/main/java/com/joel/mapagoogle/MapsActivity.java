package com.joel.mapagoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner spinner;
    private ToggleButton btnZoom;
    private Button btnPos;
    private RadioButton rdRetro, rdNight;
    private RadioGroup rgStyle;
    public final int idRetro = 2131231020, idNight=2131231019;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner)findViewById(R.id.spinner);
        btnZoom = (ToggleButton)findViewById(R.id.zoom);
        btnPos = (Button)findViewById(R.id.pos);
        rdNight = (RadioButton)findViewById(R.id.rdNight);
        rdRetro = (RadioButton)findViewById(R.id.rdRetro);
        rgStyle = (RadioGroup) findViewById(R.id.rgStyle);


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_new_look,tipoVista);
        spinner.setAdapter(adapter);


            /**Seleccionar vista de mapa*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sightMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnZoom.setText("Desactivado");

        /**Activar opciones de zoom*/
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnZoom.isChecked()){
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    btnZoom.setText("Activado");
                }else{
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    btnZoom.setText("Desactivado");
                }

            }
        });

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition position = mMap.getCameraPosition();

                LatLng coordenas = position.target;
                double latitude = coordenas.latitude;
                double longitude = coordenas.longitude;

                Toast.makeText(MapsActivity.this, "Lat: " + latitude + "\n Long: " + longitude, Toast.LENGTH_LONG).show();
            }
        });

        rgStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                stylingWizard();
            }
        });


    }


    String[] tipoVista ={"Normal","Satellite","Hybrid","Terrain"};

    public void sightMap(){
        int sightType = spinner.getSelectedItemPosition();

        switch (sightType){
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }

    public void stylingWizard(){
        int id = rgStyle.getCheckedRadioButtonId();
        switch (id){
            case idRetro:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.formatoretro));
                break;
            case idNight:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.formatonight));
                break;
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Ecuador = new LatLng( -0.225219, -78.52480);
        mMap.addMarker(new MarkerOptions().position(Ecuador).title("This is Ecuador"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ecuador));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Projection projection =  mMap.getProjection();
                Point resultadoCoord = projection.toScreenLocation(latLng);

                Toast.makeText(MapsActivity.this,
                "Click\n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lng: " + latLng.longitude + "\n" +
                        "X: " + resultadoCoord.x + "    - Y: " + resultadoCoord.y,
                        Toast.LENGTH_LONG).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Marcador pulsado:\n"+
                                "\n Título: " +marker.getTitle()+
                                "\n Ubicación: "+ marker.getPosition(),
                                Toast.LENGTH_LONG).show();
                return false;
            }
        });


    }



    /**vistas menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vistas,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.vBogota:
                CameraUpdate ubicacionBogota= CameraUpdateFactory.newLatLngZoom(new LatLng(4.60971,-74.08175),8);
                mMap.moveCamera(ubicacionBogota);

                //poligono
                PolygonOptions cuadrado = new PolygonOptions()
                            .add(new LatLng(4.81,-74.27),
                                new LatLng(4.81,-73.95),
                                new LatLng(4.45,-73.95),
                                    new LatLng(4.45,-74.27));
                cuadrado.strokeWidth(10);
                cuadrado.strokeColor(Color.rgb(57,138,181));
                mMap.addPolygon(cuadrado);

                break;
            case R.id.vPiramides:
                LatLng ubicacionEgipto = new LatLng(29.980161122887264, 31.133858191417676);

                CameraPosition position = new CameraPosition.Builder()
                        .target(ubicacionEgipto)
                        .zoom(15)
                        .bearing(65)
                        .tilt(50)
                        .build();
                CameraUpdate cameraUpdate3D = CameraUpdateFactory.newCameraPosition(position);
                mMap.animateCamera(cameraUpdate3D);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mParis:
                LatLng ubicacionParis = new LatLng(48.8032, 2.3511);
                mMap.addMarker(new MarkerOptions().position(ubicacionParis).title("País: Francia").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionParis,5));

                break;

        }
        return super.onOptionsItemSelected(item);
    }


}