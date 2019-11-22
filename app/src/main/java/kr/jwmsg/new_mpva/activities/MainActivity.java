package kr.jwmsg.new_mpva.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.jwmsg.new_mpva.R;
import kr.jwmsg.new_mpva.api_request.ApiRequsts;
import kr.jwmsg.new_mpva.data_controller.HospitalListAdt;
import kr.jwmsg.new_mpva.data_structure.HospitalData;
import kr.jwmsg.new_mpva.data_structure.LocationData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTextAlignment( Toolbar.TEXT_ALIGNMENT_CENTER );
        setSupportActionBar( toolbar );


        search_location( getaddr().province );
        changeEditorListener();
        clickListListener();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent( this,SettingActivity.class );
            startActivity( intent );
            return true;
        }

        return super.onOptionsItemSelected( item );
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        search_location( getaddr().province );
        changeEditorListener();
        clickListListener();
    }

    public void search_location(String addr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://mpva.jwmsg.me" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        ApiRequsts api = retrofit.create( ApiRequsts.class );
        Call<JsonElement> result = api.searchByCity( addr );
        result.enqueue( new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement rtn = response.body();
                if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "success" )) {
                    ListView hospital_list = findViewById( R.id.hospital_list );
                    HospitalListAdt hospital_list_adt = new HospitalListAdt( getApplicationContext(), rtn.getAsJsonObject().getAsJsonArray( "message" ) );
                    hospital_list.setAdapter( hospital_list_adt );
                    clickListListener();
                } else if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "error" )) {
                    Toast.makeText( getApplicationContext(), "Server Error\n" + rtn.getAsJsonObject(), Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText( getApplicationContext(), "Client Error\n" + rtn.getAsJsonObject(), Toast.LENGTH_LONG ).show();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText( getApplicationContext(), "Fail connection\n" + t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    public void search_name(String name){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://mpva.jwmsg.me" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        ApiRequsts api = retrofit.create( ApiRequsts.class );
        Call<JsonElement> result = api.searchByName( name );
        result.enqueue( new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement rtn = response.body();
                if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "success" )) {
                    ListView hospital_list = findViewById( R.id.hospital_list );
                    HospitalListAdt hospital_list_adt = new HospitalListAdt( getApplicationContext(), rtn.getAsJsonObject().getAsJsonArray( "message" ) );
                    hospital_list.setAdapter( hospital_list_adt );
                    clickListListener();
                } else if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "error" )) {
                    Toast.makeText( getApplicationContext(), "Server Error\n" + rtn.getAsJsonObject(), Toast.LENGTH_LONG ).show();
                }else{
                    search_location( getaddr().province );
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText( getApplicationContext(), "Fail connection\n" + t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }





    public String getSettingLocation(){
        SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
        return sp_settings.getString( "locations","서울특별시" );
    }

    public LocationData getaddr() {
        LocationData locationData = new LocationData();
        locationData.province = getSettingLocation();
        locationData.addr = getSettingLocation();

        SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
        if( !sp_settings.getBoolean( "GPS_Switch",true )){
            return locationData;
        }


        if (checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 1001);
            return locationData;
        }


        LocationManager lm = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location!=null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            locationData.longitude = longitude;
            locationData.latitude = latitude;
            locationData.initial = true;
        }else{
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            locationData.longitude = longitude;
            locationData.latitude = latitude;
            locationData.initial = true;
        }




        if(locationData.initial){
            Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addr = null;
            try{

                addr = gCoder.getFromLocation(locationData.latitude,locationData.longitude,1);

            } catch (IOException e){
                e.printStackTrace();
            }

            if (addr != null && addr.size()!=0) {
                locationData.addr = addr.get(0).getAddressLine(0 );
                locationData.province = locationData.addr.split( " " )[0];
            }
        }

        return locationData;
    }





    public void changeEditorListener() {
        final EditText editText = findViewById( R.id.searchText );
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_name( editText.getText().toString() );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
    }

    public void clickListListener(){
        ListView hospital_list = findViewById( R.id.hospital_list );
        hospital_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JsonElement job = (JsonObject)adapterView.getItemAtPosition( i );
                HospitalData hospitalData = new HospitalData( job );
                Intent intent = new Intent( getApplicationContext(), HospitalActivity.class );
                Bundle bundle = new Bundle(  );
                bundle.putSerializable( "hospital", hospitalData );
                intent.putExtra( "bundle",bundle );
                startActivity( intent );

            }
        } );
    }


}


