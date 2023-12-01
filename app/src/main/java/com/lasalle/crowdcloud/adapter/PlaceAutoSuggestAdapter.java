package com.lasalle.crowdcloud.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import fragments.Preference;
import model.PlaceAPI;

public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {
    ArrayList<String> results;
    int resAPI;
    Context context;
    PlaceAPI placeAPI = new PlaceAPI();


    public PlaceAutoSuggestAdapter(Context context, int resAPI){
        super(context,resAPI);
        this.context = context;
        this.resAPI=resAPI;
    }

    @Override
    public int getCount(){
        return results.size();
    }
    @Override
    public String getItem(int pos) {
        return(results.get(pos));
    }

    @Override
    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint!=null){
                    results=placeAPI.autoComplete(constraint.toString());
                    filterResults.values=results;
                    filterResults.count=results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results!=null && results.count>0){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
