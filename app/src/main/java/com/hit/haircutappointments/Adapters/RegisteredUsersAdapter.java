package com.hit.haircutappointments.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.haircutappointments.Classes.RecycleViewsClasses.RegisteredUsersList;
import com.hit.haircutappointments.Fregments.RegisteredUsersFragment;
import com.hit.haircutappointments.R;

import java.util.ArrayList;


public class RegisteredUsersAdapter extends RecyclerView.Adapter<RegisteredUsersAdapter.MyViewHolder> {

    public ArrayList<RegisteredUsersList> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewFullName;
        TextView mTextViewPhone;
        TextView mEmail;
        ImageView mImageViewGender;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mTextViewFullName =  itemView.findViewById(R.id.textViewFullName);
            this.mTextViewPhone = itemView.findViewById(R.id.textViewPhoneNumber);
            this.mEmail = itemView.findViewById(R.id.textViewEmail);
            this.mImageViewGender = itemView.findViewById(R.id.imageViewGender);
        }
    }

    public RegisteredUsersAdapter(ArrayList<RegisteredUsersList> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_registered_users, parent, false);

        view.setOnClickListener(RegisteredUsersFragment.mMyOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewFullDate = holder.mTextViewFullName;
        TextView textViewPhoneNumber = holder.mTextViewPhone;
        TextView textViewEmail = holder.mEmail;
        ImageView imageViewGender = holder.mImageViewGender;

        textViewFullDate.setText(dataSet.get(listPosition).getmFullName());
        textViewPhoneNumber.setText((dataSet.get(listPosition).getmPhoneNumber()));
        textViewEmail.setText((dataSet.get(listPosition).getmEmail()));
        imageViewGender.setImageBitmap(dataSet.get(listPosition).getmGenderImage());
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
