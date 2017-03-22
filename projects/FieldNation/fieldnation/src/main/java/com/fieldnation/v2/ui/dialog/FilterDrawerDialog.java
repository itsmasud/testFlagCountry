package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.RightDrawerDialog;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.ui.search.FilterEditScreen;
import com.fieldnation.v2.ui.search.FilterParams;

/**
 * Created by mc on 1/3/17.
 */

public class FilterDrawerDialog extends RightDrawerDialog {
    private static final String TAG = "FilterDrawerDialog";

    // Ui
    private FilterEditScreen _filterEditScreen;
    private Button _clearButton;

    // Data
    private FilterParams _filterParams;

    public FilterDrawerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_filter_drawer, container, false);

        _filterEditScreen = (FilterEditScreen) v.findViewById(R.id.filterEditScreen);
        _clearButton = (Button) v.findViewById(R.id.clear_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _clearButton.setOnClickListener(_clear_onClick);

        _filterEditScreen.setListener(_searchEditScreen_onApply);
        SearchTracker.onShow(App.get());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _filterParams = FilterParams.load(payload.getString("listId"));
        _filterEditScreen.setFilterParams(_filterParams);
    }

    private final FilterEditScreen.Listener _searchEditScreen_onApply = new FilterEditScreen.Listener() {
        @Override
        public void onApply() {
            _onOkDispatcher.dispatch(getUid(), _filterParams);
            dismiss(true);
        }
    };

    private final View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _filterParams = new FilterParams(_filterParams.listId);
            _filterParams.save();
            _onOkDispatcher.dispatch(getUid(), _filterParams);
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, String listId) {
        Bundle bundle = new Bundle();
        bundle.putString("listId", listId);
        Controller.show(context, uid, FilterDrawerDialog.class, bundle);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(FilterParams filterParams);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((FilterParams) parameters[0]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

}
