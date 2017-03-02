package edu.dartmouth.cs.xiankai_yang.stressmeter;

/**
 * Created by yangxk15 on 1/24/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RecordFragment extends Fragment {
    public static final String IMAGE_ID = "Image Id";
    public static final int IMAGE_NUM_PER_PAGE = 16;
    public static final int NUM_OF_PAGES = 3;

    private static final String TAG = "Record Fragment";
    private static final int DETAIL_REQUEST_CODE = 0;

    int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        // Set adapter for populating the images into the grid
        ((GridView) view.findViewById(R.id.gridview)).setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return IMAGE_NUM_PER_PAGE;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = convertView == null
                        ? new ImageView(getContext())
                        : (ImageView) convertView;
                imageView.setLayoutParams(new GridView.LayoutParams(360, 360));
                imageView.setImageResource(PSM.getGridById(currentPage + 1)[position]);
                return imageView;
            }
        });

        // Set listener to create detail activity upon item click
        ((GridView) view.findViewById(R.id.gridview)).setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view,
                                            int position,
                                            long id) {
                        MainActivity.mNotificationManager.cancel(MainActivity.NOTIFICATION_CODE);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(IMAGE_ID, PSM.getGridById(currentPage + 1)[position]);
                        startActivityForResult(intent, DETAIL_REQUEST_CODE);
                    }
                }
        );

        // Set more images button to change the current page
        ((Button) view.findViewById(R.id.more_images)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mNotificationManager.cancel(MainActivity.NOTIFICATION_CODE);
                currentPage = (currentPage + 1) % NUM_OF_PAGES;
                ((GridView) getView().findViewById(R.id.gridview)).invalidateViews();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
            int imageScore = data.getIntExtra(DetailActivity.SCORE, -1);
            try {
                // Write the result to the csv file
                new FileHelper(this.getActivity()).writeToFile(
                        new Timestamp(System.currentTimeMillis()), imageScore);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            } finally {
                getActivity().finish();
            }
        }
    }
}
