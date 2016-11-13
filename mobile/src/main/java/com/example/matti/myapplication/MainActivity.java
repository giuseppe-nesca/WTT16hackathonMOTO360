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

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private LocationListener locationListener = null;

    public JsonManager containerDati;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerDati = new JsonManager(getApplicationContext());
        if (containerDati.isUserRegistered()) {
            setContentView(R.layout.activity_main2);
            TextView nomeLabel = (TextView) findViewById(R.id.nome2);
            nomeLabel.setText(containerDati.readField("name"));
        } else {
            setContentView(R.layout.activity_main);
        }
        EventBus.getDefault().register(this);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }


    //Restituisce un vettore di due valori double il primo e latitudine e il secondo longitudine
    public double[] coordinateGPS() {

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
                    loc[0] = round(location.getLatitude() * 10000) / 10000.0;
                    loc[1] = round(location.getLongitude() * 10000) / 10000.0;
                }
            }
        }
        //Toast.makeText(this, "Help me at (" + String.valueOf(loc[0]) + ", " + String.valueOf(loc[1]) + ")", Toast.LENGTH_SHORT).show();

        return loc;
    }

    //onClick listener del pulsante Salva per salvare dati relativi all'utente
    public void onClickSalva(View view) {
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

    public void onClickChangeActivity(View view) {
        setContentView(R.layout.activity_main);

    }


    public void sendToServer(String string) {
        AsyncHttpClient client = new AsyncHttpClient();
        double[] coord = coordinateGPS();
        string="Tipo di segnalazione: "+string;

        try {
            StringEntity entity = new StringEntity(containerDati.getJSONStringToSend(coord[0],coord[1],string,"+393924953670"));
            client.post(getApplicationContext(), "http://192.168.43.158/wtt/server.php", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int a, Header[] b, final byte[] d) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onFailure(int a, Header[] b, byte[] d, Throwable e) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }

    }





    public void onClickTestServer(View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        double[] coord = coordinateGPS();

        try {
            StringEntity entity = new StringEntity(containerDati.getJSONStringToSend(coord[0],coord[1],"+393924953670"));
            client.post(getApplicationContext(), "http://192.168.43.158/wtt/server.php", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int a, Header[] b, final byte[] d) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onFailure(int a, Header[] b, byte[] d, Throwable e) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }

    }

    public void onEvent(String message){
        sendToServer(message);
    }
}


