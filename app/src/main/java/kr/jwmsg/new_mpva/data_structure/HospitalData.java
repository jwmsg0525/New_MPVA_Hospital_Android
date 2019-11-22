package kr.jwmsg.new_mpva.data_structure;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HospitalData implements Serializable, Parcelable {

    JsonElement hospitalJson = null;

    public String name = null;
    public String addr = null;
    public String phone = null;
    public int wardCnt = 0;
    public int bedCnt = 0;

    public HospitalData( JsonElement hospitalJson){
        this.hospitalJson = hospitalJson;
        this.name = hospitalJson.getAsJsonObject().getAsJsonPrimitive( "Name" ).getAsString();
        this.addr = hospitalJson.getAsJsonObject().getAsJsonPrimitive( "AddrDetail" ).getAsString();
        this.phone = hospitalJson.getAsJsonObject().getAsJsonPrimitive( "Tel" ).getAsString();
        this.wardCnt = hospitalJson.getAsJsonObject().getAsJsonPrimitive( "WardCnt" ).getAsInt();
        this.bedCnt = hospitalJson.getAsJsonObject().getAsJsonPrimitive( "BedCnt" ).getAsInt();
    }

    protected HospitalData(Parcel in) {
        name = in.readString();
        addr = in.readString();
        phone = in.readString();
        wardCnt = in.readInt();
        bedCnt = in.readInt();
    }

    public static final Creator<HospitalData> CREATOR = new Creator<HospitalData>() {
        @Override
        public HospitalData createFromParcel(Parcel in) {
            return new HospitalData( in );
        }

        @Override
        public HospitalData[] newArray(int size) {
            return new HospitalData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( name );
        parcel.writeString( addr );
        parcel.writeString( phone );
        parcel.writeInt( wardCnt );
        parcel.writeInt( bedCnt );
    }
}
