package com.hit.haircutappointments.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.PriceList;
import com.hit.haircutappointments.Fregments.PriceListFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.MyViewHolder> {

    public ArrayList<PriceList> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewAction;
        TextView mTextViewCost;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewAction =  itemView.findViewById(R.id.textViewActionHairCut);
            this.mTextViewCost = itemView.findViewById(R.id.textViewPrice);
        }
    }

    public PriceListAdapter(ArrayList<PriceList> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_prices, parent, false);

        view.setOnClickListener(PriceListFragment.mMyOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewAction = holder.mTextViewAction;
        TextView textViewCost = holder.mTextViewCost;

        textViewAction.setText(dataSet.get(listPosition).getmAction());
        textViewCost.setText(String.format("%s NIS", dataSet.get(listPosition).getmCost()));
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
