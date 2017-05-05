package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.search.SearchEditText;
import com.fieldnation.ui.workorder.WorkOrderActivity;

/**
 * Created by mc on 12/21/16.
 */

public class SearchDialog extends SimpleDialog {
    private static final String TAG = "SearchDialog";

    // Ui
    private SearchEditText _searchEdit;

    // Services
    private WorkorderClient _workorderClient;

    public SearchDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _searchEdit = new SearchEditText(context);
        return _searchEdit;
    }

    @Override
    public void onStart() {
//        _searchEdit.setListener(_search_editText);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    public void onStop() {
        if (_workorderClient != null) _workorderClient.disconnect(App.get());

        super.onStop();
    }

/*    private final SearchEditText.Listener _search_editText = new SearchEditText.Listener() {
        @Override
        public void startSearch(String searchString) {
            _workorderClient.subGet(Long.parseLong(_searchEdit.getText()));
            WorkorderClient.get(App.get(), Long.parseLong(_searchEdit.getText()), false);
        }

        @Override
        public void onTextChanged(CharSequence s) {
        }
    };*/

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
            _workorderClient.unsubGet(workorderId);
            if (workorder == null || failed) {
                ToastClient.toast(App.get(), "The work order you are looking for has been assigned to another provider, canceled, or does not exist.", Toast.LENGTH_LONG);
            } else {
                ActivityResultClient.startActivity(
                        App.get(),
                        WorkOrderActivity.makeIntentShow(App.get(), (int) workorderId),
                        R.anim.activity_slide_in_right,
                        R.anim.activity_slide_out_left);
            }
        }
    };

    public static void show(Context context) {
        Controller.show(context, null, SearchDialog.class, null);
    }
}
