package edu.dartmouth.cs.xiankai_yang.stressmeter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by yangxk15 on 1/25/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class FileHelper {
    private static final String FILE_NAME = "stress_timestamp_xiankai.csv";
    private static final int WRITE_REQUEST_CODE = 0;

    private Activity mActivity;

    /**
     * Append the timestamp and the score into the csv file
     * @param timestamp
     * @param score
     * @throws IOException
     */
    public void writeToFile(Timestamp timestamp, int score) throws IOException {
        requestPermissions();
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.write(timestamp.toString() + "," + score);
        Log.d("ttt", "write " + timestamp.toString() + "," + score + " to file");
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    /**
     * Read all the records for the csv file
     * @return
     * @throws IOException
     */
    public List<Map.Entry<Timestamp, Integer>> readFromFile() throws IOException {
        requestPermissions();
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line;
        List<Map.Entry<Timestamp, Integer>> list = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            list.add(new AbstractMap.SimpleImmutableEntry<Timestamp, Integer>(
                    Timestamp.valueOf(values[0]),
                    Integer.valueOf(values[1]))
            );
        }

        bufferedReader.close();
        return list;
    }

    /**
     * Request the write external storage permission if necessary
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mActivity.checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
            }
        }
    }
}
