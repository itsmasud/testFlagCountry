package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.IncreaseRequestInfo;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.fntools.misc;

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
    private Workorder _workorder;

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

    public void setData(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        setVisibility(GONE);
        if (_workorder == null)
            return;

        if (_payTextView == null)
            return;

        IncreaseRequestInfo info = _workorder.getIncreaseRequestInfo();

        if (info == null || info.getPay() == null) {
            return;
        }

        Pay pay = info.getPay();
        if (pay.isBlendedRate()) {
            _basisTextView.setText("Blended Rate");
            _captionTextView.setVisibility(VISIBLE);
            _captionTextView.setText(
                    misc.toCurrency(pay.getBlendedStartRate()) + " fixed for first " + ((int) (double) pay.getBlendedFirstHours()) + " hours\n"
                            + misc.toCurrency(pay.getBlendedAdditionalRate()) + " per hour after for up to " + ((int) (double) pay.getBlendedAdditionalHours()) + " hours");
            _payTextView.setText(misc.toCurrency(pay.getBlendedStartRate() + pay.getBlendedAdditionalRate() * pay.getBlendedAdditionalHours()));

        } else if (pay.isFixedRate()) {
            _basisTextView.setText("Fixed Rate");
            _captionTextView.setVisibility(GONE);
            _payTextView.setText(misc.toCurrency(pay.getFixedAmount()));

        } else if (pay.isHourlyRate()) {
            _basisTextView.setText("Hourly Rate");
            _captionTextView.setVisibility(VISIBLE);
            _captionTextView.setText("For up to " + ((int) (double) pay.getMaxHour()) + " hours");
            _payTextView.setText(misc.toCurrency(pay.getPerHour() * pay.getMaxHour()));

        } else if (pay.isPerDeviceRate()) {
            _basisTextView.setText("Per Device");
            _captionTextView.setVisibility(VISIBLE);
            _captionTextView.setText("For up to " + ((int) (double) pay.getMaxDevice()) + " devices");
            _payTextView.setText(misc.toCurrency(pay.getPerDevice() * pay.getMaxDevice()));
        }

        switch (info.getStatus()) {
            case 0: // pending
                setVisibility(VISIBLE);
                _statusTextView.setText("PENDING APPROVAL");
                _statusTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));

                _iconTextView.setText(R.string.icon_circle_pending);
                _iconTextView.setTextColor(getResources().getColor(R.color.fn_yellow));
                break;
            case 1: // approved
                setVisibility(GONE);
                break;
            case 2: // denied
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
