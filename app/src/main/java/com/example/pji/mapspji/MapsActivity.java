package com.example.pji.mapspji;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pji.mapspji.ConnectActivity.LoginActivity;
import com.example.pji.mapspji.GroupActivity.CreateGroupActivity;
import com.example.pji.mapspji.database.exception.UserHaveNotGroupException;
import com.example.pji.mapspji.database.groupe.GroupUser;
import com.example.pji.mapspji.database.localisation.Localisation;
import com.example.pji.mapspji.database.localisation.Position;
import com.example.pji.mapspji.optionActivity.AdminActivity;
import com.example.pji.mapspji.optionActivity.OptionActivity;
import com.example.pji.mapspji.optionActivity.SubscriptionGroupActivity;
import com.example.pji.mapspji.optionActivity.UnsubscribeGroupActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {

    //Demande de Localisation
    LocationRequest mLocationRequest;

    GoogleApiClient apiGoogle;
    GoogleMap googleMap;
    SupportMapFragment mapFragment;
    Marker marqueur;
    String username;
    private Long rafraichissement;
    HashMap<String,Marker> usersMarker;
    int  id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------
        Intent user=getIntent();
        //8 secondes par default
        rafraichissement=user.getLongExtra("raf",8000);
        username=user.getStringExtra("user");
        id=user.getIntExtra("id", 0);
        Log.i("rafraichissement",""+rafraichissement);
        usersMarker=new HashMap<String, Marker>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       // startActivity(new SendMail("jihad3394@hotmail.fr","Maps").getIntent());
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();

        //R.menu.menu est l'id de notre menu

        inflater.inflate(R.menu.menumap, menu);
        return true;
    }
    @Override

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.creer: {
                Intent intent=new Intent(this, CreateGroupActivity.class);
                intent.putExtra("user",username);
                intent.putExtra("raf",this.rafraichissement);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
            case R.id.abonner: {
                Intent intent=new Intent(this, SubscriptionGroupActivity.class);
                intent.putExtra("user",username);
                intent.putExtra("raf",this.rafraichissement);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
            case R.id.desabonner:{
                Intent intent=new Intent(this, UnsubscribeGroupActivity.class);
                intent.putExtra("user",username);
                intent.putExtra("raf",this.rafraichissement);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
            case R.id.admin:{
                Intent intent=new Intent(this, AdminActivity.class);
                intent.putExtra("user",username);
                intent.putExtra("raf",this.rafraichissement);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
            case R.id.option:{
                Intent intent=new Intent(this, OptionActivity.class);
                intent.putExtra("raf",this.rafraichissement);
                intent.putExtra("user",username);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
            case R.id.deconnecter:{
               startActivity(new Intent(MapsActivity.this,LoginActivity.class));
                return true;
            }



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Rajoute un bouton pour ma localisation dans la map
        this.googleMap.setMyLocationEnabled(true);
        //Preparer la  connection à l'api Google
        buildGoogleApiClient();
        //Connection
        apiGoogle.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // Connection pour Utilisation des services Google Api
    protected synchronized void buildGoogleApiClient() {
        apiGoogle = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)//Services de localisation demander
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Recuperer la dernier position
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                apiGoogle);
        //Si elle est different de null alors enlever cette position car elle sera modifier
        if (mLastLocation != null) {
            //Nettoyer la Map
            googleMap.clear();
            // Recuperer la position actuel
            LatLng latLong= new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //Preparer le marqueur de position
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLong);
            markerOptions.title("My Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            marqueur = googleMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        //Une demande de localisation toute les n secondes
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(rafraichissement);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //Demande de mise a jour de la position actuelle->va rentrer dans OnLocation Changed
        LocationServices.FusedLocationApi.requestLocationUpdates(apiGoogle, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection Suspendue",Toast.LENGTH_SHORT).show();
    }
    //On bloque le bouton de Retour
    public void onBackPressed() {
        // do nothing.
    }
    public void putUserGroupMarker()  {
        GroupUser users= null;
        try {
            users = new GroupUser();
            HashMap<String,Position>usersmap=users.getAllPosition(id);
            Iterator<String> iterator=usersmap.keySet().iterator();
            while(iterator.hasNext()){
                String user=iterator.next();
                if (usersMarker.containsKey(user)) {
                    Position position = usersmap.get(user);
                    Log.i("Position",""+position);
                    LatLng latLong = new LatLng(position.getLatitude(), position.getLongitude());
                    //Preparer le marqueur de position
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLong);
                    markerOptions.title(user);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker marker=usersMarker.get(user);
                    marker.remove();
                    usersMarker.remove(user);
                    marker=googleMap.addMarker(markerOptions);
                    usersMarker.put(user,marker);

                }
                else{
                    Position position = usersmap.get(user);
                    LatLng latLong = new LatLng(position.getLatitude(), position.getLongitude());
                    //Preparer le marqueur de position
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLong);
                    markerOptions.title(user);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker marker=googleMap.addMarker(markerOptions);
                    usersMarker.put(user,marker);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Probleme de Connection",Toast.LENGTH_SHORT).show();
    }

    @Override
    //Quand la Position change
    public void onLocationChanged(Location location) {

        //On supprime l'ancien marqueur
        if (marqueur != null) {
            marqueur.remove();
        }
        //On recupere la Longitude et Latitude actuelle
        LatLng latLong=new LatLng(location.getLatitude(), location.getLongitude());
        try {
            addPositionInServeur(latLong.longitude,latLong.latitude);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        putUserGroupMarker();
        //Preparation du Marqueur
        MarkerOptions markerOptions = new MarkerOptions();
        //Placer à la position concerner
        markerOptions.position(latLong);
        //Indiquer un titre au Marqueur
        markerOptions.title("Current Position");
        //Icon au Marqueur
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        //Ajout du Marqueur
        marqueur = googleMap.addMarker(markerOptions);


    }

    public void addPositionInServeur(final Double longitude, final Double latitude) throws SQLException, ClassNotFoundException, IOException {
            Log.i("Position",longitude+" "+latitude);
            new Localisation(id).setLocalisation(new Position(longitude, latitude));
    }

}