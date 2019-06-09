package com.abhi.weatherapp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.abhi.weatherapp.R;
import com.abhi.weatherapp.model.db.WeatherHistoryDto;

import java.util.ArrayList;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder> {

    private ArrayList<WeatherHistoryDto> mValues;
    private Context context;
    private String agentRoleType;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sno, mob;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            sno = (TextView) view.findViewById(R.id.tempcity);
            mob = (TextView) view.findViewById(R.id.tempdegree);
        }
    }

    public WeatherHistoryAdapter(Context context, ArrayList<WeatherHistoryDto> items) {
        this.mValues = items;
        this.context = context;
        this.agentRoleType = agentRoleType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.sno.setText(mValues.get(position).getCityName());
        holder.mob.setText(mValues.get(position).getTemperature());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
