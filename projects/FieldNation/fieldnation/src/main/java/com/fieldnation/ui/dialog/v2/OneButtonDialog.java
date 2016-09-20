package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fieldnation.R;
import com.fieldnation.fndialog.Dialog;

/**
 * Created by Michael on 9/19/2016.
 */
public class OneButtonDialog extends FrameLayout implements Dialog {
    private static final String TAG = "OneButtonDialog";

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_BODY = "body";
    private static final String PARAM_RESPONSE = "response";
    private static final int PARAM_RESPONSE_OK = 0;
    private static final int PARAM_RESPONSE_CANCEL = 1;
    private static final int PARAM_RESPONSE_DISMISS = 2;


    public OneButtonDialog(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.dialog_v2_one_button, this);

        if (isInEditMode())
            return;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void show(Bundle payload) {
        setVisibility(VISIBLE);
    }

    @Override
    public void onRestoreDialogState(Parcelable savedState) {

    }

    @Override
    public Parcelable onSaveDialogState() {
        return null;
    }

    @Override
    public void dismiss() {
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {

        private ControllerListener _listener;

        public Controller(Context context) {
            super(context, OneButtonDialog.class);
        }

        public void setControllerListener(ControllerListener listener) {
            _listener = listener;
        }

        @Override
        protected void onComplete(Bundle response) {

        }
    }

    public interface ControllerListener {
        void onOk();

        void onDismiss();
    }
}
