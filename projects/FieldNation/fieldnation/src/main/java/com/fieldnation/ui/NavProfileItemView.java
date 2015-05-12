package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 2/23/2015.
 */
public class NavProfileItemView extends RelativeLayout {
    private static final String TAG = "NavProfileItemView";

    private ProfilePicView _picView;
    private TextView _nameTextView;
    private TextView _companyTextView;


    public NavProfileItemView(Context context) {
        super(context);
        init();
    }

    public NavProfileItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavProfileItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_nav_profile, this);

        if (isInEditMode())
            return;

        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _nameTextView = (TextView) findViewById(R.id.name_textview);
        _companyTextView = (TextView) findViewById(R.id.company_textview);
    }
}
