package kr.jwmsg.new_mpva.data_controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;

import kr.jwmsg.new_mpva.R;


public class HospitalListAdt extends BaseAdapter {

    JsonArray hospitals = null;
    Context context = null;

    public HospitalListAdt(Context context, JsonArray hospitals){
        this.hospitals = hospitals;
        this.context = context;
    }


    @Override
    public int getCount() {

        return hospitals.size();
    }

    @Override
    public Object getItem(int i) {
        return hospitals.get( i );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         if(view == null){
             LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
             view = inflater.inflate( R.layout.main_hospital_list_cell,viewGroup,false );
         }

         TextView hospital_name = view.findViewById( R.id.hospital_name );
         hospital_name.setText( hospitals.get( i ).getAsJsonObject().getAsJsonPrimitive( "Name" ).getAsString()  );

         TextView hospital_address = view.findViewById( R.id.hospital_address );
         hospital_address.setText( hospitals.get( i ).getAsJsonObject().getAsJsonPrimitive( "AddrDetail" ).getAsString()  );

        return view;
    }
}
