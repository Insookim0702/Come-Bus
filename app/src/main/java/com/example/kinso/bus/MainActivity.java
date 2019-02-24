package com.example.kinso.bus;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterStopByRecyclerView.OnLongClickListener {
    Button movePlusActivity;
    private RecyclerView recyclerview;
    private ArrayList<CardBusStop> arraylist;
    AdapterStopByRecyclerView adapter;
    ClassGetXml classGetXml;
    String id, busstopname, busstopid, busnumber, cityid, MainActivityBusArriveTime;
    ClassSqlite sqlite;
    Map<String, String> busInfomap;
    Handler mHandler;
    private ProgressDialog mProgressDialog;
    JSONArray array;
    ArrayList<String> arrTimeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        arraylist = new ArrayList<CardBusStop>();
        movePlusActivity = (Button) findViewById(R.id.movePlusActivity);
        recyclerview.setHasFixedSize(true);
        Intent getintent = getIntent();
        String arr = getintent.getExtras().getString("arriveTime");
        System.out.println("스플래쉬에서 넘어온 값 : " + arr);
        sqlite = new ClassSqlite(MainActivity.this, "savetable", null, 1);
        array = sqlite.getResult();
        JSONObject list;

        if(arr==null){
            for(int k=0;k<array.length();k++){
                try {
                    list = array.getJSONObject(k);
                    String cityid = list.getString("cityid");
                    String busstopid = list.getString("busstopid");
                    ClassThreadForMain th = new ClassThreadForMain(cityid, busstopid, MainActivity.this);
                    th.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mHandler = new Handler();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = ProgressDialog.show(MainActivity.this, "데이터 읽는 중 ..", "잠시만 기다려 주세요.", true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                                JSONObject item;
                                for (int i = 0; i < array.length(); i++) {
                                    try {
                                        item = array.getJSONObject(i);
                                        id = item.getString("id");
                                        busstopname = item.getString("busstopname");
                                        busstopid = item.getString("busstopid");
                                        busnumber = item.getString("busnumber");
                                        cityid = item.getString("cityid");
                                        //System.out.println("나올까??\n" + data);
                                        System.out.println("id" + id);
                                        System.out.println("busstopname" + busstopname);
                                        System.out.println("busstopid" + busstopid);
                                        System.out.println("busnumber" + busnumber);
                                        System.out.println("cityid" + cityid);
                                        System.out.println("MainActivityBusArriveTime" + arrTimeArray.get(i));
                                        arraylist.add(new CardBusStop(id, busstopname, busnumber, arrTimeArray.get(i)));
                                        adapter = new AdapterStopByRecyclerView(arraylist);
                                        adapter.setOnClickListener(MainActivity.this);
                                        recyclerview.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000);
                }
            });//runOnUiThread

        }else{// 스플레시바에서 넘어온거라면...
            for(int k=0;k<array.length();k++){
                try {
                    list = array.getJSONObject(k);
                    String cityid = list.getString("cityid");
                    String busstopid = list.getString("busstopid");
                    ClassThreadForMain th = new ClassThreadForMain(cityid, busstopid, MainActivity.this);
                    th.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String[] arrTimeArray = arr.split(",");
            JSONObject item;
            for (int i = 0; i < array.length(); i++) {
                try {
                    item = array.getJSONObject(i);
                    id = item.getString("id");
                    busstopname = item.getString("busstopname");
                    busstopid = item.getString("busstopid");
                    busnumber = item.getString("busnumber");
                    cityid = item.getString("cityid");
                    //System.out.println("나올까??\n" + data);
                    System.out.println("id" + id);
                    System.out.println("busstopname" + busstopname);
                    System.out.println("busstopid" + busstopid);
                    System.out.println("busnumber" + busnumber);
                    System.out.println("cityid" + cityid);
                    System.out.println("MainActivityBusArriveTime" + arrTimeArray[i]);
                    arraylist.add(new CardBusStop(id, busstopname, busnumber, arrTimeArray[i]));
                    adapter = new AdapterStopByRecyclerView(arraylist);
                    adapter.setOnClickListener(MainActivity.this);
                    recyclerview.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        movePlusActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveplusActivity = new Intent(MainActivity.this, PlusStopByActivity.class);
                startActivity(moveplusActivity);
            }
        });
    }
    //길게 눌렀을 때 삭제하기
    @Override
    public void onLongItemClicked(final int position, final String id) {
        Toast.makeText(MainActivity.this, "길게 누르기", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Sqlite에서 삭제
                sqlite = new ClassSqlite(MainActivity.this, "savetable", null, 1);
                sqlite.delete(id);
                arraylist.remove(position);
                adapter = new AdapterStopByRecyclerView(arraylist);
                recyclerview.setAdapter(adapter);
            }

        });
        alert.setTitle("삭제").setMessage("삭제합니다.");
        alert.show();
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
    @Override
    public void onBackPressed(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("종료하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();;
                System.runFinalization();
                System.exit(0);
            }
        });
        alert.setNegativeButton("취소하기",null);
        alert.setTitle("종료").setMessage("앱을 종료하시겠습니까?");
        alert.show();
    }

}
