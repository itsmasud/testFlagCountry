package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 2/23/2015.
 */
public class NavProfileDetailListView extends RelativeLayout {
    private static final String TAG = "ui.NavProfileDetailListView";

    private LinearLayout _profileList;
    private LinearLayout _addLayout;
    private LinearLayout _manageLayout;

    public NavProfileDetailListView(Context context) {
        super(context);
        init();
    }

    public NavProfileDetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavProfileDetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_nav_profile_detail, this);

        if (isInEditMode())
            return;

        _profileList = (LinearLayout) findViewById(R.id.profile_list);

        _addLayout = (LinearLayout) findViewById(R.id.add_layout);
        _addLayout.setOnClickListener(_add_onClick);

        _manageLayout = (LinearLayout) findViewById(R.id.manage_layout);
        _manageLayout.setOnClickListener(_manage_onClick);
    }

    private final OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final OnClickListener _manage_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
