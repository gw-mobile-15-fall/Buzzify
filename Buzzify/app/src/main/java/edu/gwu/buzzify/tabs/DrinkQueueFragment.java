package edu.gwu.buzzify.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.models.DrinkInfo;
import edu.gwu.buzzify.models.DrinkItemAdapter;


public class DrinkQueueFragment extends Fragment {
    private String TAG = DrinkQueueFragment.class.getName();

    private RecyclerView mRvDrinkQueue;
    private DrinkItemAdapter mDrinkQueueAdapter;
    private ArrayList<DrinkInfo> mDrinkInfos;

    public DrinkQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_drink_queue, container, false);

        mDrinkInfos = new ArrayList<DrinkInfo>();
        createPlaceholderDrinks(mDrinkInfos);
        setupQueue(view);
        return view;
    }

    private void createPlaceholderDrinks(List<DrinkInfo> output){
        for(int i = 0; i < 10; i++)
            output.add(new DrinkInfo(getString(R.string.placeholder_drink), null));
    }

    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvDrinkQueue = (RecyclerView) view.findViewById(R.id.rvDrinkQueue);
        mRvDrinkQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvDrinkQueue.setLayoutManager(queueLayoutManager);

        mDrinkQueueAdapter = new DrinkItemAdapter(mDrinkInfos);
        mRvDrinkQueue.setAdapter(mDrinkQueueAdapter);
    }

}
