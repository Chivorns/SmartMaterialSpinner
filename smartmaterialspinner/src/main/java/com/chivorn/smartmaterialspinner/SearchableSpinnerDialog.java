package com.chivorn.smartmaterialspinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.chivorn.smartmaterialspinner.util.SoftKeyboardUtil;

import java.io.Serializable;
import java.util.List;

public class SearchableSpinnerDialog extends DialogFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String ITEMS = "items";
    private ArrayAdapter searchArrayAdapter;
    private ListView searchListView;
    private SearchableItem searchableItem;
    private OnSearchTextChanged onSearchTextChanged;
    private SearchView searchView;
    private String searchDialogTitle;
    private DialogInterface.OnClickListener dialogListener;

    public SearchableSpinnerDialog() {
    }

    public static SearchableSpinnerDialog newInstance(List items) {
        SearchableSpinnerDialog multiSelectExpandableFragment = new SearchableSpinnerDialog();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);
        multiSelectExpandableFragment.setArguments(args);
        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (savedInstanceState != null) {
            searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }

        View rootView = inflater.inflate(R.layout.searchable_dialog_layout, null);
        initSearchDialog(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        String strTitle = searchDialogTitle == null ? "Select Item" : searchDialogTitle;
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        //  getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", searchableItem);
        super.onSaveInstanceState(outState);
    }

    public void setTitle(String strTitle) {
        searchDialogTitle = strTitle;
    }

    public void setOnSearchDialogItemClickListener(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    private void initSearchDialog(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = rootView.findViewById(R.id.search_view);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
        List items = (List) getArguments().getSerializable(ITEMS);
        searchListView = rootView.findViewById(R.id.search_list_item);
        searchArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        searchListView.setAdapter(searchArrayAdapter);
        searchListView.setTextFilterEnabled(true);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchableItem.onSearchItemClickListener(searchArrayAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            ((ArrayAdapter) searchListView.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) searchListView.getAdapter()).getFilter().filter(s);
        }
        if (onSearchTextChanged != null) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchItemClickListener(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }
}
