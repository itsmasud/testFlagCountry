package com.fieldnation.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.FnSpinner;
import com.fieldnation.utils.misc;

public class PhotoUploadDialog extends DialogFragmentBase {
    private static final String TAG = "PhotoUploadDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_PHOTO = "STATE_TITLE";
    private static final String STATE_FILE_NAME = "STATE_FILE_NAME";
    private static final String STATE_PHOTO_DESCRIPTION = "STATE_PHOTO_DESCRIPTION";


    // UI
    private ImageView _photoImageView;
    private EditText _fileNameEditText;
    private EditText _photoDescriptionEditText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private String _title;
    private long _taskId = 0;
    private boolean _clear = false;
    private String _fileName;
    private String _photoDescription;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static PhotoUploadDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, PhotoUploadDialog.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_FILE_NAME))
                _fileName = savedInstanceState.getString(STATE_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_PHOTO_DESCRIPTION))
                _photoDescription = savedInstanceState.getString(STATE_PHOTO_DESCRIPTION);

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_taskId != 0)
            outState.putLong(STATE_TASKID, _taskId);

        if (!misc.isEmptyOrNull(_fileName))
            outState.putString(STATE_FILE_NAME, _fileName);

        if (!misc.isEmptyOrNull(_photoDescription))
            outState.putString(STATE_PHOTO_DESCRIPTION, _photoDescription);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_photo_upload, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _fileNameEditText = (EditText) v.findViewById(R.id.filename_edittext);
        _fileNameEditText.setOnEditorActionListener(_onEditor);

        _photoDescriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _photoDescriptionEditText.setOnEditorActionListener(_onEditor);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (_clear) {
            _clear = false;
            _fileNameEditText.setText("");
            _photoDescriptionEditText.setText("");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.v(TAG, "onDestroy");
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Log.v(TAG, "onDismiss");
        _clear = true;
    }

    @Override
    public void dismiss() {
//        Log.v(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(CharSequence title, long taskId) {
        _title = (String) title;
        _taskId = taskId;
        _clear = true;
        show();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v == _fileNameEditText) {
                    _fileNameEditText.requestFocus();
                    handled = true;

                } else if (v == _photoDescriptionEditText) {
                    _photoDescriptionEditText.requestFocus();
                    handled = true;
                }
            }

            return handled;
        }
    };


    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (misc.isEmptyOrNull(_trackingIdEditText.getText().toString())) {
//                ToastClient.toast(App.get(), getString(R.string.toast_missing_traking_number), Toast.LENGTH_SHORT);
//                return;
//            }


//            if (_listener != null) {
//                        _listener.onOk(
//                                _trackingIdEditText.getText().toString(),
//                                "Other",
//                                _carrierEditText.getText().toString(),
//                                _descriptionEditText.getText().toString(),
//                                _selectedPosition_directionSpinner == 0,
//                                _taskId);
//
//            }
//            PhotoUploadDialog.this.dismiss();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
//        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId);
//
//        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite);

        void onCancel();

    }


}
