package com.crypto.cryptodata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static Retrofit retrofit = null;
    RecyclerView currencyRecycler;
    CurrencyAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyRecycler= findViewById(R.id.CurrencyRecyclerView);
        currencyRecycler.setLayoutManager(new LinearLayoutManager(this));

        setupNetwork();
    }

    public void setupNetwork() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.currency_endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        CryptoData cryptoData = retrofit.create(CryptoData.class);

        Call<List<Currency>> call = cryptoData.getCurrencies();

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {

                List<Currency> currencies= response.body();

                adapter= new CurrencyAdapter(currencies);


                currencyRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {

                Log.e("Error", t.getMessage());
            }
        });


    }

    public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder>
    {

        private final List<Currency> currencies;

        public CurrencyAdapter(List<Currency> currencies)
        {
            this.currencies= currencies;
        }
        @Override
        public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_btc, parent, false);
            return new CurrencyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CurrencyViewHolder holder, int position) {

            float nairaValue= Float.parseFloat(currencies.get(position).Price) * 359.71f;

//            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.CANADA.ENGLISH));//.format(String.valueOf(naira));
//
//            String naira=df.format(nairaValue);
//            String dollar= df.format(currencies.get(position).Price);

            holder.setCurrencyName(currencies.get(position).Name);
            holder.setNgnPrice(String.valueOf(nairaValue));
            holder.setUsdPrice(currencies.get(position).Price);
        }

        @Override
        public int getItemCount() {
            return currencies.size();
        }
    }



    class CurrencyViewHolder extends RecyclerView.ViewHolder
    {

        TextView currencyName;
        TextView usdPrice;
        TextView ngnPrice;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            currencyName= itemView.findViewById(R.id.BTCTextVIew);
            usdPrice= itemView.findViewById(R.id.USDTextView);
            ngnPrice= itemView.findViewById(R.id.NGNTextView);
        }

        public void setCurrencyName (String currencyName)
        {
            this.currencyName.setText(currencyName);
        }

        public void setUsdPrice (String usdPrice)
        {
            this.usdPrice.setText(usdPrice);
        }

        public void setNgnPrice (String ngnPrice)
        {
            this.ngnPrice.setText(ngnPrice);
        }


    }
}
