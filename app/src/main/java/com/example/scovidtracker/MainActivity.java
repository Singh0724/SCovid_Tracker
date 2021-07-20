package com.example.scovidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scovidtracker.api.ApiUtilities;
import com.example.scovidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // DECLARING ALL VARIABLES

    private TextView TotalConform,TotalActive,TotalRecover,TotalDeath,TotalTest;
    private TextView TodayConform,TodayRecover,TodayDeath,date;
    private PieChart pieChart;
    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list= new ArrayList<>();

        init();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i=0;i<list.size();i++){
                            if (list.get(i).getCountry().equals("India")){
                                int conform = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recover = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                // FOR TOTAL

                                TotalActive.setText(NumberFormat.getInstance().format(active));
                                TotalConform.setText(NumberFormat.getInstance().format(conform));
                                TotalDeath.setText(NumberFormat.getInstance().format(death));
                                TotalRecover.setText(NumberFormat.getInstance().format(recover));

                             // FOR TODAY

                                TodayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                TodayConform.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                TodayRecover.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                TotalTest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));


                              setText(list.get(i).getUpdated());

                              //FOR PIECHART

                                pieChart.addPieSlice(new PieModel("Conform",conform,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue)));

                                pieChart.addPieSlice(new PieModel("Recover",recover,getResources().getColor(R.color.green)));
                                pieChart.addPieSlice(new PieModel("Death",death,getResources().getColor(R.color.red)));

                                pieChart.startAnimation();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setText(String updated) {
        DateFormat format=new SimpleDateFormat("MMM dd,yyyy");

        long milliseconds =Long.parseLong(updated);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
    date.setText("Updated at "+format.format(calendar.getTime()));

    }

    private void init(){
        TotalConform = findViewById(R.id.TotalConform);
        TotalActive = findViewById(R.id.TotalActive);
        TotalRecover = findViewById(R.id.TotalRecover);
        TotalDeath = findViewById(R.id.TotalDeath);
        TotalTest = findViewById(R.id.TotalTest);
        TodayConform= findViewById(R.id.TodayConform);
        TodayRecover= findViewById(R.id.TodayRecover);
        TodayDeath= findViewById(R.id.TodayDeath);
        pieChart= findViewById(R.id.pieChart);
        date=findViewById(R.id.date);


    }
}