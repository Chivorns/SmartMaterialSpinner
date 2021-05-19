package com.chivorn.smartmaterialspinner.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chivorn.smartmaterialspinner.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter<T> extends ArrayAdapter<T> implements Filterable {
    private Context context;
    private final List<T> itemList;
    private List<T> itemListFiltered;

    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.itemList = objects;
        this.itemListFiltered = objects;
    }

    @Override
    public int getCount() {
        return itemListFiltered != null ? itemListFiltered.size() : 0;
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return itemListFiltered != null ? itemListFiltered.get(position) : null;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence != null ? charSequence.toString() : null;
                if (charString == null || charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<T> filteredList = new ArrayList<>();
                    String searchText = StringUtils.removeDiacriticalMarks(charString).toLowerCase();
                    for (T row : itemList) {
                        String item = StringUtils.removeDiacriticalMarks(row.toString()).toLowerCase();
                        if (item.contains(searchText)) {
                            filteredList.add(row);
                        }
                    }
                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                filterResults.count = itemListFiltered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemListFiltered = (List<T>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}