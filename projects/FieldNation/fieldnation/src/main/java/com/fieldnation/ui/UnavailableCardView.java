package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
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
    }

    public void setData(WorkorderDataSelector displayView) {
        switch (displayView) {
            case ASSIGNED:
                _titleTextView.setText(R.string.no_assigned_work);
                _captionTexView.setText(R.string.check_our_marketplace_for_new_work);
                _actionButton.setText(R.string.btn_view_marketplace);
                _actionButton.setVisibility(VISIBLE);
                _actionButton.setOnClickListener(_viewMarketplace_onClick);
                break;
            case AVAILABLE:
                Profile profile = App.get().getProfile();

                if (profile.getMarketplaceStatusOn()) {
                    _titleTextView.setText(R.string.no_available_work);
                    _captionTexView.setText(R.string.try_adding_to_your_profile);
                    _actionButton.setText(R.string.btn_edit_profile);
                    _actionButton.setVisibility(VISIBLE);
                    _actionButton.setOnClickListener(_editProfile_onClick);
                } else {
                    String reason = profile.getMarketplaceStatusReason();
                    if (misc.isEmptyOrNull(reason)) {
                        _titleTextView.setText(R.string.no_available_work);
                        _captionTexView.setText(R.string.try_adding_to_your_profile);
                        _actionButton.setText(R.string.btn_edit_profile);
                        _actionButton.setVisibility(VISIBLE);
                        _actionButton.setOnClickListener(_editProfile_onClick);
                    } else if ("KEEP_PRIVATE".equals(reason)) {
                        _titleTextView.setText(R.string.marketplace_access);
                        _captionTexView.setText(R.string.looks_like_you_need_to_finish_setup);
                        _actionButton.setText(R.string.btn_setup_account);
                        _actionButton.setVisibility(VISIBLE);
                        _actionButton.setOnClickListener(_setupAccount_onClick);
                    } else if ("PENDING_VERIFICATION".equals(reason)) {
                        _titleTextView.setText(R.string.marketplace_access);
                        _captionTexView.setText(R.string.your_account_is_pending_verification);
                        _actionButton.setText(R.string.btn_contact_support);
                        _actionButton.setVisibility(VISIBLE);
                        _actionButton.setOnClickListener(_contactSupport_onClick);
                    } else if ("SUSPENDED".equals(reason)) {
                        _titleTextView.setText(R.string.marketplace_suspension);
                        _captionTexView.setText(R.string.you_are_currently_suspended);
                        _actionButton.setText(R.string.btn_contact_support);
                        _actionButton.setVisibility(VISIBLE);
                        _actionButton.setOnClickListener(_contactSupport_onClick);
                    }
                }
                break;
            case CANCELED:
                _titleTextView.setText(R.string.no_canceled_work);
                _captionTexView.setText(R.string.nothing_to_worry_about);
                _actionButton.setVisibility(GONE);
                break;
            case COMPLETED:
                _titleTextView.setText(R.string.completed_work);
                _captionTexView.setText(R.string.you_havent_completed_work_yet);
                _actionButton.setVisibility(GONE);
                break;
            case REQUESTED:
                _titleTextView.setText(R.string.requested_work);
                _captionTexView.setText(R.string.you_currently_have_no_requested_work_orders);
                _actionButton.setVisibility(GONE);
                break;
        }
    }

    private final View.OnClickListener _viewMarketplace_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // go to marketplace
            Intent intent = new Intent(getContext(), MarketActivity.class);
            getContext().startActivity(intent);
        }
    };

    private final View.OnClickListener _contactSupport_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            GlobalTopicClient.showHelpDialog(getContext());
        }
    };

    private final View.OnClickListener _setupAccount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LeavingActivity.start(getContext(), R.string.edit_your_profile, R.string.currently_to_edit_your, Uri.parse("https://app.fieldnation.com/"));
        }
    };

    private final View.OnClickListener _editProfile_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LeavingActivity.start(getContext(), R.string.setup_your_account, R.string.currently_to_setup_your, Uri.parse("https://app.fieldnation.com/"));
        }
    };
}
