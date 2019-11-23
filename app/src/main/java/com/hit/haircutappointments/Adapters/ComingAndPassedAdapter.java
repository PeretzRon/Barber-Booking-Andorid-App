package com.hit.haircutappointments.Adapters;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.haircutappointments.Activities.MainAppActivity;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.ComingAndPassedEventsList;
import com.hit.haircutappointments.Fregments.MyComingEventsFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class ComingAndPassedAdapter extends RecyclerView.Adapter<ComingAndPassedAdapter.MyViewHolder> {

    public ArrayList<ComingAndPassedEventsList> dataSet;
    private Context context;
    private int lastPosition = -1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewFullDate;
        TextView mTextViewTime;
        ImageView mImageViewStatus;
        public ConstraintLayout viewBackground, viewForeground;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewFullDate =  itemView.findViewById(R.id.textViewFullDate);
            this.mTextViewTime = itemView.findViewById(R.id.textViewTimeEvent);
            this.mImageViewStatus = itemView.findViewById(R.id.imageViewStatusEvent);
            this.viewBackground = itemView.findViewById(R.id.view_background);
            this.viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public ComingAndPassedAdapter(ArrayList<ComingAndPassedEventsList> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_for_other_project, parent, false);

        view.setOnClickListener(MyComingEventsFragment.mMyOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewFullDate = holder.mTextViewFullDate;
        TextView textViewTime = holder.mTextViewTime;
        ImageView imageViewStatus = holder.mImageViewStatus;

        textViewFullDate.setText(dataSet.get(listPosition).getmFullDate());
        textViewTime.setText((dataSet.get(listPosition).getmTime()));
        imageViewStatus.setImageBitmap(dataSet.get(listPosition).getmImage());

       // setAnimation(holder.itemView, listPosition);
    }

//    private void setAnimation(View viewToAnimate, int position)
//    {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        {
//            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_out_right);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
