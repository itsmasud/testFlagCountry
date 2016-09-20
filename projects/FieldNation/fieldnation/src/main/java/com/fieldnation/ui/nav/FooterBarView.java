package com.fieldnation.ui.nav;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.dialog.v2.TestDialog;

/**
 * Created by Michael on 8/31/2016.
 */
public class FooterBarView extends RelativeLayout {
    private static final String TAG = "FooterBarView";

    // Ui
    private IconFontTextView _inboxTextView;
    private IconFontTextView _menuTextView;

    // Dialog Controllers
    private TestDialog.Controller _testDialog;

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
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.v(TAG, "onRestoreInstanceState");
        // todo... need to load some sort of unique key so that the controller can resync with the dialog
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.v(TAG, "onAttachedToWindow");
        _testDialog = new TestDialog.Controller(App.get());
        _testDialog.setControllerListener(dialog_listener);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.v(TAG, "onDetachedFromWindow");
        if (_testDialog != null) {
            _testDialog.disconnect(App.get());
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.v(TAG, "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    private final TestDialog.ControllerListener dialog_listener = new TestDialog.ControllerListener() {
        @Override
        public void onOk() {
            Log.v(TAG, "onOk");
        }

        @Override
        public void onCancel() {
            Log.v(TAG, "onCancel");
        }

        @Override
        public void onDismiss() {
            Log.v(TAG, "onDismiss");
        }
    };

    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //InboxActivity.startNew(v.getContext());

            TestDialog.Controller.show(App.get(), "This is a test title");
        }
    };

    private final View.OnClickListener _menu_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsActivity.startNew(getContext());
        }
    };
}