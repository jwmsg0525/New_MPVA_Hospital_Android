package kr.jwmsg.new_mpva.api_request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiRequsts {

    @GET("/api/search/name/{name}")
    Call<JsonElement> searchByName(@Path("name") String user);
    
    @GET("/api/search/addr/{addr1}")
    Call<JsonElement> searchByCity(@Path("addr1") String addr1);


    @GET("/api/search/addr/{addr1}/{addr2}")
    Call<JsonElement> searchByCity(@Path("addr1") String addr1,@Path("addr2") String addr2);
}
