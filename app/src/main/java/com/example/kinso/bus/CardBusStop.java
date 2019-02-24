package com.example.kinso.bus;

/**
 * Created by kinso on 2019-02-22.
 */

public class CardBusStop {
    public String Id,BusStopName, BusNumber, BusArriveTime;

    public CardBusStop(String Id, String BusStopName, String BusNumber, String BusArriveTime){
        this.Id =Id;
        this.BusStopName = BusStopName;
        this.BusNumber = BusNumber;
        this.BusArriveTime = BusArriveTime;
    }
}