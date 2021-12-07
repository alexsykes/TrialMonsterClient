package com.alexsykes.trialmonsterclient.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alexsykes.trialmonsterclient.R;
import com.alexsykes.trialmonsterclient.support.ResultListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ResultListActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://android.trialmonster.uk/";
    public static final String TAG = "Info";

    private String trialid;
    public static int numlaps, numsections;
    ArrayList<HashMap<String, String>> theResults;

    RecyclerView rv;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i(TAG, "onCreate: ");
        trialid = getIntent().getExtras().getString("trialid");
        getJSONDataset(BASE_URL + "getTrialResultJSONdata.php?id=" + trialid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: ");
        initialiseAdapter();
    }

    private void getJSONDataset(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetData extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                processJSON(s);
            }

            private void processJSON(String json) {
                try {
                    // Parse string data into JSON
                    JSONArray jsonArray = new JSONArray(json);

                    JSONArray theTrial = jsonArray.getJSONObject(0).getJSONArray("trial details");
                    JSONObject trialDetails = theTrial.getJSONObject(0);
                    displayTrialDetails(trialDetails);

                    // JSONArray courseCount = jsonArray.getJSONObject(1).getJSONArray("entry count");
                    String results = jsonArray.getJSONObject(2).getJSONArray("results").toString();
                    theResults = getResultList(results);

                   // JSONArray nonStarters = jsonArray.getJSONObject(3).getJSONArray("nonstarters");

                    rv = findViewById(R.id.rv);


                    llm = new LinearLayoutManager(rv.getContext());
                    rv.setLayoutManager(llm);
                    rv.setHasFixedSize(true);
                    initialiseAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void displayTrialDetails(JSONObject trialDetails) throws JSONException {
                String location = trialDetails.getString("location");

                // Get numsections and numlaps from trialDetails
                numlaps = trialDetails.getInt("numlaps");
                numsections = trialDetails.getInt("numsections");

                // Display title data in ActionBar
                String club = trialDetails.getString("club");
                String title = club + " - " + trialDetails.getString("eventname") + " - " + location;
                getSupportActionBar().setTitle(title);
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

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
                    return null;
                }
            }
        }

        //creating asynctask object and executing it
        GetData getJSON = new GetData();
        getJSON.execute();
    }

    private void initialiseAdapter() {
        ResultListAdapter adapter = new ResultListAdapter(theResults);
        rv.setAdapter(adapter);
    }

    private ArrayList<HashMap<String, String>> getResultList(String json) throws JSONException {

        theResults = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        String gone = Integer.toString(View.GONE);
    String name;
        for (int index = 0; index < jsonArray.length(); index++) {
            // Create new HashMap
            HashMap<String, String> theResultHash = new HashMap<>();
    name = jsonArray.getJSONObject(index).getString("name");
            Log.i("Name", "getResultList: " + name);
            // ut data from JSON
            theResultHash.put("rider", jsonArray.getJSONObject(index).getString("rider"));
            theResultHash.put("name", jsonArray.getJSONObject(index).getString("name"));
            theResultHash.put("machine", jsonArray.getJSONObject(index).getString("machine"));
            theResultHash.put("total", jsonArray.getJSONObject(index).getString("total"));
            theResultHash.put("class", jsonArray.getJSONObject(index).getString("class"));
            theResultHash.put("course", jsonArray.getJSONObject(index).getString("course"));
            theResultHash.put("cleans", jsonArray.getJSONObject(index).getString("cleans"));
            theResultHash.put("ones", jsonArray.getJSONObject(index).getString("ones"));
            theResultHash.put("twos", jsonArray.getJSONObject(index).getString("twos"));
            theResultHash.put("threes", jsonArray.getJSONObject(index).getString("threes"));
            theResultHash.put("fives", jsonArray.getJSONObject(index).getString("fives"));
            theResultHash.put("missed", jsonArray.getJSONObject(index).getString("missed"));
            theResultHash.put("sectionscores", jsonArray.getJSONObject(index).getString("sectionscores"));
            theResultHash.put("scores", jsonArray.getJSONObject(index).getString("scores"));
            theResultHash.put("dnf", jsonArray.getJSONObject(index).getString("dnf"));
            theResultHash.put("position", "");
            theResultHash.put("index", String.valueOf(index));
            theResultHash.put("visibility", gone);

            // Add to array
            theResults.add(theResultHash);
        }
        theResults = addPosition(theResults);

        return theResults;
    }

    private ArrayList<HashMap<String, String>> addPosition(ArrayList<HashMap<String, String>> theResults) {
        HashMap<String, String> previous, current;
        String currCourse, prevCourse;

        int numResults = theResults.size();
        int position = 1;


        current = theResults.get(0);
        current.put("position", String.valueOf(position));

        for (int index = 1; index < numResults; index++) {
            position++;
            previous = theResults.get(index - 1);
            current = theResults.get(index);

            prevCourse = previous.get("course");
            currCourse = current.get("course");


            if (prevCourse != null && !prevCourse.equals(currCourse)) {
                position = 1;
            }

            current.put("position", String.valueOf(position));

            if (Objects.equals(current.get("sectionscores"), previous.get("sectionscores"))) {
                current.put("position", "=");
            }

        }
        return theResults;
    }
}