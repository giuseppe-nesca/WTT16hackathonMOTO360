package com.example.matti.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    private LocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonManager containerDati = new JsonManager(getApplicationContext());
        if(containerDati.isUserRegistered()){
            setContentView(R.layout.activity_main2);
            TextView nomeLabel = (TextView) findViewById(R.id.nome2);
            nomeLabel.setText(containerDati.readField("name"));
        }else{
            setContentView(R.layout.activity_main);
        }

    }

    //Restituisce un vettore di due valori double il primo e latitudine e il secondo longitudine
    public double[] coordinateGPS(View view) {

        //location
        double loc[] = new double[2];
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Richiesta permessi", Toast.LENGTH_SHORT).show();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Location location;

        if (isGPSEnabled) {
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    loc[0] = round(location.getLatitude()*10000)/10000.0;
                    loc[1] = round(location.getLongitude()*10000)/10000.0;
                }
            }
        }
        //Toast.makeText(this, "Help me at (" + String.valueOf(loc[0]) + ", " + String.valueOf(loc[1]) + ")", Toast.LENGTH_SHORT).show();

        return loc;
    }

    //onClick listener del pulsante Salva per salvare dati relativi all'utente
    public void onClickSalva(View view){
        EditText nome = (EditText) findViewById(R.id.nome);
        EditText cognome = (EditText) findViewById(R.id.cognome);
        EditText sangue = (EditText) findViewById(R.id.sangue);
        EditText eta = (EditText) findViewById(R.id.eta);
        EditText peso = (EditText) findViewById(R.id.peso);
        EditText sesso = (EditText) findViewById(R.id.sesso);

        JsonManager containerDati = new JsonManager(getApplicationContext());
        containerDati.registerUser(nome.getText().toString(), cognome.getText().toString(), sangue.getText().toString(), Integer.parseInt(eta.getText().toString()), sesso.getText().toString(), Integer.parseInt(peso.getText().toString()));

        setContentView(R.layout.activity_main2);
        TextView nomeLabel = (TextView) findViewById(R.id.nome2);
        nomeLabel.setText(containerDati.readField("name"));
    }

    public void onClickChangeActivity(View view)
    {
        setContentView(R.layout.activity_main);

    }
}
