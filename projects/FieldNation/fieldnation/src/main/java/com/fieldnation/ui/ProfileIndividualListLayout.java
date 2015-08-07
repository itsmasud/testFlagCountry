package com.fieldnation.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.ui.workorder.detail.DiscountView;
import com.fieldnation.ui.workorder.detail.WorkorderRenderer;

import java.util.Random;

/**
 * Created by Shoyeb Ahmed on 08/07/2015.
 */
public class ProfileIndividualListLayout extends RelativeLayout{
    private static final String TAG = "ProfileIndividualListLayout";

    // UI
    private LinearLayout _profileContainerLayout;
    private ProfilePicView _picView;
    private TextView _profileNameTextView;
    private TextView _profileCompanyTextView;
    private TextView _providerIdTextView;


    public ProfileIndividualListLayout(Context context) {
        super(context);
        init();
    }

    public ProfileIndividualListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileIndividualListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_individual, this);

        if (isInEditMode())
            return;

        // profile
        _profileContainerLayout = (LinearLayout) findViewById(R.id.profile_container);
        _profileContainerLayout.setOnClickListener(_profileContainerLayout_onClick);

        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);
        _profileNameTextView = (TextView) findViewById(R.id.profile_name_textview);
        _profileNameTextView.setVisibility(View.GONE);
        _profileCompanyTextView = (TextView) findViewById(R.id.profile_company_textview);
        _profileCompanyTextView.setVisibility(View.GONE);
        _providerIdTextView = (TextView) findViewById(R.id.providerid_textview);
        _providerIdTextView.setVisibility(View.GONE);
    }



    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final OnClickListener _profileContainerLayout_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };



}
