package com.example.kinso.bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    ClassSqlite sqlite;
    JSONArray array;
    ArrayList<String> arrTimeArray;
    String busnumber, MainActivityBusArriveTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sqlite = new ClassSqlite(SplashActivity.this, "savetable", null, 1);
        array = sqlite.getResult();
        JSONObject list;
        for(int k=0;k<array.length();k++){
            try {
                System.out.println("Splash에서 도착시간 알아오기 실행 중...");
                list = array.getJSONObject(k);
                String cityid = list.getString("cityid");
                String busstopid = list.getString("busstopid");
                ClassThreadForSplash th = new ClassThreadForSplash(cityid, busstopid, SplashActivity.this);
                th.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try{
            Thread.sleep(1300);
            System.out.println("1300시간 지나고...");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        String result ="";
        for(int i=0;i<arrTimeArray.size();i++){
            result +=arrTimeArray.get(i);
            if(i!=arrTimeArray.size()-1){
                result+=",";
            }
        }
        System.out.println("Splash 결과값 : " + result);
        Intent moveMain = new Intent(this, MainActivity.class);
        moveMain.putExtra("arriveTime", result);
        SplashActivity.this.startActivity(moveMain);
        finish();
    }
    public void callbackThread(String dataarray) {
        arrTimeArray = new ArrayList<>();
        System.out.println("callback 실행");
        try {
            JSONObject json = new JSONObject(dataarray);
            JSONArray array = json.getJSONArray("data");
            JSONObject item;
            ArrayList<String> arr = sqlite.ReadBusnumber();
            System.out.println("callback busnumber : " + busnumber);
            for(int k=0;k<arr.size();k++){
                for (int i = 0; i < array.length(); i++) {
                    item = array.getJSONObject(i);
                    System.out.println("버스번호 나와라"+arr.get(k));
                    System.out.println("sqlite에 저장되어 있는 버스번호 : "+item.getString("버스번호"));
                    if (arr.get(k).equals(item.getString("버스번호"))) {
                        MainActivityBusArriveTime = item.getString("도착시간");
                        arrTimeArray.add(MainActivityBusArriveTime);
                        System.out.println("Handler에서 버스 도착 시간 :" + MainActivityBusArriveTime);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
