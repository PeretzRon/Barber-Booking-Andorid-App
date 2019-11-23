package com.hit.haircutappointments.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.SpecificDayEventListAdmin;
import com.hit.haircutappointments.Fregments.SpecificDayFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class SpecificDayForAdminAdapter extends RecyclerView.Adapter<SpecificDayForAdminAdapter.MyViewHolder> {

    public ArrayList<SpecificDayEventListAdmin> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewStartEvent;
        TextView mTextViewEndEvent;
        TextView mTextViewEventId;
        TextView mTextViewName;
        TextView mTextViewPhone;
        ImageView mImageViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewStartEvent =  itemView.findViewById(R.id.textViewStartEventTime2);
            this.mTextViewEndEvent = itemView.findViewById(R.id.textViewEndEventTime2);
            this.mImageViewStatus = itemView.findViewById(R.id.imageViewStatusEvent2);
            this.mTextViewEventId = itemView.findViewById(R.id.textViewEventId);
            this.mTextViewName = itemView.findViewById(R.id.textViewClientName2);
            this.mTextViewPhone = itemView.findViewById(R.id.textViewPhoneClient2);
        }
    }

    public SpecificDayForAdminAdapter(ArrayList<SpecificDayEventListAdmin> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_day_events_to_admin, parent, false);

        view.setOnClickListener(SpecificDayFragment.mMyOnClickListener);
        view.setOnLongClickListener(SpecificDayFragment.mMyOnLongClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewStartEvent = holder.mTextViewStartEvent;
        TextView textViewEndEvent = holder.mTextViewEndEvent;
        TextView textViewEventId = holder.mTextViewEventId;
        TextView textViewName = holder.mTextViewName;
        TextView textViewPhone = holder.mTextViewPhone;
        ImageView imageViewStatus = holder.mImageViewStatus;

        textViewStartEvent.setText(dataSet.get(listPosition).getmStartEvent());
        textViewEndEvent.setText((dataSet.get(listPosition).getmEndEvent()));
        imageViewStatus.setImageBitmap(dataSet.get(listPosition).getmStatusImage());
        textViewName.setText(dataSet.get(listPosition).getmName());
        textViewPhone.setText(dataSet.get(listPosition).getmPhone());
        textViewEventId.setText((dataSet.get(listPosition).getmEventId()));

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
