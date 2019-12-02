package kr.jwmsg.new_mpva.activities;

        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;


        import java.util.List;

        import kr.jwmsg.new_mpva.R;
        import kr.jwmsg.new_mpva.data_structure.HospitalData;

public class HospitalActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    HospitalData hospitalData;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_hospital );
        this.hospitalData = (HospitalData)getIntent().getBundleExtra( "bundle" ).getSerializable( "hospital" );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync(this);

        initInfo();

    }


    public void initInfo(){
        TextView HosName = findViewById( R.id.hospital_name );
        TextView HosPhone = findViewById( R.id.hospital_phone );
        TextView HosAddr = findViewById( R.id.hospital_addr );

        HosName.setText( hospitalData.name );
        HosPhone.setText( hospitalData.phone );
        HosAddr.setText( hospitalData.addr );


        Button toCall = findViewById( R.id.btn_to_call );
        Button toMap = findViewById( R.id.btn_to_map );



        toCall.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", hospitalData.phone, null));
                startActivity(intent);
            }
        } );

        toMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    getPackageManager().getPackageInfo("net.daum.android.map", 0);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("daummaps://search?q=맛집&p=37.537229,127.005515"));
                    startActivity(intent);
                }catch (PackageManager.NameNotFoundException e) {
                    String url = "https://play.google.com/store/apps/details?id=net.daum.android.map";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }
        } );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng address = getLocationFromAddress(this, hospitalData.addr);
        mMap.addMarker(new MarkerOptions().position(address).title(hospitalData.name));
        mMap.moveCamera( CameraUpdateFactory.zoomTo(16));
        mMap.moveCamera( CameraUpdateFactory.newLatLng(address));
    }



    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }

            Address location = address.get(0);
            Log.i("LONG LAT",location.getLatitude()+" , " +location.getLongitude());
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return p1;

    }
}