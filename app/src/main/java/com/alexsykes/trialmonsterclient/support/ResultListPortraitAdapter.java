package com.alexsykes.trialmonsterclient.support;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alexsykes.trialmonsterclient.R;
import com.alexsykes.trialmonsterclient.activities.ResultListActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultListPortraitAdapter extends RecyclerView.Adapter<ResultListPortraitAdapter.ResultViewHolder> {

    ArrayList<HashMap<String, String>> theResultList;
    HashMap<String, String> theResult;
    OnItemClickListener listener;

    public ResultListPortraitAdapter(ArrayList<HashMap<String, String>> theResultList) {
        this.theResultList = theResultList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Point to data holder layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_list_item, viewGroup, false);
        ResultViewHolder resultViewHolder = new ResultViewHolder(v);
        Log.i("Info", "onCreateViewHolder: ");
        return resultViewHolder;
    }

    @Override
    public void onBindViewHolder(final ResultViewHolder resultViewHolder, int i) {
        theResult = theResultList.get(i);
        int numlaps = ResultListActivity.numlaps;
        int visibility;

        int backgroundColor = ContextCompat.getColor(resultViewHolder.itemView.getContext(), R.color.offWhite);
        int white = ContextCompat.getColor(resultViewHolder.itemView.getContext(), R.color.colorWhite);

        // Insert * character after each section
        // Create sectionsScore data string

        String theSectionScores, theSections, separator;
        separator = " | ";
        theSectionScores = theResult.get("sectionscores");
        theSections = "";
        char theChar;

        for (int pos = 0; pos < theSectionScores.length(); pos++) {
            theChar = theSectionScores.charAt(pos);
            if (pos % numlaps == 0) {
                theSections = theSections + separator;
            }
            theSections = theSections + theChar;
        }
        theSections = (theSections + separator).trim();
        // End

        // Format background
        if (i % 2 != 0) {
            resultViewHolder.itemView.setBackgroundColor(backgroundColor);
        } else {
            resultViewHolder.itemView.setBackgroundColor(white);
        }

        if (theResult.get("position").equals("1")) {
            resultViewHolder.topRow.setVisibility(View.VISIBLE);

        } else {
            resultViewHolder.topRow.setVisibility(View.GONE);
        }

        String theClass = theResult.get("class");
        if (theClass.contains("Adult")) {
            theClass = "";
        }

        resultViewHolder.positionTextView.setText(theResult.get("position"));
        resultViewHolder.riderTextView.setText(theResult.get("rider"));
        resultViewHolder.nameTextView.setText(theResult.get("name"));
        resultViewHolder.machineTextView.setText(theResult.get("machine"));
        resultViewHolder.totalTextView.setText(theResult.get("total"));
        resultViewHolder.classTextView.setText(theClass);

        // Check for missed sections
        // Display position if dnf = 0
        resultViewHolder.courseTextView.setText(theResult.get("course"));

        if (theResult.get("dnf").equals("1")) {
            resultViewHolder.positionTextView.setText("");
            resultViewHolder.totalTextView.setText("DNF");
        }
    }

    @Override
    public int getItemCount() {
        return theResultList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(HashMap<String, String> theResult);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView, riderTextView, machineTextView, totalTextView, nameTextView, cleansTextView, onesTextView, twosTextView, threesTextView, fivesTextView, missedTextView, courseTextView, classTextView, scoresTextView;
        LinearLayout topRow;


        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            riderTextView = itemView.findViewById(R.id.riderTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            machineTextView = itemView.findViewById(R.id.machineTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            classTextView = itemView.findViewById(R.id.classTextView);
            courseTextView = itemView.findViewById(R.id.courseTextView);
            topRow = itemView.findViewById(R.id.topRow);
        }

        public void bind(final HashMap<String, String> theScore, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = theScore.get(("id"));
                    Context context = v.getContext();
                }
            });

        }
    }
}
