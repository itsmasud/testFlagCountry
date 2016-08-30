package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.Debug;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;

/**
 * Created by Michael Carver on 5/26/2015.
 */
public class ContactTileView extends RelativeLayout {
    private static final String TAG = "ContactTileView";

    private TextView _nameTextView;
    private TextView _phoneTextView;
    private TextView _titleTextView;

    private String _name;
    private String _phone;
    private String _title;

    public ContactTileView(Context context) {
        super(context);
        init();
    }

    public ContactTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_tile, this);

        if (isInEditMode())
            return;

        _nameTextView = (TextView) findViewById(R.id.name_textview);
        _phoneTextView = (TextView) findViewById(R.id.phone_textview);
        _titleTextView = (TextView) findViewById(R.id.title_textview);

        setOnClickListener(_this_onClick);
    }

    public void setData(String name, String phone, String title) {
        _name = name;
        _phone = phone;
        _title = title;

        populateUi();
    }

    private void populateUi() {
        if (_nameTextView == null)
            return;

        if (_name == null) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        _nameTextView.setText(_name);

        if (_phone != null) {
            _phoneTextView.setText(_phone);
            _phoneTextView.setVisibility(VISIBLE);
        } else {
            _phoneTextView.setVisibility(GONE);
        }

        if (_title != null) {
            _titleTextView.setText(_title);
            _titleTextView.setVisibility(VISIBLE);
        } else {
            _titleTextView.setVisibility(GONE);
        }
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_phone != null) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + _phone));
                try {
                    if (getContext().getPackageManager().queryIntentActivities(callIntent, 0).size() > 0) {
                        getContext().startActivity(callIntent);
                    } else {
                        ToastClient.toast(getContext(), "Couldn't call number: " + _phone, Toast.LENGTH_LONG);
                    }
                } catch (Exception ex) {
                    Debug.logException(ex);
                }

            }
        }
    };
}
