package com.example.kinso.bus;

import android.widget.AutoCompleteTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by kinso on 2019-02-23.
 */

public class ClassGetXml {
    String requesurl = "http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";
    String ServiceKey = "?ServiceKey=9rcnfwjy64jClWwFsC1Jt9W6S7SFnjIuSjyl%2FkVBV8oeR8ZK2TZGiXLKyHDSVr18KEu0zlDKM4k8dP7%2FYSInzQ%3D%3D";
    String parameter1 = "&cityCode=";
    String parameter2 = "&nodeId=";
    String cityCode;
    String busstopid;
    public ClassGetXml(String cityCode, String busstopid){
        this.cityCode = cityCode;
        this.busstopid = busstopid;
    }
    String getXmlData() {
        StringBuffer buffer = new StringBuffer();
       /* try{                                                                                            //한글로 정류소이름 검색하면 인코딩해주는 것.
            address = URLEncoder.encode(autoComplete.getText().toString(), "utf-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }                                                                                                   //try
        String name ="http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getSttnNoList" +
                "?serviceKey=9rcnfwjy64jClWwFsC1Jt9W6S7SFnjIuSjyl%2FkVBV8oeR8ZK2TZGiXLKyHDSVr18KEu0zlDKM4k8dP7%2FYSInzQ%3D%3D" +
                "&cityCode="+cityCode +"&nodeNo="+autoComplete.getText().toString(); */                                                                              //정류장 nodeId를 받아오는 코드줄..
        String queryUrl = requesurl + ServiceKey + parameter1 + cityCode + parameter2 + getXml();
        System.out.println("queryUrl" + queryUrl);
        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            int eventType = 0;
            xpp.next();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("\n\n");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("arrtime")) {
                            buffer.append("\"도착시간\":");
                            xpp.next();
                            int result = Integer.parseInt(xpp.getText()) / 60;
                            buffer.append("\"" + result + "\"");
                            buffer.append(",\n");
                        } else if (tag.equals("arrprevstationcnt")) {
                            buffer.append("{ \"남은 정류장\": ");
                            xpp.next();
                            buffer.append("\"" + xpp.getText() + "\"");
                            buffer.append(",\n");
                        } else if (tag.equals("nodenm")) {
                            buffer.append("\"정류소명\": ");
                            xpp.next();
                            buffer.append("\"" + xpp.getText() + "\"");
                            buffer.append(",\n");
                        } else if (tag.equals("routeno")) {
                            buffer.append("\"버스번호\": ");
                            xpp.next();
                            buffer.append("\"" + xpp.getText() + "\"");
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item"))
                            buffer.append("},");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {                    //Try
            e.printStackTrace();
        }
        String result = buffer.toString();
        return result.substring(0, result.length() - 1);
    }//getXmlData

    String getXml() {
        StringBuffer buffer = new StringBuffer();
        String fullurl = "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getSttnNoList" +
                "?serviceKey=9rcnfwjy64jClWwFsC1Jt9W6S7SFnjIuSjyl%2FkVBV8oeR8ZK2TZGiXLKyHDSVr18KEu0zlDKM4k8dP7%2FYSInzQ%3D%3D" +
                "&cityCode=" + cityCode + "&nodeNo=" + busstopid;
        System.out.println("URL주소 : " + fullurl);
        try {
            URL url = new URL(fullurl);
            InputStream ai = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(ai, "utf-8"));
            String tag;
            xpp.next();
            int eventType = 0;
            xpp.next();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        /*buffer.append("Loading.....\n\n");*/
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if (tag.equals("items")) ;
                        else if (tag.equals("nodeid")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("items"))
                            buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
