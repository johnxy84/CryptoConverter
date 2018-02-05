package com.crypto.cryptodata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatActivity extends AppCompatActivity {

    String Mode = "";

    private static Retrofit retrofit = null;
    RecyclerView currencyRecycler;
    CurrencyStatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Mode = getIntent().getStringExtra("Data");

        if (Mode != null) {
            Toast.makeText(this, Mode, Toast.LENGTH_SHORT).show();
            setTitle(Mode);
        }

        currencyRecycler = findViewById(R.id.CurrencyRecycler);
        currencyRecycler.setLayoutManager(new LinearLayoutManager(this));


        prepareData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.stat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
            {
                onBackPressed();
            }
            break;

            case R.id.HourChange: {
                if (adapter != null) {
                    adapter.setMode("1H");
                }
            }
            break;

            case R.id.DayChange: {
                if (adapter != null) {
                    adapter.setMode("1D");
                }
            }
            break;
            case R.id.WeekChange: {
                if (adapter != null) {
                    adapter.setMode("1W");
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareData() {
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

                List<Currency> currencies = response.body();

                if (currencies==null || currencies.size()<1) return;

                switch (Mode) {
                    case "Losers": {
                        Comparator<Currency> comp = (currency, t1) -> (int) (currency.getHourChange() - t1.getHourChange());

                        Collections.sort(currencies, comp);
                        Collections.reverse(currencies);
                        adapter = new CurrencyStatAdapter(currencies, false);

                    }
                    break;
                    case "Winners": {
                        Comparator<Currency> comp = (currency, t1) -> (int) (currency.getHourChange() - t1.getHourChange());
                        Collections.sort(currencies, comp);
                        adapter = new CurrencyStatAdapter(currencies, true);

                    }
                    break;
                }


                currencyRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {

                Log.e("Error", t.getMessage());
            }
        });

    }

    class CurrencyStatAdapter extends RecyclerView.Adapter<CurrencyStatViewHolder> {
        boolean isWinners;
        String Mode = "1H";

        private final List<Currency> currencies;

        public CurrencyStatAdapter(List<Currency> currencies, boolean isWinners) {

            this.currencies = currencies;
            this.isWinners = isWinners;
        }

        @Override
        public CurrencyStatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stat, parent, false);
            return new CurrencyStatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CurrencyStatViewHolder holder, int position) {

            Currency currency = currencies.get(position);
            holder.setCurrencyTextView(currency.Name);
            if (isWinners) {
                holder.setPercentageTextViewColor(R.color.green);
            } else {
                holder.setPercentageTextViewColor(R.color.red);
            }

            switch (Mode) {
                case "1H": {
                    holder.setPercentageTextView(String.valueOf(currency.HourChange));
                }
                break;
                case "1D": {
                    holder.setPercentageTextView(String.valueOf(currency.DayChange));
                }
                break;
                case "1W": {
                    holder.setPercentageTextView(String.valueOf(currency.WeekChange));
                }
                break;
            }
        }

        @Override
        public int getItemCount() {
            return currencies.size();
        }

        public void setMode(String Mode) {
            this.Mode = Mode;
            switch (Mode) {
                case "1H": {
                    Comparator<Currency> comp = (currency, t1) -> (int) (currency.getHourChange() - t1.getHourChange());

                    Collections.sort(currencies, comp);
                    if (!isWinners) Collections.reverse(currencies);
                }
                break;
                case "1D": {
                    Comparator<Currency> comp = (currency, t1) -> (int) (currency.getDayChange() - t1.getDayChange());
                    Collections.sort(currencies, comp);
                    if (!isWinners) Collections.reverse(currencies);
                }
                break;
                case "1W": {
                    Comparator<Currency> comp = (currency, t1) -> (int) (currency.getWeekChange() - t1.getWeekChange());

                    Collections.sort(currencies, comp);
                    if (!isWinners) Collections.reverse(currencies);
                }
                break;
            }

            notifyDataSetChanged();

        }
    }


    class CurrencyStatViewHolder extends RecyclerView.ViewHolder {

        TextView CurrencyTextView;

        TextView PercentageTextView;

        public CurrencyStatViewHolder(View itemView) {
            super(itemView);
            CurrencyTextView = itemView.findViewById(R.id.CurrencyTextView);
            PercentageTextView = itemView.findViewById(R.id.PercentageTextView);
        }

        public void setCurrencyTextView(String currencyTextView) {
            this.CurrencyTextView.setText(currencyTextView);
        }


        public void setPercentageTextView(String percentageTextView) {
            this.PercentageTextView.setText(percentageTextView);
        }

        public TextView getPercentageTextView() {
            return PercentageTextView;
        }

        public void setPercentageTextViewColor(int colorId) {
            this.PercentageTextView.setTextColor(colorId);
        }
    }
}






















