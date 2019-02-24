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

public class AdapterBusInfoRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<CardBusInfo> BusInfoArrayList;
    private PlusStopByActivity recyclerViewClickListener;
    public AdapterBusInfoRecyclerView(ArrayList<CardBusInfo> BusInfoArrayList){
        this.BusInfoArrayList = BusInfoArrayList;
    }
    public interface RecyclerViewClickListener {
        void onItemClicked(int position, String busnumber, String busstopname);
    }

    public void setOnClickListener(PlusStopByActivity listener){
        recyclerViewClickListener = listener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_busstopnum, tv_arrivetime, tv_busstopname, tv_busnumber;
        ViewHolder(View view){
            super(view);
            tv_busstopnum = view.findViewById(R.id.tv_busstopnum);
            tv_arrivetime = view.findViewById(R.id.tv_arrivetime);
            //tv_busstopname = view.findViewById(R.id.tv_busstopname);
            tv_busnumber = view.findViewById(R.id.tv_busnumber);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_info,parent,false);
        return new AdapterBusInfoRecyclerView.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterBusInfoRecyclerView.ViewHolder viewholder = (AdapterBusInfoRecyclerView.ViewHolder) holder;
        viewholder.tv_busstopnum.setText(BusInfoArrayList.get(position).busstopnum);
        viewholder.tv_arrivetime.setText(BusInfoArrayList.get(position).arrivetime);
        //viewholder.tv_busstopname.setText("버스 정류장 이름 : "+BusInfoArrayList.get(position).busstopname);
        viewholder.tv_busnumber.setText(BusInfoArrayList.get(position).busnumber);
        if(recyclerViewClickListener!=null){
            System.out.println("AdapterBusInfoRecyclerView에서 클릭 됨.");
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onItemClicked(pos, BusInfoArrayList.get(pos).busnumber, BusInfoArrayList.get(pos).busstopname);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return BusInfoArrayList.size();
    }
}
