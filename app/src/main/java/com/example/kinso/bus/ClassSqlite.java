package com.example.kinso.bus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kinso on 2019-02-22.
 */

public class ClassSqlite extends SQLiteOpenHelper {
    private Context context;
    SQLiteDatabase db;
    public ClassSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context =context;
    }
    //DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db){
        //새로운 테이블 생성
        db.execSQL("CREATE TABLE usertable(id INTEGER PRIMARY KEY AUTOINCREMENT, busstopname TEXT, busstopid TEXT, busnumber TEXT, cityid TEXT)");
        Toast.makeText(context, "Table 생성 완료", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(String busstopname, String busnumber, String busstopid, String cityid){
        db = getWritableDatabase();
        db.execSQL("INSERT INTO usertable(busstopname, busstopid, busnumber, cityid) VALUES('"+busstopname+"','"+busstopid+"','"+busnumber+"','"+cityid+"')");
        db.close();
    }

    public JSONArray getResult(){
        db = getWritableDatabase();
        String result ="";
        Cursor cursor = db.rawQuery("SELECT * FROM usertable",null);
        int count=0;
        while (cursor.moveToNext()){
                result+="{\"id\":\""+cursor.getString(0)+"\","
                        +"\"busstopname\":\""+cursor.getString(1)+"\","
                        +"\"busnumber\":\""+cursor.getString(2)+"\","
                        +"\"busstopid\":\""+cursor.getString(3)+"\","
                        +"\"cityid\":\""+cursor.getString(4)+"\"}";
        }
        System.out.println("sqlite에 저장된 값 : "+result);
        String sqlitedata = "{\"sqlitedata\":["+result.replace("}{","},{")+"]}";
        JSONArray array = null;
        try {
            JSONObject json = new JSONObject(sqlitedata);
            array = json.getJSONArray("sqlitedata");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return array;
    }
    ArrayList<String> ReadBusnumber(){
        ArrayList<String> arr = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT id,busstopid FROM usertable ORDER BY id", null);
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                arr.add(cursor.getString(1));
            }
        }
        cursor.close();
        db.close();
        return arr;

    }
    void delete(String id){
        db = getWritableDatabase();
        db.execSQL("DELETE FROM usertable WHERE id ='"+id+"'");
        db.close();
        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();

    }
}
