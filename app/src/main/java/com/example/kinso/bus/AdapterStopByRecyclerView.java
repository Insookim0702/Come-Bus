package com.example.kinso.bus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kinso on 2019-02-22.
 */

public class AdapterStopByRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardBusStop> BusStopArrayList;
    private MainActivity recyclerViewClickListener;

    public void setOnClickListener(MainActivity listener) {
        recyclerViewClickListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onLongItemClicked(int postion, String id);
    }

    public interface OnLongClickListener {
        //길게 눌렀을 때 삭제하기
        void onLongItemClicked(int position, String id);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_busstopname, tv_arrivetime, tv_busnumber, tv_cityid;
        MyViewHolder(View view) {
            super(view);
            tv_busstopname = (TextView) view.findViewById(R.id.tv_busstopname);
            tv_busnumber = (TextView) view.findViewById(R.id.tv_busnumber);
            tv_arrivetime = (TextView)view.findViewById(R.id.tv_arrivetime);
        }
    }

    public AdapterStopByRecyclerView(ArrayList<CardBusStop> BusStopArrayList) {
        this.BusStopArrayList = BusStopArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new AdapterStopByRecyclerView.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterStopByRecyclerView.MyViewHolder myViewHolder = (AdapterStopByRecyclerView.MyViewHolder) holder;
        myViewHolder.tv_busstopname.setText(BusStopArrayList.get(position).BusStopName);
        myViewHolder.tv_busnumber.setText(BusStopArrayList.get(position).BusNumber);
        myViewHolder.tv_arrivetime.setText(BusStopArrayList.get(position).BusArriveTime);
        if (recyclerViewClickListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onLongItemClicked(pos, BusStopArrayList.get(pos).Id);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return BusStopArrayList.size();
    }


}
