package com.crypto.cryptodata;


import com.google.gson.annotations.SerializedName;

/**
 * Created by john.okoroafor on 31/01/2018.
 */

public class Currency {


    @SerializedName("name")
    String Name;

    @SerializedName("price_usd")
    String Price;

    public String getName ()
    {
        return Name;
    }

    public void setName (String name)
    {
        Name=name;
    }

    public String getPrice ()
    {
        return Price;
    }

    public void setPrice (String name)
    {
        Name=name;
    }


}
