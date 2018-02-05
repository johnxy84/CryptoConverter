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

    @SerializedName("percent_change_1h")
    String HourChange;

    @SerializedName("percent_change_24h")
    String DayChange;

    @SerializedName("percent_change_7d")
    String WeekChange;



//    public String getName ()
//    {
//        return Name;
//    }

//    public void setName (String name)
//    {
//        Name=name;
//    }
//

    public double getHourChange()
    {
        return Double.parseDouble(HourChange);
    }

    public double getDayChange()
    {
        return Double.parseDouble(HourChange);
    }

    public double getWeekChange()
    {
        return Double.parseDouble(HourChange);
    }


}
