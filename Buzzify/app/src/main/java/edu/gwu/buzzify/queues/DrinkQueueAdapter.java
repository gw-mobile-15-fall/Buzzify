package edu.gwu.buzzify.queues;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.gwu.buzzify.R;

/**
 * Created by cheng on 11/2/15.
 */
public class DrinkQueueAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    private List<DrinkInfo> mDrinkInfos;

    public DrinkQueueAdapter(List<DrinkInfo> drinks){
        mDrinkInfos = drinks;
    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drink_queue_item, parent, false);
        return new DrinkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position){
        DrinkInfo info = mDrinkInfos.get(position);
        holder.setDrinkName(info.name);
        holder.setDrinkIcon(info.iconUrl);
    }

    @Override
    public int getItemCount(){
        return mDrinkInfos.size();
    }
}
