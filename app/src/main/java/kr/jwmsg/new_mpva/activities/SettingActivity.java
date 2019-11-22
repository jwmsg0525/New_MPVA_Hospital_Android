package kr.jwmsg.new_mpva.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.jwmsg.new_mpva.R;
import kr.jwmsg.new_mpva.api_request.ApiRequsts;
import kr.jwmsg.new_mpva.data_controller.ProvinceListAdt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        getProvinceList();
        setFirstGPS_SwitchSelection();
    }


    public void getProvinceList(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://mpva.jwmsg.me" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        ApiRequsts api = retrofit.create( ApiRequsts.class );
        Call<JsonElement> result = api.getAddrList();

        result.enqueue( new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement rtn = response.body();
                if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "success" )) {
                    Spinner province_list = findViewById( R.id.location_spinner );
                    ProvinceListAdt province_list_adt = new ProvinceListAdt( getApplicationContext(), rtn.getAsJsonObject().getAsJsonArray( "message" ) );
                    province_list.setAdapter( province_list_adt );
                    clickListListener();
                } else if (rtn.getAsJsonObject().getAsJsonPrimitive( "status" ).getAsString().equals( "error" )) {
                    Toast.makeText( getApplicationContext(), "Server Error\n" + rtn.getAsJsonObject(), Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText( getApplicationContext(), "Server Error\n" + rtn.getAsJsonObject(), Toast.LENGTH_LONG ).show();
                }
                setFirstProvinceSelection();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText( getApplicationContext(), "Fail connection\n" + t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    public void setFirstProvinceSelection(){
        //BUG FIX!
        Spinner province_list = findViewById( R.id.location_spinner );
        SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
        String data = sp_settings.getString( "locations","서울특별시" );
        Log.i("ddd",province_list.getCount()+"");
        for(int i = 0 ; i < province_list.getCount(); i++){
            JsonElement job = (JsonElement) province_list.getItemAtPosition( i );
            if(job.getAsJsonObject().getAsJsonPrimitive( "province" ).getAsString().equals( data )){
                province_list.setSelection( i );
            }

        }

    }

    public void setFirstGPS_SwitchSelection(){
        Switch GPS_Switch = findViewById(R.id.GPS_Switch);
        SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
        GPS_Switch.setChecked( sp_settings.getBoolean( "GPS_Switch",true ) );
    }



    public void clickListListener(){
        Spinner province_list = findViewById( R.id.location_spinner );
        province_list.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JsonElement job = (JsonObject)adapterView.getItemAtPosition( i );
                SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor speditor = sp_settings.edit();
                speditor.putString( "locations", job.getAsJsonObject().getAsJsonPrimitive( "province" ).getAsString());
                speditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                JsonElement job = (JsonObject)adapterView.getItemAtPosition( 0 );
                SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor speditor = sp_settings.edit();
                speditor.putString( "locations", job.getAsJsonObject().getAsJsonPrimitive( "province" ).getAsString());
                speditor.commit();
            }
        } );

        Switch GPS_Switch = findViewById(R.id.GPS_Switch);
        GPS_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
                    SharedPreferences.Editor speditor = sp_settings.edit();
                    speditor.putBoolean( "GPS_Switch",true );
                    speditor.commit();
                } else {
                    SharedPreferences sp_settings = getSharedPreferences("settings", MODE_PRIVATE);
                    SharedPreferences.Editor speditor = sp_settings.edit();
                    speditor.putBoolean( "GPS_Switch",false );
                    speditor.commit();
                }
            }
        });


    }

}
