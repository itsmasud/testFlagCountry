package com.fieldnation.v2.ui;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.OverScrollRecyclerView;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedScreen extends RelativeLayout {
    private static final String TAG = "UnsyncedScreen";

    // UI
    private OverScrollRecyclerView _recyclerView;
    private UnsyncedAdapter _unsyncedAdapter = new UnsyncedAdapter();

    public UnsyncedScreen(Context context) {
        super(context);
        init();
    }

    public UnsyncedScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnsyncedScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(App.get()).inflate(R.layout.screen_unsynced, this, true);

        _recyclerView = findViewById(R.id.recyclerView);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _recyclerView.setAdapter(_unsyncedAdapter);

        _unsyncedAdapter.refresh();
    }
}
