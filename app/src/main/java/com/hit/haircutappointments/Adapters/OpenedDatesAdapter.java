package com.hit.haircutappointments.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.haircutappointments.Classes.RecycleViewsClasses.MainDateList;
import com.hit.haircutappointments.Fregments.DayListFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class OpenedDatesAdapter extends RecyclerView.Adapter<OpenedDatesAdapter.MyViewHolder> {

    public ArrayList<MainDateList> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewDate;
        TextView mDayNameTextView;
        TextView mHouMuchEventAvailable;
        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewDate =  itemView.findViewById(R.id.DateTextView);
            this.mDayNameTextView = itemView.findViewById(R.id.DayNameTextView);
            this.mHouMuchEventAvailable = itemView.findViewById(R.id.textViewHowMuchAvailable);
            this.mImageView = itemView.findViewById(R.id.imageViewDayList);
        }
    }

    public OpenedDatesAdapter(ArrayList<MainDateList> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(DayListFragment.mMyOnClickListener);
        view.setOnLongClickListener(DayListFragment.mMyOnLongClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewDate = holder.mTextViewDate;
        TextView dayNameOfDate = holder.mDayNameTextView;
        TextView howMuchEventAvailable = holder.mHouMuchEventAvailable;
        ImageView mImageView1 = holder.mImageView;

        textViewDate.setText(dataSet.get(listPosition).getmDate());
        dayNameOfDate.setText(dataSet.get(listPosition).getmNameDayDate());
        howMuchEventAvailable.setText(dataSet.get(listPosition).getmHotMuchEventsAvailable());
        mImageView1.setImageBitmap(dataSet.get(listPosition).getmImageView());
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
