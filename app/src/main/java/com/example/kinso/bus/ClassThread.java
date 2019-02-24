package com.example.kinso.bus;

/**
 * Created by kinso on 2019-02-23.
 */


public class ClassThread extends Thread {
    ClassGetXml classGetXml;
    String data;
    String cityCode, busstopid;
    PlusStopByActivity callbackInstance = null;
    public ClassThread(String cityCocde, String busstopid, PlusStopByActivity callback) {
        this.cityCode = cityCocde;
        this.busstopid = busstopid;
        this.callbackInstance = callback;
    }
    public void run() {
        classGetXml = new ClassGetXml(cityCode, busstopid);
        data = classGetXml.getXmlData();
        //data1 = classGetXml.getXml();
        System.out.println("보자 data : " + data);
        //System.out.println("보자 data1 : " +data1 );

        System.out.println("{\"data\" :[" + data + "]}");
        String dataarray = "{\"data\" :[" + data + "]}";
        this.callbackInstance.callbackThread(dataarray);
    }
}

