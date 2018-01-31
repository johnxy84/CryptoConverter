package com.crypto.cryptodata;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by john.okoroafor on 31/01/2018.
 */

public interface CryptoData {



        @GET("/v1/ticker/")
        Call<List<Currency>> getCurrencies();

}
