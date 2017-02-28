package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncrease;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by Michael on 6/3/2016.
 */
public class RequestNewPayTile extends RelativeLayout {
    private static final String TAG = "RequestNewPayTile";

    // Ui
    private TextView _statusTextView;
    private IconFontTextView _iconTextView;
    private TextView _basisTextView;
    private TextView _captionTextView;
    private TextView _payTextView;

    // Data
    private WorkOrder _workOrder;

    public RequestNewPayTile(Context context) {
        super(context);
        init();
    }

    public RequestNewPayTile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RequestNewPayTile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_request_pay_tile, this);

        if (isInEditMode())
            return;

        _statusTextView = (TextView) findViewById(R.id.requestPayStatus_textview);
        _iconTextView = (IconFontTextView) findViewById(R.id.requestPayIcon_textview);
        _basisTextView = (TextView) findViewById(R.id.requestPayBasis_textview);
        _captionTextView = (TextView) findViewById(R.id.requestPayCaption_textview);
        _payTextView = (TextView) findViewById(R.id.requestPayAmount_textview);

        populateUi();
    }

    public void setData(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        setVisibility(GONE);
        if (_workOrder == null)
            return;

        if (_payTextView == null)
            return;

        if (_workOrder.getPay() == null || _workOrder.getPay().getIncreases() == null)
            return;

        PayIncrease increase = _workOrder.getPay().getIncreases().getLastIncrease();

        if (increase == null || increase.getPay() == null) {
            return;
        }

        Pay pay = increase.getPay();
        switch (pay.getType()) {
            case BLENDED:
                _basisTextView.setText("Blended Rate");
                _captionTextView.setVisibility(VISIBLE);
                _captionTextView.setText(
                        misc.toCurrency(pay.getBase().getAmount()) + " fixed for first " + ((int) (double) pay.getBase().getUnits()) + " hours\n"
                                + misc.toCurrency(pay.getAdditional().getAmount()) + " per hour after for up to " + ((int) (double) pay.getAdditional().getUnits()) + " hours");
                _payTextView.setText(misc.toCurrency(pay.getBase().getAmount() + pay.getAdditional().getAmount() * pay.getAdditional().getUnits()));
                break;
            case DEVICE:
                _basisTextView.setText("Per Device");
                _captionTextView.setVisibility(VISIBLE);
                _captionTextView.setText("For up to " + ((int) (double) pay.getBase().getUnits()) + " devices");
                _payTextView.setText(misc.toCurrency(pay.getBase().getAmount() * pay.getBase().getUnits()));
                break;
            case FIXED:
                _basisTextView.setText("Fixed Rate");
                _captionTextView.setVisibility(GONE);
                _payTextView.setText(misc.toCurrency(pay.getBase().getAmount()));
                break;
            case HOURLY:
                _basisTextView.setText("Hourly Rate");
                _captionTextView.setVisibility(VISIBLE);
                _captionTextView.setText("For up to " + ((int) (double) pay.getBase().getUnits()) + " hours");
                _payTextView.setText(misc.toCurrency(pay.getBase().getAmount() * pay.getBase().getUnits()));
                break;
        }

        switch (increase.getStatus()) {
            case PENDING: // pending
                setVisibility(VISIBLE);
                _statusTextView.setText("PENDING APPROVAL");
                _statusTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));

                _iconTextView.setText(R.string.icon_circle_pending);
                _iconTextView.setTextColor(getResources().getColor(R.color.fn_yellow));
                break;
            case ACCEPTED: // approved
                setVisibility(GONE);
                break;
            case DECLINED: // denied
                setVisibility(VISIBLE);
                _statusTextView.setText("DECLINED");
                _statusTextView.setTextColor(getResources().getColor(R.color.fn_red));

                _iconTextView.setText(R.string.icon_circle_delete);
                _iconTextView.setTextColor(getResources().getColor(R.color.fn_red));
                break;
            default:
                setVisibility(GONE);
                break;
        }
    }
}
