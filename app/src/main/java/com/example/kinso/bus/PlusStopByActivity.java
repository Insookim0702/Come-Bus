package com.example.kinso.bus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class PlusStopByActivity extends AppCompatActivity implements AdapterBusInfoRecyclerView.RecyclerViewClickListener {
    Button moveMainActivity;
    String cityCode, busstopid;
    XmlPullParser xpp;
    EditText ed_busstopid;
    private ArrayList<CardBusInfo> BusInfoArray;
    String data, data1;
    String arrivetime, busnumber, busstopname, busstopnum;
    ImageButton searchbutton;
    RecyclerView recyclerView;
    TextView tv_busstopname;
    //ClassGetXml classGetXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_stop_by);
        recyclerView = findViewById(R.id.busInfoRecyclerView);
        recyclerView.setHasFixedSize(true);
        searchbutton = (ImageButton) findViewById(R.id.searchbutton);
        ed_busstopid = (EditText) findViewById(R.id.ed_busstopid);
        Spinner s = (Spinner) findViewById(R.id.spinner1);
        //moveMainActivity = (Button) findViewById(R.id.moveMainActivity);
        tv_busstopname = (TextView)findViewById(R.id.tv_busstopname);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusInfoArray = new ArrayList<>();
                Toast toast = Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 150);
                toast.show();
                busstopid = ed_busstopid.getText().toString();

                ClassThread th = new ClassThread(cityCode, busstopid, PlusStopByActivity.this);
                th.start();
                //th1 th = new th1(PlusStopByActivity.this);
                //th.start();
                tv_busstopname.setVisibility(View.VISIBLE);
            }
        });

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    cityCode = "11";//서울
                else if (position == 1)
                    cityCode = "23";//인천
                else if (position == 2)
                    cityCode = "25";//대전
                else if (position == 3)
                    cityCode = "31";//경기도
                else if(position ==4)
                    cityCode ="21";//부산광역시
                else if (position == 5)
                    cityCode = "35";//전라북도
                else if (position == 6)
                    cityCode = "24";//광주광역시
                else if (position == 7)
                    cityCode = "32";//강원도
                else if (position == 8)
                    cityCode = "33";//충청북도
                else if (position == 9)
                    cityCode = "34";//충청남도
                else if (position == 10)
                    cityCode = "36";//전라남도
                else if (position == 11)
                    cityCode = "22";//대구광역시
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });//setOnItemSelected
        String[] items = {"인천대 입구역", "원인재역 6번출구", "원인재역 1번출구", "38166", "38378", "38377"};
        /*AutoCompleteTextView auto = (AutoCompleteTextView)findViewById(autoComplete);*/


    }//end of onCreate;

    /*public class th1 extends Thread {
        PlusStopByActivity callbackInstance = null;
        public th1(PlusStopByActivity callback){
            this.callbackInstance = callback;
        }

        @Override
        public void run() {
            classGetXml = new ClassGetXml(cityCode, busstopid);
            data = classGetXml.getXmlData();
            //data1 = classGetXml.getXml();
            System.out.println("보자 data : " +data );
            //System.out.println("보자 data1 : " +data1 );

            System.out.println("{\"data\" :[" + data + "]}");
            String dataarray = "{\"data\" :[" + data + "]}";
            this.callbackInstance.callbackThread(dataarray);
        }
    }*/
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();
            String receive = bun.getString("DATA");
            try {
                JSONObject json = new JSONObject(receive);
                JSONArray array = json.getJSONArray("data");
                JSONObject item;
                for (int i = 0; i < array.length(); i++) {
                    item = array.getJSONObject(i);
                    busstopnum = item.getString("남은 정류장");
                    arrivetime = item.getString("도착시간");
                    busstopname = item.getString("정류소명");
                    busnumber = item.getString("버스번호");
                    System.out.println("busstopnum " + busstopnum);
                    System.out.println("arrivetime " + arrivetime);
                    //System.out.println("busstopname " + busstopname);
                    System.out.println("busnumber " + busnumber);
                    BusInfoArray.add(new CardBusInfo(busstopnum, arrivetime, busstopname, busnumber));
                    AdapterBusInfoRecyclerView adapterbusInfo = new AdapterBusInfoRecyclerView(BusInfoArray);
                    adapterbusInfo.setOnClickListener(PlusStopByActivity.this);
                    recyclerView.setAdapter(adapterbusInfo);
                }
                tv_busstopname.setText(busstopname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void callbackThread(String data){
        Message msg = handler.obtainMessage();
        Bundle bun = new Bundle();
        bun.putString("DATA",data);
        msg.setData(bun);
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClicked(int position, final String busnumber, final String busstopname) {
        System.out.println("눌렸다!");
        AlertDialog.Builder alert = new AlertDialog.Builder(PlusStopByActivity.this);
        alert.setPositiveButton("저장하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //1. SQlite에 저장하기.
                ClassSqlite sqlite = new ClassSqlite(PlusStopByActivity.this,"savetable", null,1);
                sqlite.insert(busstopname,busstopid,busnumber,cityCode);
                System.out.println("SQlite에 저장하였습니다.");
                //2. 메인액티비티로 넘어간다.
                Intent move = new Intent(PlusStopByActivity.this, MainActivity.class);
                move.putExtra("BusNumber", busnumber);
                move.putExtra("BusStopName", busstopname);
                move.putExtra("BusStopId", busstopid);
                move.putExtra("CityCode", cityCode);
                PlusStopByActivity.this.startActivity(move);
                finish();
            }
        });
        alert.setTitle("즐겨찾기 등록").setMessage("정류장 "+busstopname+ "버스 "+busnumber+"번 를 저장합니다.");
        alert.show();
    }

}
