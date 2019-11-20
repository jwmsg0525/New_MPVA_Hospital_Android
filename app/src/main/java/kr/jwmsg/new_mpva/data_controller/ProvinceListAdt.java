package kr.jwmsg.new_mpva.data_controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;

import kr.jwmsg.new_mpva.R;

public class ProvinceListAdt extends BaseAdapter {

    JsonArray provinces = null;
    Context context = null;
    LayoutInflater inflater;

    public ProvinceListAdt(Context context, JsonArray provinces){
        this.provinces = provinces;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return provinces.size();
    }

    @Override
    public Object getItem(int i) {
        return provinces.get( i );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = inflater.inflate( R.layout.main_province_list_cell, viewGroup, false);
        }


        if(provinces!=null){
            //데이터세팅
            String text = provinces.get(i).getAsJsonObject().getAsJsonPrimitive( "province" ).getAsString();
            ((TextView)view.findViewById(R.id.cell_txt_province)).setText(text);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getView( position, convertView, parent );
    }
}
