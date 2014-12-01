package com.fieldnation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

/**
 * Created by michael.carver on 12/1/2014.
 */
public class SignOffFragment extends Fragment {
    private static final String TAG = "ui.SignOffFragment";


    // Ui
    private ImageView _companyImageView;
    private TextView _titleTextView;
    private TextView _descriptionTextView;
    private LinearLayout _timeLinearLayout;
    private LinearLayout _tasksLinearLayout;
    private Button _signOffButton;


    // Data
    private Workorder _workorder;

    /*-*************----------**************-*/
    /*-             Life Cycle              -*/
    /*-*************----------**************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signoff, container, false);

        _companyImageView = (ImageView) v.findViewById(R.id.company_imageview);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _descriptionTextView = (TextView) v.findViewById(R.id.description_textview);
        _timeLinearLayout = (LinearLayout) v.findViewById(R.id.time_list);
        _tasksLinearLayout = (LinearLayout) v.findViewById(R.id.tasks_list);
        _signOffButton = (Button) v.findViewById(R.id.signoff_button);
        _signOffButton.setOnClickListener(_signOff_onClick);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    public void setData(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }


    private void populateUi() {
        if (_signOffButton == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_workorder.getTitle());
        _descriptionTextView.setText(_workorder.getFullWorkDescription());

        
    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private View.OnClickListener _signOff_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO STUB .onClick()
            Log.v(TAG, "STUB .onClick()");
        }
    };
}
