package com.example.stationeryproject.rest_discard;

import com.example.stationeryproject.model.History;
import com.example.stationeryproject.model.Inventory;
import com.example.stationeryproject.model.Price;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//import the retrofit connection into the service
//notice that the commands here match what is on the ASP web service API GET/POST/PUT commands
//Here you append the details and instruction on what parameters associated when you call the restful URL and it links to backend server

public interface StationeryService {
    /*
     * Inventories
     *    GET api/Inventories
     *    GET api/Inventories/{id}
     *    PUT api/Inventories/{id}
     *    POST api/Inventories
     *    DELETE api/Inventories/{id}
     */
    @GET("Inventories")
    Call<List<Inventory>> getAllInventories();

    @GET("Inventories/{Id}")
    Call<Inventory> getInventory(@Path("Id") Long Id);

    @FormUrlEncoded
    @PUT("Inventories/{Id}")
    Call<Void> updateInventory(
            @Path("Id") Long index,
            @Field("Id") Long Id,
            @Field("Description") String Description,
            @Field("Category") String Category,
            @Field("Quantity") Long Quantity,
            @Field("TransactionId") Long TransactionId
            );

    @FormUrlEncoded
    @POST("Inventories")
    Call<Inventory> createInventory(
            @Field("Id") Long Id,
            @Field("Category") String Category,
            @Field("Description") String Description,
            @Field("Quantity") Long Quantity,
            @Field("TransactionId") Long TransactionId
    );

    @DELETE("Inventories/{Id}")
    Call<Inventory> deleteInventory(@Path("Id") Long Id);


    /*
     * Histories
     *    GET api/Histories
     *    GET api/Histories/{id}
     *    PUT api/Histories/{id}
     *    POST api/Histories
     *    DELETE api/Histories/{id}
     */
    @GET("Histories")
    Call<List<History>> getAllHistories();

    @GET("Histories/{Id}")
    Call<History> getHistory(@Path("Id") Long Id);

    @FormUrlEncoded
    @PUT("Histories/{Id}")
    Call<Void> updateHistory(
            @Path("Id") Long index,
            @Field("Id") Long Id,
            @Field("Timestamp") String Timestamp
    );

    @FormUrlEncoded
    @POST("Histories")
    Call<History> createHistory(
            @Field("Id") Long Id,
            @Field("Timestamp") String Timestamp
    );

    @DELETE("Histories/{Id}")
    Call<History> deleteHistory(@Path("Id") Long Id);

    /*
     * Prices
     *    GET api/Prices
     *    GET api/Prices/{id}
     *    PUT api/Prices/{id}
     *    POST api/Prices
     *    DELETE api/Prices/{id}
     */
    @GET("Prices")
    Call<List<Price>> getAllPrices();

    @GET("Prices/{Id}")
    Call<Price> getPrice(@Path("Id") Long Id);

    @FormUrlEncoded
    @PUT("Prices/{Id}")
    Call<Void> updatePrice(
            @Path("Id") Long index,
            @Field("Id") Long Id,
            @Field("Code") String Code,
            @Field("Category") String Category,
            @Field("Description") String Description,
            @Field("Price1") Double Price
    );

    @FormUrlEncoded
    @POST("Prices")
    Call<Price> createPrice(
            @Field("Id") Long Id,
            @Field("Code") String Code,
            @Field("Category") String Category,
            @Field("Description") String Description,
            @Field("Price1") Double Price
    );

    @DELETE("Prices/{Id}")
    Call<Price> deletePrice(@Path("Id") Long Id);
}
