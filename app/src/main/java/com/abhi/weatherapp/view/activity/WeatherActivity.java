package com.abhi.weatherapp.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.abhi.weatherapp.R;
import com.abhi.weatherapp.app.App;
import com.abhi.weatherapp.app.ObjectBox;
import com.abhi.weatherapp.model.CitiesData;
import com.abhi.weatherapp.model.Example;
import com.abhi.weatherapp.model.db.WeatherHistoryDto;
import com.abhi.weatherapp.network.NetworkClient;
import com.abhi.weatherapp.network.WeatherAPIs;
import com.abhi.weatherapp.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;
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
    FirebaseFirestore db;
    ArrayAdapter<String> arrayAdapter;
    String temper;
    String citydata;
    /*Box<WeatherHistoryDto> boxy;*/
    WeatherHistoryDto weatherhistorydata;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
       // boxy= ObjectBox.get().boxFor(WeatherHistoryDto.class);
        initializr();
    }

   public void initializr()
   {   /* BoxStore boxStore = App.getApp().getBoxStore();*/
       /*Box<WeatherHistoryDto> animalBox = boxStore.boxFor(WeatherHistoryDto.class);*/
       /*animalBox.put(new WeatherHistoryDto("rer","hguy"));*/
       weatherhistorydata = new WeatherHistoryDto();
       db = FirebaseFirestore.getInstance();
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
            {   String city= arrayAdapter.getItem(position).toString();
                fetchWeatherDetails(city);
                cityname.setText(city);
                inputcity.setText(city);
                temperature.setText(temper);
                addtodb(city,temper,db);
                clearSearchResults();
            }
        });

    }

    private void fetchWeatherDetails(String cityname) {
            citydata = cityname;
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

           temper= Double.toString((current.getMain().getTemp()-273.15))+" C";
        }
    }
    @Override
    public void onFailure(Call call, Throwable t) {
        Log.w("MyTag", "requestFailed", t);
    }
};

   public void addtodb(String city,String temperature,FirebaseFirestore dbdata)
   {
       Map<String, Object> user = new HashMap<>();
       user.put("city", city);
       user.put("temperature", temperature);

       db.collection("users")
               .add(user)
               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       Log.d("addtodb", "DocumentSnapshot added with ID: " + documentReference.getId());
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.w("addtodb", "Error adding document", e);
                   }
               });
   }
}
