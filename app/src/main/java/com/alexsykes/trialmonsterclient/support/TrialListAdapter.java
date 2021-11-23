package com.alexsykes.trialmonsterclient.support;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alexsykes.trialmonsterclient.R;
import com.alexsykes.trialmonsterclient.activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TrialListAdapter extends RecyclerView.Adapter<TrialListAdapter.TrialViewHolder> {
    ArrayList<HashMap<String, String>> theTrialList;
    HashMap<String, String> theTrial;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HashMap<String, String> theTrial);
    }

    public TrialListAdapter(ArrayList<HashMap<String, String>> theTrialList) {
        this.theTrialList = theTrialList;
    }

    public TrialListAdapter(ArrayList<HashMap<String, String>> theTrialList, OnItemClickListener listener) {
        this.theTrialList = theTrialList;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TrialViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Point to data holder layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trial_row, viewGroup, false);
        TrialViewHolder trialViewHolder = new TrialViewHolder(v);
        return trialViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrialViewHolder trialViewHolder, final int i) {
        // Populate TextViews with data
        theTrial = theTrialList.get(i);

        String theDate = theTrial.get("date".toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(theDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int backgroundColor = ContextCompat.getColor(trialViewHolder.itemView.getContext(), R.color.offWhite);
        int white = ContextCompat.getColor(trialViewHolder.itemView.getContext(), R.color.colorWhite);
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM d, yyyy");
        theDate = targetFormat.format(sourceDate);

        trialViewHolder.nameTextView.setText(theTrial.get("name".toString()));
        trialViewHolder.clubTextView.setText(theTrial.get("club".toString()));
        trialViewHolder.venueTextView.setText(theTrial.get("location".toString()));
        trialViewHolder.dateTextView.setText(theDate);
        trialViewHolder.bind(theTrial, listener);

        if (i % 2 != 0) {
            trialViewHolder.itemView.setBackgroundColor(backgroundColor);
        } else {
            trialViewHolder.itemView.setBackgroundColor(white);
        }
    }

    @Override
    public int getItemCount() {
        return theTrialList.size();
    }

    public static class TrialViewHolder extends RecyclerView.ViewHolder {
        TextView clubTextView;
        TextView dateTextView;
        TextView nameTextView;
        TextView venueTextView;

        public TrialViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            clubTextView = (TextView) itemView.findViewById(R.id.clubTextView);
            venueTextView = (TextView) itemView.findViewById(R.id.venueTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(final HashMap<String, String> theTrial, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = theTrial.get("id").toString();
                    Context context = v.getContext();
                    ((MainActivity) context).onClickCalled(id);

                }
            });
        }
    }
}