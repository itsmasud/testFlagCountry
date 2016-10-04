package com.fieldnation.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.dialog.OneButtonDialog;

/**
 * Created by Michael on 7/7/2016.
 */
public class EditSearchActivity extends AuthSimpleActivity {
    private static final String TAG = "EditSearchActivity";

    // Ui
    private SearchEditScreen _searchEditScreen;

    // Dialog
    private OneButtonDialog _notAvailableDialog;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_edit_search;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.work_order_search);

        _searchEditScreen = (SearchEditScreen) findViewById(R.id.searchEdit_screen);
        _searchEditScreen.setListener(_searchEditScreen_listener);

        _notAvailableDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":notAvailableDialog");
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        _searchEditScreen.reset();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _notAvailableDialog.setData(getString(R.string.not_available),
                getString(R.string.workorder_not_found),
                getString(R.string.btn_close), null);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private final SearchEditScreen.Listener _searchEditScreen_listener = new SearchEditScreen.Listener() {
        @Override
        public void showNotAvailableDialog() {
            _notAvailableDialog.show();
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, EditSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
