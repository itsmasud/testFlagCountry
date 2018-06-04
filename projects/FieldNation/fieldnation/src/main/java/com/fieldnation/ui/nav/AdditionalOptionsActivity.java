package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.SwitchUserOverlayView;

/**
 * Created by Michael on 9/1/2016.
 */
public class AdditionalOptionsActivity extends AuthSimpleActivity {
    private static final String TAG = "AdditionalOptionsActivity";

    // Ui
    private ViewStub _switchUserOverlayViewStub;
    private SwitchUserOverlayView _switchUserOverlay = null;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_additional_options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AdditionalOptionsScreen) findViewById(R.id.screen)).setListener(_screen_listener);

        _switchUserOverlayViewStub = (ViewStub) findViewById(R.id.switchUserOverlay_viewstub);

        setTitle("Additional Options");
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
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
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        _appMessagingClient.subUserSwitched();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _appMessagingClient.unsubUserSwitched();
    }

    private final AdditionalOptionsScreen.Listener _screen_listener = new AdditionalOptionsScreen.Listener() {
        @Override
        public void onSwitchUser(long userId) {
            if (_switchUserOverlay == null) {
                _switchUserOverlay = (SwitchUserOverlayView) _switchUserOverlayViewStub.inflate();
            }
            _switchUserOverlay.startSwitch();
            ProfileClient.switchUser(App.get(), userId);
        }
    };

    private final AppMessagingClient _appMessagingClient = new AppMessagingClient() {
        @Override
        public void onUserSwitched(Profile profile) {
            //startNew(App.get());
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, AdditionalOptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityClient.startActivity(intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
