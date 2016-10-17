package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;

/**
 * Created by Shoaib on 10/13/2016.
 */
public class PicChooserDialog extends SimpleDialog {
    private static final String TAG = "PicChooserDialog";

    // Ui
    private View _root;
    private View _takePhotoLayout;
    private View _chooseFromGalleryLayout;


    // Data
    private static Listener _listener;

    public PicChooserDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
//    public static PicChooserDialog getInstance(FragmentManager fm, String tag) {
//        Log.v(TAG, "getInstance");
//        return getInstance(fm, tag, PicChooserDialog.class);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.v(TAG, "onCreate");
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//    }
//
//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        Log.v(TAG, "onViewStateRestored");
//        super.onViewStateRestored(savedInstanceState);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.v(TAG, "onSaveInstanceState");
//        super.onSaveInstanceState(outState);
//    }



    @Override
    public void onAdded() {
        super.onAdded();


//        if (_clear) {
//            _explanationEditText.setText("");
//            _explanationEditText.setHint(getString(R.string.dialog_explanation_default));
//            _clear = false;
//            return;
//        }

        _takePhotoLayout.setOnClickListener(_takePhoto_onClick);
        _chooseFromGalleryLayout.setOnClickListener(_chooseFromGallery_onClick);

    }

    @Override
    public void onRemoved() {
        super.onRemoved();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        _root= inflater.inflate(R.layout.dialog_v2_pic_chooser, container, false);

        _takePhotoLayout = _root.findViewById(R.id.upper_layout);

        _chooseFromGalleryLayout = _root.findViewById(R.id.lower_layout);

        return _root;
    }


    public static void setListener(Listener listener){
        _listener  = listener;
    }


    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/

    private final View.OnClickListener _takePhoto_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "_takePhoto_onClick");
            dismiss(true);
            _listener.onCamera();
        }
    };

    private final View.OnClickListener _chooseFromGallery_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "_chooseFromGallery_onClick");
            dismiss(true);
            _listener.onGallery();
        }
    };


    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, PicChooserDialog.class, null);
        }

        public static void show(Context context) {
            show(context, null, PicChooserDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, null);
        }
    }


    public interface  Listener {
        void onCamera();
        void onGallery();
    }


}
