package edu.gwu.buzzify.drinks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.ViewHolderClickListener;

/**
 * Created by cheng on 11/2/15.
 */
public class DrinkItemAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    private List<DrinkInfo> mDrinkInfos;
    private ViewHolderClickListener mListener;

    public DrinkItemAdapter(List<DrinkInfo> drinks, ViewHolderClickListener listener){
        mDrinkInfos = drinks;
        mListener = listener;
    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drink_queue_item, parent, false);
        return new DrinkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position){
        DrinkInfo info = mDrinkInfos.get(position);
        holder.setDrinkName(info.drinkName);
        holder.setCustomerName(info.customerName);
        holder.setDrinkIcon(info.iconUrl);
        holder.setListener(mListener);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount(){
        return mDrinkInfos.size();
    }
}
