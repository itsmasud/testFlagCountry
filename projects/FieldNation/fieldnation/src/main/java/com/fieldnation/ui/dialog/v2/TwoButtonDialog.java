package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.SimpleDialog;

/**
 * Created by Michael on 9/21/2016.
 */

public class TwoButtonDialog extends SimpleDialog {
    private static final String TAG = "TwoButtonDialog";

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _okButton;
    private Button _cancelButton;

    public TwoButtonDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_two_button, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context) {
            super(context, TwoButtonDialog.class);
        }

        public static void show(Context context, Bundle params) {
            show(context, TwoButtonDialog.class, params);
        }

        public static void dismiss(Context context) {
            dismiss(context, TwoButtonDialog.class);
        }
    }

    public static abstract class ControllerListener implements com.fieldnation.fndialog.Controller.Listener {
        @Override
        public void onComplete(Bundle response) {

        }
    }
}
