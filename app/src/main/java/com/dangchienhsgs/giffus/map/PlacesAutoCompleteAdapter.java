package com.dangchienhsgs.giffus.map;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private String TAG = "Places AutoComplete Adapter";

    private List<String> listDescription;

    private List<String> listReference;


    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return listDescription.size();
    }

    @Override
    public String getItem(int index) {
        return listDescription.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    Map<String, String> mapPlaces = new HashMap<String, String>();
                    mapPlaces = MapServer.autoComplete(constraint.toString());

                    listDescription = new ArrayList<String>();
                    listReference = new ArrayList<String>();

                    for (String key : mapPlaces.keySet()) {
                        listDescription.add(key);
                        listReference.add(mapPlaces.get(key));
                    }

                    Log.d(TAG, listDescription.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = listDescription;
                    filterResults.count = listDescription.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    public String getReference(int index) {
        return listReference.get(index);
    }
}