package com.nestlabs.sdk.sample;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Inverter_API_services {

    @Headers("token: 6eb6f069523055a339d71e5b1f6c88cc")
    @GET("device/inverter/last_new_data")
    Call<Inverter_data> getRealTimeInverterData(@Query("device_sn") String device_sn);

    @Headers("token: 6eb6f069523055a339d71e5b1f6c88cc")
    @GET("plant/energy")
    Call<History_data> getHistoryEnergy(@Query("plant_id") Integer plant_id, @Query("start_date") String start_date, @Query("end_date") String end_date, @Query("time_unit") String time_unit, @Query("page") Integer page, @Query("perpage") Integer perpage);

    @Headers("token: 6eb6f069523055a339d71e5b1f6c88cc")
    @GET("plant/power")
    Call<Daily_data> getDailyEnergy(@Query("plant_id") Integer plant_id, @Query("date") String date, @Query("timezone_id") Integer timezone_id);

//    @Headers("token: 6eb6f069523055a339d71e5b1f6c88cc")
//    @FormUrlEncoded
//    @POST("user/user_register")
//    Call<create_account_data> createUser(@Field("user_name") String user_name, @Field("user_password") String user_password, @Field("user_email") String user_email, @Field("user_tel") String user_tel, @Field("user_country") String user_country, @Field("user_type") Integer user_type);
//
//    @Headers("token: 6eb6f069523055a339d71e5b1f6c88cc")
//    @FormUrlEncoded
//    @POST("plant/add")
//    Call<create_station_data> createStation(@Field("c_user_id") Integer c_user_id, @Field("name") String name, @Field("peak_power") Integer peak_power, @Field("currency") String currency, @Field("longitude") String longitude, @Field("latitude") String latitude, @Field("timezone_id") Integer timezone_id);

}