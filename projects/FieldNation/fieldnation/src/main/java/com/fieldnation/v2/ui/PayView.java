package com.fieldnation.v2.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;

/**
 * Created by mc on 7/11/17.
 */

public class PayView extends LinearLayout {
    private static final String TAG = "PayView";

    // Ui
    private KeyValuePairView _pay1View;
    private KeyValuePairView _pay2View;
    private KeyValuePairView _pay3View;
    private KeyValuePairView _pay4View;
    private KeyValuePairView _pay5View;

    // Data
    private Pay _pay;

    // Listeners
    private OnRenderListener _onRenderListener;

    public PayView(Context context) {
        super(context);
        init();
    }

    public PayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_pay, this);

        if (isInEditMode())
            return;

        _pay1View = findViewById(R.id.pay1_view);
        _pay2View = findViewById(R.id.pay2_view);
        _pay3View = findViewById(R.id.pay3_view);
        _pay4View = findViewById(R.id.pay4_view);
        _pay5View = findViewById(R.id.pay5_view);

        populateUi();
    }

    public void set(Pay pay) {
        _pay = pay;
        populateUi();
    }

    private void populateUi() {
        if (_pay1View == null)
            return;

        if (_pay == null)
            return;

        try {
            switch (_pay.getType()) {
                case FIXED:
                    _pay1View.set("Type", "Fixed");
                    _pay2View.set("Amount", misc.toCurrency(_pay.getBase().getAmount()));

                    _pay3View.setVisibility(GONE);
                    _pay4View.setVisibility(GONE);
                    _pay5View.setVisibility(GONE);
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                case BLENDED:
                    _pay1View.set("Type", "Blended");
                    _pay2View.set("Fixed Pay Amount", misc.toCurrency(_pay.getBase().getAmount()));
                    _pay3View.set("For How Many Hours", misc.to2Decimal(_pay.getBase().getUnits()));
                    _pay4View.set("Hourly Amount After", misc.toCurrency(_pay.getAdditional().getAmount()));
                    _pay5View.set("For Up To How Many Hours", misc.to2Decimal(_pay.getAdditional().getUnits()));

                    _pay3View.setVisibility(VISIBLE);
                    _pay4View.setVisibility(VISIBLE);
                    _pay5View.setVisibility(VISIBLE);

                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                case DEVICE:
                    _pay1View.set("Type", "Per Device");
                    _pay2View.set("Amount Per Device", misc.toCurrency(_pay.getBase().getAmount()));
                    _pay3View.set("Max Devices", misc.to2Decimal(_pay.getBase().getUnits()));

                    _pay3View.setVisibility(VISIBLE);
                    _pay4View.setVisibility(GONE);
                    _pay5View.setVisibility(GONE);
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                case HOURLY:
                    _pay1View.set("Type", "Hourly");
                    _pay2View.set("Amount Per Hour", misc.toCurrency(_pay.getBase().getAmount()));
                    _pay3View.set("Max Hours", misc.to2Decimal(_pay.getBase().getUnits()));

                    _pay3View.setVisibility(VISIBLE);
                    _pay4View.setVisibility(GONE);
                    _pay5View.setVisibility(GONE);
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                default:
                    if (_onRenderListener != null) _onRenderListener.onRender(false);
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            if (_onRenderListener != null) _onRenderListener.onRender(false);
        }
    }

    public void setOnRenderListener(OnRenderListener onRenderListener) {
        _onRenderListener = onRenderListener;
    }

    public void removeOnRenderListener() {
        _onRenderListener = null;
    }

    public interface OnRenderListener {
        void onRender(boolean success);
    }
}
