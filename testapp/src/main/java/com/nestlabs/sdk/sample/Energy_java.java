/*
 * Copyright 2016, Google Inc.
 * Copyright 2014, Nest Labs Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nestlabs.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Energy_java extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Energy_java.class.getSimpleName();

    //textview
    private TextView energyGotoText;
    private ImageView energyGotoView;
    private ImageView energyLeftView;

    private TextView energyInflux;
    private TextView energyOutflux;
    private TextView energyCurPower;
    private TextView energyTodayPower;
    private TextView energyMonthPower;
    private TextView energyYearPower;
    private TextView energyTotalPower;
    private TextView energyCarbonReduction;

    private Button energyDayButton;
    private Button energyWeekButton;
    private Button energyMonthButton;

    private LineChart day_lineChart;
    private LineChart week_lineChart;
    private LineChart month_lineChart;

    private static Retrofit retrofit = Constants.retrofit;
    public static final String BASE_URL = "http://test.growatt.com/v1/";
    private static final String CUR_POWER = "%.1fKW";
    private static final String POWER = "%.1fKWh";
    private static final String CARBON = "%.1fton";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy);

        //register
        energyLeftView = (ImageView) findViewById(R.id.Energy_lefticon);
        energyGotoText = (TextView) findViewById(R.id.Energy_to_lighting_title);
        energyGotoView = (ImageView) findViewById(R.id.Energy_to_lighting_icon);

        energyInflux = (TextView) findViewById(R.id.energy_influx);
        energyOutflux = (TextView) findViewById(R.id.energy_outflux);
        energyCurPower = (TextView) findViewById(R.id.energy_cur_power);
        energyTodayPower = (TextView) findViewById(R.id.energy_today_energy);
        energyMonthPower = (TextView) findViewById(R.id.energy_monthly_energy);

        energyYearPower = (TextView) findViewById(R.id.energy_yearly_energy);
        energyTotalPower = (TextView) findViewById(R.id.energy_total_energy);
        energyCarbonReduction = (TextView) findViewById(R.id.energy_carbon_offset);

        energyDayButton = (Button) findViewById(R.id.energy_day_button);
        energyWeekButton = (Button) findViewById(R.id.energy_week_button);
        energyMonthButton = (Button) findViewById(R.id.energy_month_button);
        day_lineChart = (LineChart) findViewById(R.id.day_linechart);
        week_lineChart = (LineChart) findViewById(R.id.week_linechart);
        month_lineChart = (LineChart) findViewById(R.id.month_linechart);

        energyLeftView.setOnClickListener(this);
        energyGotoText.setOnClickListener(this);
        energyGotoView.setOnClickListener(this);
        energyDayButton.setOnClickListener(this);
        energyWeekButton.setOnClickListener(this);
        energyMonthButton.setOnClickListener(this);

        //register the growatt
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Constants.retrofit = retrofit;
        }
        day_lineChart.setDrawBorders(true);
        week_lineChart.setDrawBorders(true);
        month_lineChart.setDrawBorders(true);

        updateViews();
    }

    /**
     * Identifies setting to update from click event, and updates the thermostat control using the Nest API.
     * NOTE: The Works with Nest product configuration must have write access permission
     *       in order to update the selected thermostat, and away permission for setting away status.
     * @param v the view component
     */
    @Override
    public void onClick(View v) {

        Random random = new Random();

        switch (v.getId()) {
            //to lighting page
            case R.id.Energy_to_lighting_title:
            case R.id.Energy_to_lighting_icon:
                Intent intent_hue=new Intent(Energy_java.this,hue_activity.class);
                startActivity(intent_hue);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

                //back to dashboard
            case R.id.Energy_lefticon:
                Intent intent_dashboard=new Intent(Energy_java.this,DashBoard.class);
                startActivity(intent_dashboard);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            //day graph
            case R.id.energy_day_button:
                day_lineChart.setVisibility(View.VISIBLE);
                week_lineChart.setVisibility(View.INVISIBLE);
                month_lineChart.setVisibility(View.INVISIBLE);
                energyInflux.setText("0");
                energyOutflux.setText("0");
                energyDayButton.setBackgroundColor(Color.BLACK);
                energyWeekButton.setBackgroundColor(Color.GRAY);
                energyMonthButton.setBackgroundColor(Color.GRAY);
                break;

            //week graph
            case R.id.energy_week_button:
                day_lineChart.setVisibility(View.INVISIBLE);
                week_lineChart.setVisibility(View.VISIBLE);
                month_lineChart.setVisibility(View.INVISIBLE);
                energyInflux.setText("1");
                energyOutflux.setText("1");
                energyDayButton.setBackgroundColor(Color.GRAY);
                energyWeekButton.setBackgroundColor(Color.BLACK);
                energyMonthButton.setBackgroundColor(Color.GRAY);
                break;

            //month graph
            case R.id.energy_month_button:

                day_lineChart.setVisibility(View.INVISIBLE);
                week_lineChart.setVisibility(View.INVISIBLE);
                month_lineChart.setVisibility(View.VISIBLE);
                energyInflux.setText("2");
                energyOutflux.setText("2");
                energyDayButton.setBackgroundColor(Color.GRAY);
                energyWeekButton.setBackgroundColor(Color.GRAY);
                energyMonthButton.setBackgroundColor(Color.BLACK);
                break;

        }
    }

    /**
     * Update the value part
     */
    private void updateValueRange() {

        if(retrofit == null){
//            Toast.makeText(getApplicationContext(), "Configuration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        energyCurPower.setText(String.format(CUR_POWER,1.0));
        energyTodayPower.setText(String.format(POWER,2.0));
        energyMonthPower.setText(String.format(POWER,2.0));
        energyYearPower.setText(String.format(POWER,2.0));
        energyTotalPower.setText(String.format(POWER,2.0));
        energyCarbonReduction.setText(String.format(CARBON,2.0));

    }

    /**
     * Update the chart part
     */
    private void updateChartRange() {

        if(retrofit == null){
//            Toast.makeText(getApplicationContext(), "Configuration failed", Toast.LENGTH_SHORT).show();
            return;
        }
        //day
        List<Entry> entries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i + 1, random.nextInt(240) + 30));

        }

        LineDataSet dataSet = new LineDataSet(entries, "能源数据");
        LineData lineData = new LineData(dataSet);
        day_lineChart.setData(lineData);

        //week
        List<Entry> entries_week = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries_week.add(new Entry(i + 1, random.nextInt(240) + 30));

        }

        LineDataSet dataSet_week = new LineDataSet(entries_week, "每周数据");
        LineData lineData_week = new LineData(dataSet_week);
        week_lineChart.setData(lineData_week);

        //month
        //growatt need
        List<Entry> entries_month = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries_month.add(new Entry(i + 1, random.nextInt(240) + 30));

        }

        LineDataSet dataSet_month = new LineDataSet(entries_month, "每月数据");
        LineData lineData_month = new LineData(dataSet_month);
        month_lineChart.setData(lineData_month);

        day_lineChart.setVisibility(View.VISIBLE);
        week_lineChart.setVisibility(View.INVISIBLE);
        month_lineChart.setVisibility(View.INVISIBLE);
        energyInflux.setText("0");
        energyOutflux.setText("0");
        energyDayButton.setBackgroundColor(Color.BLACK);
        energyWeekButton.setBackgroundColor(Color.GRAY);
        energyMonthButton.setBackgroundColor(Color.GRAY);

    }

    /**
     * Updates views to show current state from thermostat and structures.
     */
    private void updateViews() {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateValueRange();
                updateChartRange();
            }
        });
    }
}
