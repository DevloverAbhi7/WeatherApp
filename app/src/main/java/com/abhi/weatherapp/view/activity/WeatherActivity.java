package com.abhi.weatherapp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.abhi.weatherapp.R;
import com.abhi.weatherapp.model.CitiesData;
import com.abhi.weatherapp.model.Example;
import com.abhi.weatherapp.network.NetworkClient;
import com.abhi.weatherapp.network.WeatherAPIs;
import com.abhi.weatherapp.utils.Constants;
import com.jakewharton.rxbinding2.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherActivity extends AppCompatActivity
{
    EditText inputcity;
    ListView citylist;
    TextView temperature;
    TextView cityname;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initializr();
    }

   public void initializr()
   {
       inputcity = (EditText) findViewById(R.id.input_city);
       cityname =(TextView) findViewById(R.id.city);
       citylist = findViewById(R.id.search_results);
       temperature =findViewById(R.id.temp);
       arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
       citylist.setAdapter(arrayAdapter);
       RxTextView.textChanges(inputcity)
               .doOnNext(text -> this.clearSearchResults())
               .filter(text -> text.length() >= 3)
               .debounce(500, TimeUnit.MILLISECONDS)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(this::updateSearchResults);
   }

    private void clearSearchResults() {
        arrayAdapter.clear();
        citylist.setVisibility(View.GONE);
    }

    private void updateSearchResults(CharSequence text) {
        ArrayList<String> list = new ArrayList<>();
       list = CitiesData.getcities();
       if(!(text.toString().equals("")))
        list.removeIf(s -> !s.contains(text.toString()));
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        citylist.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        citylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                fetchWeatherDetails(arrayAdapter.getItem(position).toString());
                cityname.setText(arrayAdapter.getItem(position).toString());
                inputcity.setText(arrayAdapter.getItem(position).toString());
                clearSearchResults();
            }
        });

    }

    private void fetchWeatherDetails(String cityname) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        WeatherAPIs weatherAPIs = retrofit.create(WeatherAPIs.class);
        Call<Example> call = weatherAPIs.getWeatherByCity(Constants.key, cityname);

        call.enqueue(cb);
    }
                Callback<Example> cb = new Callback()
                {
    @Override
    public void onResponse(Call call, Response  response)
    {
        /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
         */
        if (response.body() != null) {
            Example current = (Example) response.body();

            temperature.setText(Double.toString((current.getMain().getTemp()-273.15))+" C");
        }
    }
    @Override
    public void onFailure(Call call, Throwable t) {
        Log.w("MyTag", "requestFailed", t);
    }
};
}
