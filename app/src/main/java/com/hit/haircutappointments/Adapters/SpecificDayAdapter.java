package com.hit.haircutappointments.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.haircutappointments.Classes.RecycleViewsClasses.SpecificDayEventList;
import com.hit.haircutappointments.Fregments.DayListFragment;
import com.hit.haircutappointments.Fregments.SpecificDayFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class SpecificDayAdapter extends RecyclerView.Adapter<SpecificDayAdapter.MyViewHolder> {

    public ArrayList<SpecificDayEventList> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewStartEvent;
        TextView mTextViewEndEvent;
        TextView mTextViewEventId;
        ImageView mImageViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewStartEvent =  itemView.findViewById(R.id.textViewStartEventTime);
            this.mTextViewEndEvent = itemView.findViewById(R.id.textViewEndEventTime);
            this.mImageViewStatus = itemView.findViewById(R.id.imageViewStatusEvent);
            this.mTextViewEventId = itemView.findViewById(R.id.textViewEventId);
        }
    }

    public SpecificDayAdapter(ArrayList<SpecificDayEventList> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_day_events, parent, false);

        view.setOnClickListener(SpecificDayFragment.mMyOnClickListener);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewStartEvent = holder.mTextViewStartEvent;
        TextView textViewEndEvent = holder.mTextViewEndEvent;
        TextView textViewEventId = holder.mTextViewEventId;
        ImageView imageViewStatus = holder.mImageViewStatus;

        textViewStartEvent.setText(dataSet.get(listPosition).getmStartEvent());
        textViewEndEvent.setText((dataSet.get(listPosition).getmEndEvent()));
        imageViewStatus.setImageBitmap(dataSet.get(listPosition).getmStatusImage());
        textViewEventId.setText((dataSet.get(listPosition).getmEventId()));

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
