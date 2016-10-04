package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;

/**
 * Created by Shoaib on 10/04/2016.
 */
public class PicChooserDialog extends DialogFragmentBase {
    private static final String TAG = "PicChooserDialog";

    // Ui
    private View _takePhotoLayout;
    private View _chooseFromGalleryLayout;


    // Data
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static PicChooserDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, PicChooserDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_pic_chooser, container, false);

        _takePhotoLayout = v.findViewById(R.id.upper_layout);
        _takePhotoLayout.setOnClickListener(_takePhoto_onClick);

        _chooseFromGalleryLayout = v.findViewById(R.id.lower_layout);
        _chooseFromGalleryLayout.setOnClickListener(_chooseFromGallery_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final View.OnClickListener _takePhoto_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "_takePhoto_onClick");

            dismiss();
        }
    };

    private final View.OnClickListener _chooseFromGallery_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "_chooseFromGallery_onClick");

            dismiss();
        }
    };


    public interface Listener {
        void onOk(int rating, boolean clearExpectations, boolean professional);

        void onCancel();
    }


}
