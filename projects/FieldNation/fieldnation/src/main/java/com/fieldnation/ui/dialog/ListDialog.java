package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 7/9/2015.
 */
public class ListDialog extends DialogFragmentBase {
    private static final String TAG = "ListDialog";

    // Ui
    private TextView _titleTextView;
    private ListView _items;

    // Data
    private ListAdapter _adapter;
    private String _title;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static ListDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ListDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_item_list, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _items = (ListView) v.findViewById(R.id.apps_listview);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void setListAdapter(ListAdapter listAdapter) {
        _adapter = listAdapter;
        populateUi();
    }

    public void setTitle(String title) {
        _title = title;
        populateUi();
    }

    private void populateUi() {
        if (_items == null)
            return;

        if (_title == null)
            _titleTextView.setVisibility(View.GONE);
        else {
            _titleTextView.setVisibility(View.VISIBLE);
            _titleTextView.setText(_title);
        }

        _items.setAdapter(_adapter);
    }

    @Override
    public void show() {
        populateUi();
        super.show();
    }
}
