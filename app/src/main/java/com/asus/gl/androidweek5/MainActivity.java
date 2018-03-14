package com.asus.gl.androidweek5;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SensorEventListener {

    private HashMap<String,String> dictionary;
    private String the_word;
    private TextView txt_the_word;
    private ArrayList<String> wordList;
    private ListView lvWordList;
    ArrayAdapter<String> adapter;
    ArrayList<String> definitons;

    TextView tx,ty,tz;
    SensorManager sm;
    Sensor acc;

    private final int MULTIPLE_CHOICE =5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvWordList = findViewById(R.id.lv_main);
        txt_the_word = findViewById(R.id.the_word);
        lvWordList.setOnItemClickListener(this);
        definitons = new ArrayList<>();
        readFile();
        askQuestion();

        tx= findViewById(R.id.txtX);
        ty= findViewById(R.id.txtY);
        tz= findViewById(R.id.txtZ);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void readFile(){
        dictionary = new HashMap<>();
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.dict));
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            String[] split = line.split("-");
            if(split.length<2)
                continue;
            dictionary.put(split[0],split[1]);
        }
        scanner.close();

        wordList= new ArrayList<>(dictionary.keySet());



    }
    private void askQuestion(){
        Collections.shuffle(wordList);
        the_word = wordList.get(0);
        txt_the_word.setText( the_word );

        definitons.clear();
        for(int i=0;i<MULTIPLE_CHOICE;i++){
            definitons.add(dictionary.get(wordList.get(i)));
        }
        Collections.shuffle(definitons);
        adapter = new ArrayAdapter<String>(this,
                R.layout.my_item_view,
                R.id.txtWord,
                definitons);
        lvWordList.setAdapter(adapter);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String meaning = definitons.get(i);
        if(meaning.equals(dictionary.get(the_word))){
            Toast.makeText(MainActivity.this, "You got it!", Toast.LENGTH_SHORT).show();
            askQuestion();
        }
        else{
            Toast.makeText(MainActivity.this, "You better work!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        tx.setText(""+sensorEvent.values[0]);
        ty.setText(""+sensorEvent.values[1]);
        tz.setText(""+sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
