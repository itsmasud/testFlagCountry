package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.DateUtils;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class ToSDialog extends DialogFragmentBase {
    private static final String TAG = "ToSDialog";

    // Ui
    private Button _reviewTermsOfServiceButton;
    private Button _acceptButton;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static ToSDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ToSDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        }

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_tos, container, false);

        _reviewTermsOfServiceButton = (Button) v.findViewById(R.id.review_tos_button);
        _reviewTermsOfServiceButton.setOnClickListener(_reviewTermsOfServices_onClick);
        _acceptButton = (Button) v.findViewById(R.id.accept_button);
        _acceptButton.setOnClickListener(_accept_onClick);

        setCancelable(false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        populateUi();
    }


    public void show() {
        super.show();
    }

//    private void populateUi() {
//    }

    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/
    private final View.OnClickListener _reviewTermsOfServices_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=provider"));
            startActivity(intent);
        }
    };

    private final View.OnClickListener _accept_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            App.get().setToSAccepted();
            dismiss();
        }
    };

}