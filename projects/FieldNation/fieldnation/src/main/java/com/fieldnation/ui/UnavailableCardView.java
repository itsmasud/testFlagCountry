package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 6/24/2015.
 */
public class UnavailableCardView extends FrameLayout {
    private static final String TAG = "UnavailableCardView";

    // Ui
    private TextView _titleTextView;
    private TextView _captionTexView;
    private Button _actionButton;

    // Data
    private WorkorderDataSelector _displayView;

    public UnavailableCardView(Context context) {
        super(context);
        init();
    }

    public UnavailableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnavailableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_unavailable_card, this);

        if (isInEditMode())
            return;
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _captionTexView = (TextView) findViewById(R.id.caption_textview);
        _actionButton = (Button) findViewById(R.id.action_button);

        _actionButton.setOnClickListener(_action_OnClick);
    }

    public void setData(WorkorderDataSelector displayView) {
        _displayView = displayView;
        switch (displayView) {
            case ASSIGNED:
                _titleTextView.setText("No Assigned Work");
                _captionTexView.setText("Go check out work in the marketplace");
                _actionButton.setText("View Marketplace");
                _actionButton.setVisibility(VISIBLE);
                break;
            case AVAILABLE:
                Profile profile = App.get().getProfile();

                if (profile.getMarketplaceStatusOn()) {
                    _titleTextView.setText("No work available");
                    _captionTexView.setText("Try updating your profile on the web site");
                    //_actionButton.setText("View Marketplace");
                    _actionButton.setVisibility(GONE);
                } else {
                    String reason = profile.getMarketplaceStatusReason();
                    if (misc.isEmptyOrNull(reason)) {
                        _titleTextView.setText("No work available");
                        _captionTexView.setText("Try updating your profile on the web site");
                        _actionButton.setVisibility(GONE);
                    } else if ("KEEP_PRIVATE".equals(reason)) {
                        _titleTextView.setText("Marketplace access");
                        _captionTexView.setText("You do not have Marketplace access");
                        _actionButton.setVisibility(GONE);
                    } else if ("PENDING_VERIFICATION".equals(reason)) {
                        _titleTextView.setText("Marketplace access");
                        _captionTexView.setText("Your account is pending verification. Try again later");
                        //_actionButton.setText("View Marketplace");
                        _actionButton.setVisibility(GONE);
                    } else if ("SUSPENDED".equals(reason)) {
                        _titleTextView.setText("Marketplace suspension");
                        _captionTexView.setText("You are currently suspended");
                        _actionButton.setVisibility(GONE);
                    }
                }
                break;
            case CANCELED:
                _titleTextView.setText("No canceled work");
                _captionTexView.setText("This is a good thing.");
                _actionButton.setVisibility(GONE);
                break;
            case COMPLETED:
                _titleTextView.setText("Completed work");
                _captionTexView.setText("You haven't completed a work order yet");
                _actionButton.setVisibility(GONE);
                break;
            case REQUESTED:
                _titleTextView.setText("Requested work");
                _captionTexView.setText("You haven't requested a work order yet");
                _actionButton.setVisibility(GONE);
                break;
        }
    }

    private final View.OnClickListener _action_OnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MarketActivity.class);
            getContext().startActivity(intent);
        }
    };
}
