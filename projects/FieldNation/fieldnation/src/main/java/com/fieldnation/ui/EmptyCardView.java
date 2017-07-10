package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.ui.nav.NavActivity;

/**
 * Created by shoaib.ahmed on 6/21/2017.
 */
public class EmptyCardView extends RelativeLayout {
    private static final String TAG = "UnavailableCardView";


    public static final String PARAM_VIEW_TYPE_AVAILABLE = "available";
    public static final String PARAM_VIEW_TYPE_ASSIGNED = "assigned";
    public static final String PARAM_VIEW_TYPE_COMPLETED = "completed";
    public static final String PARAM_VIEW_TYPE_CONFIRM = "confirm";
    public static final String PARAM_VIEW_TYPE_COUNTER = "counter";
    public static final String PARAM_VIEW_TYPE_REQUESTED = "requested";
    public static final String PARAM_VIEW_TYPE_ROUTED = "routed";
    public static final String PARAM_VIEW_TYPE_PAYMENT = "payment";
    public static final String PARAM_VIEW_TYPE_DECLINED = "declined";
    public static final String PARAM_VIEW_TYPE_PENDING = "pending";
    public static final String PARAM_VIEW_TYPE_MESSAGE = "message";
    public static final String PARAM_VIEW_TYPE_NOTIFICATION = "notification";


    // Ui
    private TextView _titleTextView;
    private TextView _captionTexView;
    private Button _actionButton;

    // Data
    private String _viewType;

    public EmptyCardView(Context context) {
        super(context);
        init();
    }

    public EmptyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_empty_card, this);

        if (isInEditMode())
            return;
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _captionTexView = (TextView) findViewById(R.id.caption_textview);
        _actionButton = (Button) findViewById(R.id.action_button);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void setNoAvailableWorkOrder() {
        _titleTextView.setText(R.string.empty_state_available_wol_title);
        _captionTexView.setText(R.string.empty_state_available_wol_body);
        _actionButton.setVisibility(GONE);
    }

    private void setNoAssignedWorkOrder() {
        _titleTextView.setText(R.string.empty_state_assigned_wol_title);
        _captionTexView.setText(R.string.empty_state_assigned_wol_body);
        _actionButton.setVisibility(VISIBLE);
        _actionButton.setOnClickListener(_available_onClick);
    }

    private void setNoCompletedWorkOrder() {
        _titleTextView.setText(R.string.empty_state_completed_wol_title);
        _captionTexView.setText(R.string.empty_state_completed_wol_body);
        _actionButton.setVisibility(VISIBLE);
        _actionButton.setOnClickListener(_available_onClick);
    }

    private void setNoConfirmWorkOrder() {
        _titleTextView.setText(R.string.no_confirm_work);
        _captionTexView.setText(R.string.empty_state_confirm_wol_body);
        _actionButton.setVisibility(GONE);
    }

    private void setNoCounteredWorkOrder() {
        _titleTextView.setText(R.string.empty_state_countered_wol_title);
        _captionTexView.setText(R.string.empty_state_countered_wol_body);
        _actionButton.setVisibility(VISIBLE);
        _actionButton.setOnClickListener(_available_onClick);
    }

    private void setNoRequestedWorkOrder() {
        _titleTextView.setText(R.string.empty_state_requested_wol_title);
        _captionTexView.setText(R.string.empty_state_requested_wol_body);
        _actionButton.setVisibility(VISIBLE);
        _actionButton.setOnClickListener(_available_onClick);
    }

    private void setNoRoutedWorkOrder() {
        _titleTextView.setText(R.string.empty_state_routed_wol_title);
        _captionTexView.setText(R.string.empty_state_routed_wol_body);
        _actionButton.setVisibility(GONE);
    }

    private void setNoPendingWorkOrder() {
        _titleTextView.setText(R.string.empty_state_pending_wol_title);
        _captionTexView.setText(R.string.empty_state_pending_wol_body);
        _actionButton.setVisibility(GONE);
    }

    private void setNoDeclinedWorkOrder() {
        _titleTextView.setText(R.string.empty_state_declined_wol_title);
        _captionTexView.setText(R.string.empty_state_declined_wol_body);
        _actionButton.setVisibility(GONE);
    }

    private void setPaymentEmptyState() {
        _titleTextView.setText(R.string.empty_state_payment_title);
        _captionTexView.setText(R.string.empty_state_payment_body);
        _actionButton.setVisibility(VISIBLE);
        _actionButton.setOnClickListener(_available_onClick);
    }

    private void setMessageEmptyState() {
        _titleTextView.setText(R.string.empty_state_message_title);
        _captionTexView.setText(R.string.empty_state_message_body);
        _actionButton.setVisibility(GONE);
    }

    private void setNotificationEmptyState() {
        _titleTextView.setText(R.string.empty_state_notification_title);
        _captionTexView.setText(R.string.empty_state_notification_body);
        _actionButton.setVisibility(GONE);
    }


    public void setData(String viewType) {
        _viewType = viewType;
        populateUi();
    }

    public void populateUi() {
        if (_viewType == null) {
            return;
        }

        switch (_viewType) {
            case PARAM_VIEW_TYPE_ASSIGNED:
                setNoAssignedWorkOrder();
                break;
            case PARAM_VIEW_TYPE_AVAILABLE:
                setNoAvailableWorkOrder();
                break;

            case PARAM_VIEW_TYPE_ROUTED:
                setNoRoutedWorkOrder();
                break;

            case PARAM_VIEW_TYPE_COMPLETED:
                setNoCompletedWorkOrder();
                break;

            case PARAM_VIEW_TYPE_REQUESTED:
                setNoRequestedWorkOrder();
                break;

            case PARAM_VIEW_TYPE_CONFIRM:
                setNoConfirmWorkOrder();
                break;

            case PARAM_VIEW_TYPE_COUNTER:
                setNoCounteredWorkOrder();
                break;

            case PARAM_VIEW_TYPE_DECLINED:
                setNoDeclinedWorkOrder();
                break;

            case PARAM_VIEW_TYPE_PENDING:
                setNoPendingWorkOrder();
                break;

            case PARAM_VIEW_TYPE_PAYMENT:
                setPaymentEmptyState();
                break;

            case PARAM_VIEW_TYPE_MESSAGE:
                setMessageEmptyState();
                break;

            case PARAM_VIEW_TYPE_NOTIFICATION:
                setNotificationEmptyState();
                break;

        }
    }

    private final OnClickListener _available_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // go to available WOL
            NavActivity.startNew(App.get());
        }
    };

}
