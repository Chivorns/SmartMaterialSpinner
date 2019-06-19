package com.chivorn.smartmaterialspinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class SearchableSpinnerDialog extends DialogFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = SearchableSpinnerDialog.class.getSimpleName();
    private static final String INSTANCE_LIST_ITEMS = "ListItems";
    private static final String INSTANCE_LISTENER_KEY = "OnSearchDialogEventListener";
    private static final String INSTANCE_SPINNER_KEY = "SmartMaterialSpinner";
    private ArrayAdapter searchArrayAdapter;
    private ViewGroup searchHeaderView;
    private AppCompatTextView tvSearchHeader;
    private SearchView searchView;
    private ListView searchListView;
    private TextView tvListItem;

    private boolean isEnableSearchHeader = true;
    private int headerBackgroundColor;
    private Drawable headerBackgroundDrawable;
    private int searchListItemColor;
    private int selectedSearchItemColor;
    private int selectedPosition = -1;

    private String searchHeaderText;
    private int searchHeaderTextColor;
    private String searchHint;
    private int searchDialogGravity = Gravity.TOP;

    private OnSearchDialogEventListener onSearchDialogEventListener;
    private OnSearchTextChanged onSearchTextChanged;
    private DialogInterface.OnClickListener dialogListener;
    private SmartMaterialSpinner smartMaterialSpinner;
    private boolean isDismissOnSelected = true;

    public SearchableSpinnerDialog() {
    }

    public static SearchableSpinnerDialog newInstance(SmartMaterialSpinner smartMaterialSpinner, List items) {
        SearchableSpinnerDialog searchableSpinnerDialog = new SearchableSpinnerDialog();
        Bundle args = new Bundle();
        args.putSerializable(INSTANCE_LIST_ITEMS, (Serializable) items);
        args.putSerializable(INSTANCE_SPINNER_KEY, smartMaterialSpinner);
        searchableSpinnerDialog.setArguments(args);
        return searchableSpinnerDialog;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState = setSavedInstanceState(outState);
        outState.putSerializable(INSTANCE_LISTENER_KEY, outState.getSerializable(INSTANCE_LISTENER_KEY));
        outState.putSerializable(INSTANCE_SPINNER_KEY, outState.getSerializable(INSTANCE_SPINNER_KEY));
        outState.putSerializable(INSTANCE_LIST_ITEMS, outState.getSerializable(INSTANCE_LIST_ITEMS));
        super.onSaveInstanceState(outState);
    }

    private Bundle setSavedInstanceState(Bundle savedInstanceState) {
        Bundle dialogInstanceState = this.getArguments();
        if (savedInstanceState == null || savedInstanceState.isEmpty() && dialogInstanceState != null) {
            savedInstanceState = dialogInstanceState;
        }
        return savedInstanceState;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        savedInstanceState = setSavedInstanceState(savedInstanceState);
        this.smartMaterialSpinner = (SmartMaterialSpinner) savedInstanceState.get(INSTANCE_SPINNER_KEY);
        this.onSearchDialogEventListener = smartMaterialSpinner;
        savedInstanceState.putSerializable(INSTANCE_LISTENER_KEY, onSearchDialogEventListener);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        savedInstanceState = setSavedInstanceState(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (savedInstanceState != null) {
            onSearchDialogEventListener = (OnSearchDialogEventListener) savedInstanceState.getSerializable(INSTANCE_LISTENER_KEY);
        }
        View searchLayout = inflater.inflate(R.layout.smart_material_spinner_searchable_dialog_layout, null);
        initSearchDialog(searchLayout, savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(searchLayout);

        AlertDialog dialog = builder.create();
        setGravity(dialog);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        savedInstanceState = setSavedInstanceState(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initSearchDialog(View rootView, Bundle savedInstanceState) {
        searchHeaderView = rootView.findViewById(R.id.search_header_layout);
        tvSearchHeader = rootView.findViewById(R.id.tv_search_header);
        searchView = rootView.findViewById(R.id.search_view);
        searchListView = rootView.findViewById(R.id.search_list_item);

        if (getActivity() != null) {
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            }
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        List items = savedInstanceState != null ? (List) savedInstanceState.getSerializable(INSTANCE_LIST_ITEMS) : null;
        if (items != null) {
            searchArrayAdapter = new ArrayAdapter<Object>(getActivity(), R.layout.smart_material_spinner_search_list_item_layout, items) {
                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View listView = super.getView(position, convertView, parent);
                    tvListItem = listView.findViewById(R.id.tv_search_list_item);
                    if (searchListItemColor != 0) {
                        tvListItem.setTextColor(searchListItemColor);
                    }
                    if (selectedSearchItemColor != 0 && position >= 0 && position == selectedPosition) {
                        tvListItem.setTextColor(selectedSearchItemColor);
                    }
                    return listView;
                }
            };
        }
        searchListView.setAdapter(searchArrayAdapter);
        searchListView.setTextFilterEnabled(true);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onSearchDialogEventListener != null) {
                    onSearchDialogEventListener.onSearchItemSelected(searchArrayAdapter.getItem(position), position);
                }
                getDialog().dismiss();
            }
        });

        searchListView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    scrollToSelectedItem();
                } else if (bottom > oldBottom) {
                    scrollToSelectedItem();
                }
            }
        });

        initSearchHeader();
        initSearchBody();
    }

    private void initSearchHeader() {
        if (isEnableSearchHeader) {
            searchHeaderView.setVisibility(View.VISIBLE);
        } else {
            searchHeaderView.setVisibility(View.GONE);
        }

        if (searchHeaderText != null) {
            tvSearchHeader.setText(searchHeaderText);
        }

        if (searchHeaderTextColor != 0) {
            tvSearchHeader.setTextColor(searchHeaderTextColor);
        }

        if (headerBackgroundColor != 0) {
            searchHeaderView.setBackgroundColor(headerBackgroundColor);
        } else if (headerBackgroundDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                searchHeaderView.setBackground(headerBackgroundDrawable);
            }
        }
    }

    private void initSearchBody() {
        if (searchHint != null) {
            searchView.setQueryHint(searchHint);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onSearchDialogEventListener != null) {
            onSearchDialogEventListener.onSearchableSpinnerDismiss();
        }
        super.onDismiss(dialog);
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

    @Override
    public boolean onClose() {
        return false;
    }

    public interface OnSearchDialogEventListener extends Serializable {
        void onSearchItemSelected(Object item, int position);

        void onSearchableSpinnerDismiss();
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }

    public void setOnSearchDialogEventListener(OnSearchDialogEventListener onSearchDialogEventListener) {
        this.onSearchDialogEventListener = onSearchDialogEventListener;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    public void setEnableSearchHeader(boolean enableSearchHeader) {
        isEnableSearchHeader = enableSearchHeader;
    }

    public void setSearchHeaderText(String header) {
        searchHeaderText = header;
    }

    public void setSearchHeaderTextColor(int color) {
        this.searchHeaderTextColor = color;
    }

    public void setSearchHeaderBackgroundColor(int color) {
        headerBackgroundColor = color;
        headerBackgroundDrawable = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setSearchHeaderBackgroundColor(Drawable drawable) {
        headerBackgroundDrawable = drawable;
        headerBackgroundColor = 0;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
    }

    public void setSearchListItemColor(int searchListItemColor) {
        this.searchListItemColor = searchListItemColor;
    }

    public void setSelectedSearchItemColor(int selectedSearchItemColor) {
        this.selectedSearchItemColor = selectedSearchItemColor;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public void setGravity(int gravity) {
        this.searchDialogGravity = gravity;
    }

    private void setGravity(Dialog dialog) {
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setGravity(searchDialogGravity);
        }
    }

    private void scrollToSelectedItem() {
        if (selectedPosition >= 0 && searchListView.isSmoothScrollbarEnabled()) {
            searchListView.smoothScrollToPositionFromTop(selectedPosition, 0, 0);
        }
    }
}
