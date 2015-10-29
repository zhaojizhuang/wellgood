package com.wellgood.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
import com.wellgood.frame.grid.StaggeredGridView;
import com.wellgood.frame.grid.Adapter.SampleAdapter;
import com.wellgood.frame.grid.Adapter.SampleData;

import java.util.ArrayList;

public class WallView extends BaseFragment implements AbsListView.OnItemClickListener {

    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 500;

    private StaggeredGridView mGridView;
    private SampleAdapter mAdapter;

    private ArrayList<String> mData;
    View view;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		 //ViewUtils.inject(this, view); //注入view和事件
		
		
	            view=inflater.inflate(R.layout.activity_sgv_empy_view, null);
	     
	      //注入view和事件
	        ViewUtils.inject(this, view); 

	        getActivity().setTitle("公共");
	        
	        
        mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);


        View header = inflater.inflate(R.layout.list_item_header_footer, null);
        View footer = inflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("THE HEADER!");
        txtFooterTitle.setText("THE FOOTER!");

        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);
        mGridView.setEmptyView(view.findViewById(android.R.id.empty));
        mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);

        // do we have saved data?
        if (savedInstanceState != null) {
            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
            fillAdapter();
        }

        if (mData == null) {
            mData = SampleData.generateSampleData();
        }


        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);

        fetchData();
		return view;
    }

    private void fillAdapter() {
        for (String data : mData) {
            mAdapter.add(data);
        }
    }

    private void fetchData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
               // SystemClock.sleep(FETCH_DATA_TASK_DURATION);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                fillAdapter();
            }
        }.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

/*    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }*/
}
