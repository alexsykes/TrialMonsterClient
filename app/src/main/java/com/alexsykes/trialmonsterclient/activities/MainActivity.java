package com.alexsykes.trialmonsterclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.alexsykes.trialmonsterclient.R;
import com.alexsykes.trialmonsterclient.support.TrialListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://android.trialmonster.uk/";

    // Data
    ArrayList<HashMap<String, String>> theTrialList;

    // Values
    private static final String TAG = "Info";

    // UI Components
    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        llm = new LinearLayoutManager(this);
        getDataset(BASE_URL + "getResultList.php");
    }

    private void setupUI() {
        // rv = findViewById(R.id.trialListRV);
    }

    private void initializeAdapter() {
        TrialListAdapter adapter = new TrialListAdapter(theTrialList);
        rv.setAdapter(adapter);
    }

    private void getDataset(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetResults extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //loadIntoListView(s);

                    Log.i(TAG, "onPostExecute: " + s);
                    theTrialList = getTrialList(s);

                    /*
                        Section dealing with RecyclerView starts here
                     */

                    rv = findViewById(R.id.trialListRV);
                    rv.setLayoutManager(llm);
                    rv.setHasFixedSize(true);
                    initializeAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    // readStream(in);

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    String error = e.getLocalizedMessage();
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetResults getJSON = new GetResults();
        getJSON.execute();
    }


    private ArrayList<HashMap<String, String>> getTrialList(String json) throws JSONException {

        theTrialList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int index = 0; index < jsonArray.length(); index++) {
            HashMap<String, String> theTrialHash = new HashMap<>();
            String name = jsonArray.getJSONObject(index).getString("name");
            String date = jsonArray.getJSONObject(index).getString("date");
            String club = jsonArray.getJSONObject(index).getString("club");
            String id = jsonArray.getJSONObject(index).getString("id");
            String location = jsonArray.getJSONObject(index).getString("location");
            theTrialHash.put("name", name);
            theTrialHash.put("date", date);
            theTrialHash.put("id", id);
            theTrialHash.put("club", club);
            theTrialHash.put("location", location);
            theTrialList.add(theTrialHash);
        }
        return theTrialList;
    }

    public void onClickCalled(String id) {
    }
}