package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.v2.ui.dialog.ContactUsDialog;

/**
 * Created by Michael Carver on 5/21/2015.
 */
public class RateMeView extends RelativeLayout {
    private static final String TAG = "RateMeView";

    private TextView _messageTextView;
    private Button _cancelButton;
    private Button _okButton;

    public Listener _listener;

    private int _state = 0;

    private static final String[] MESSAGES = {
            "Enjoying the Field Nation App?",
            "Would you mind taking a moment to rate it?",
            "Would you mind sharing your feedback?"};

    private static final String[] okText = {"LOVE IT", "YOU GOT IT", "YOU GOT IT"};

    private static final String[] cancelText = {"NOT REALLY", "NO, THANKS", "NO, THANKS"};

    public RateMeView(Context context) {
        super(context);
        init();
    }

    public RateMeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RateMeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt("state", _state);
        bundle.putParcelable("super", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof Bundle)) {
            return;
        }
        Bundle bundle = (Bundle) state;

        super.onRestoreInstanceState(bundle.getParcelable("super"));

        _state = bundle.getInt("state");
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_rate_me, this, true);

        if (isInEditMode())
            return;

        _messageTextView = findViewById(R.id.message_textview);

        _cancelButton = findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancelButton_onClick);

        _okButton = findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_messageTextView == null)
            return;

        _messageTextView.setText(MESSAGES[_state]);
        _okButton.setText(okText[_state]);
        _cancelButton.setText(cancelText[_state]);
    }

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (_state) {
                case 0: // Love it
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .label("Love")
                                    .build());
                    _state = 1;
                    populateUi();
                    break;
                case 1: // Rate me? You got it
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .action("Rate")
                                    .build());

                    Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
                    if (_listener != null)
                        _listener.onHide();
                    break;
                case 2: // Feedback? You got it
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .action("Feedback")
                                    .build());
                    if (_listener != null) {
                        ContactUsDialog.show(getContext(), null, "RateMeCard");
                        _listener.onHide();
                    }
                    break;
            }
        }
    };

    private final View.OnClickListener _cancelButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (_state) {
                case 0: // Like it? Not really
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .label("Hate")
                                    .build());
                    _state = 2;
                    populateUi();
                    break;
                case 1: // Rate me? no thanks
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .action("No Rate")
                                    .build());
                    if (_listener != null)
                        _listener.onHide();
                    break;
                case 2: // Feedback? no thanks
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("RateMeCard")
                                    .action("No Feedback")
                                    .build());
                    if (_listener != null)
                        _listener.onHide();
                    break;
            }
        }
    };

    public interface Listener {
        void onHide();
    }
}
