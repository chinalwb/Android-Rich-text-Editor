package com.chinalwb.are.activities;

import android.content.ClipData;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.chinalwb.are.R;
import com.chinalwb.are.adapters.AtListAdapter;
import com.chinalwb.are.models.AtItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class Are_AtPickerActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.are_activity_at_picker);
        this.mListView = this.findViewById(R.id.are_view_at_listview);
        setTitle("@");
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void prepareData() {
        new DataLoadTask().executeOnExecutor(THREAD_POOL_EXECUTOR, "");
    }

    private void showData(ArrayList<AtItem> itemsList) {
        if (this.mListView.getAdapter() == null) {
            AtListAdapter listAdapter = new AtListAdapter(this, itemsList);
            this.mListView.setAdapter(listAdapter);
        } else {
            AtListAdapter listAdapter  = (AtListAdapter) this.mListView.getAdapter();
            listAdapter.setData(itemsList);
            listAdapter.notifyDataSetChanged();
        }

    }

    private ArrayList<AtItem> makeDummyData() {
        ArrayList<AtItem> itemsList = new ArrayList<AtItem>();
        int[] iconIds = {
                R.drawable.at_1,
                R.drawable.at_2,
                R.drawable.at_3,
                R.drawable.at_4,
                R.drawable.at_5,
                R.drawable.at_6,
                R.drawable.at_7,
                R.drawable.at_8,
                R.drawable.at_9,
                R.drawable.at_10,
        };

        String[] names = {
                "Adale Lee",
                "Bill Gates",
                "Country Side",
                "Dummy Name",
                "Emily John",
                "Family Mart",
                "Glide Ant",
                "Gradle Maven",
                "Michael Jordan",
                "Steve Jobs",
        };

        for (int i = 0; i < 20; i++) {
            int index = new Random().nextInt(10);
            if (index > 9) index = 9;
            if (index < 0) index = 0;
            AtItem atItem = new AtItem(iconIds[index], names[index]);
            itemsList.add(atItem);
        }
        return itemsList;
    }

    private class DataLoadTask extends AsyncTask<String, String, ArrayList<AtItem>> {
        @Override
        protected ArrayList<AtItem> doInBackground(String... strings) {
            ArrayList<AtItem> dummyList = makeDummyData();
            return dummyList;
        }

        @Override
        protected void onPostExecute(ArrayList<AtItem> itemsList) {
            super.onPostExecute(itemsList);
            showData(itemsList);
        }
    }

}
