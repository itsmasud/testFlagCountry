package com.fieldnation.ui.nav;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.dialog.v2.DurationDialog;
import com.fieldnation.ui.inbox.InboxActivity;

/**
 * Created by Michael on 8/31/2016.
 */
public class FooterBarView extends RelativeLayout {
    private static final String TAG = "FooterBarView";

    // Ui
    private IconFontTextView _inboxTextView;
    private IconFontTextView _menuTextView;
    private IconFontTextView _unreadTextView;

    // Service
    private ProfileClient _profileClient;
    private DurationDialog.Controller _durationDialog;

    // Data
    private Profile _profile = null;

    public FooterBarView(Context context) {
        super(context);
        init();
    }

    public FooterBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.v(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_footerbar, this);

        if (isInEditMode())
            return;

        _inboxTextView = (IconFontTextView) findViewById(R.id.inbox_textview);
        _inboxTextView.setOnClickListener(_inbox_onClick);

        _menuTextView = (IconFontTextView) findViewById(R.id.menu_textview);
        _menuTextView.setOnClickListener(_menu_onClick);

        _unreadTextView = (IconFontTextView) findViewById(R.id.unread_textview);

        findViewById(R.id.reset_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //App.get().setNeedsConfirmation(true);
                DurationDialog.Controller.show(App.get(), TAG + ":DurationDialog");
            }
        });

        _profileClient = new ProfileClient(_profile_listener);
        _profileClient.connect(App.get());

        _durationDialog = new DurationDialog.Controller(App.get(), TAG + ".DurationDialog");
        _durationDialog.setListener(_durationDialog_listener);
        populateUi();
    }

    private DurationDialog.ControllerListener _durationDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            ToastClient.toast(App.get(), "IT WORKS!!!" + milliseconds, Toast.LENGTH_SHORT);
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (_profileClient != null && _profileClient.isConnected())
            _profileClient.disconnect(App.get());

        if (_durationDialog != null) _durationDialog.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.v(TAG, "onRestoreInstanceState");
        // todo... need to load some sort of unique key so that the controller can resync with the dialog
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.v(TAG, "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    private void populateUi() {
        if (_inboxTextView == null)
            return;

        _unreadTextView.setVisibility(GONE);
        if (_profile != null) {
            if (_profile.getUnreadMessageCount() + _profile.getNewNotificationCount() > 0)
                _unreadTextView.setVisibility(VISIBLE);
        }
    }

    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            InboxActivity.startNew(v.getContext());
        }
    };

    private final View.OnClickListener _menu_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsActivity.startNew(getContext());
        }
    };

    private final ProfileClient.Listener _profile_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subGet(false);
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            if (profile == null || failed)
                return;

            _profile = profile;
            populateUi();
        }
    };
}