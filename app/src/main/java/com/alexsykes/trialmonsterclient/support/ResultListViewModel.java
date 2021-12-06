package com.alexsykes.trialmonsterclient.support;

import android.os.AsyncTask;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultListViewModel extends ViewModel {
    private static final String BASE_URL = "https://android.trialmonster.uk/";
    ArrayList<HashMap<String, String>> theResults;
    JSONObject trialDetails;
    private JSONArray nonStarters;

    public void putResults(ArrayList<HashMap<String, String>> results) {
        theResults = results;
    }

    public ArrayList<HashMap<String, String>> getTheResults() {
        return theResults;
    }

    public void putTrialsDetails(JSONObject trialDetails) {
        this.trialDetails = trialDetails;
    }

    public void putNonStarters(JSONArray nonStarters) {
        this.nonStarters = nonStarters;
    }

    public void getData(String trialid) {

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

                // Set up ArrayLists of trialsDetails, theResults and nonStarters
                private void processJSON(String json) {
                    try {
                        // Parse string data into JSON
                        JSONArray jsonArray = new JSONArray(json);
                        JSONArray nonStarters = jsonArray.getJSONObject(3).getJSONArray("nonstarters");
                        JSONArray theTrial = jsonArray.getJSONObject(0).getJSONArray("trial details");
                        JSONObject trialDetails = theTrial.getJSONObject(0);

                        putTrialsDetails(trialDetails);
                        putNonStarters(nonStarters);
                        // displayTrialDetails(trialDetails);

                        // JSONArray courseCount = jsonArray.getJSONObject(1).getJSONArray("entry count");
                        String results = jsonArray.getJSONObject(2).getJSONArray("results").toString();
                        // theResults = getResultList(results);

//                    rv = findViewById(R.id.rv);
//
//
//                    llm = new LinearLayoutManager(rv.getContext());
//                    rv.setLayoutManager(llm);
//                    rv.setHasFixedSize(true);
//                    initialiseAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                //in this method we are fetching the json string
                @Override
                protected String doInBackground(Void... voids) {

                    try {
                        //creating a URL
                        URL url = new URL(BASE_URL + "getTrialResultJSONdata.php?id=" + trialid);

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
}
