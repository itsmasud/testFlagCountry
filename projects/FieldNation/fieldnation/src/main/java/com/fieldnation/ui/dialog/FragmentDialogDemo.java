package com.fieldnation.ui.dialog;

/**
 * Created by user on 6/26/2015.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.fieldnation.Log;
import com.fieldnation.service.data.workorder.WorkorderClient;
// ...

public class FragmentDialogDemo extends FragmentActivity {
    private static String TAG = "FragmentDialogDemo";

    private FeedbackDialog _feedbackDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Hello", "-101");
//        showEditDialog();

        // Integration of feedback dialog
        _feedbackDialog = FeedbackDialog.getInstance(getSupportFragmentManager(), TAG);
        _feedbackDialog.setListener(_feedbackDialog_onOk);
        showFeedbackDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(fm, "fragment_edit_name");
    }


    private void showFeedbackDialog() {
        FragmentManager fm = getSupportFragmentManager();
        _feedbackDialog.show(fm, "fragment_edit_name");
    }


    private final FeedbackDialog.Listener _feedbackDialog_onOk = new FeedbackDialog.Listener() {
        @Override
        public void onOk(String message) {
//            WorkorderClient.actionSetClosingNotes(getActivity(), _workorder.getWorkorderId(), message);
//            _workorder.dispatchOnChange();
//            setLoading(true);
        }

        @Override
        public void onCancel() {

        }

    };

}
