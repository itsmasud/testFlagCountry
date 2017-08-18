package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WodBottomSheetView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "WodBottomSheetView";

    // Ui
    private View _bottomSheetBackground;
    private LinearLayout _bottomSheet;
    private TextView _addCounterOfferButton;
    private TextView _addRequestNewPayButton;
    private TextView _addTimeLogButton;
    private TextView _addExpenseButton;
    private TextView _addDiscountButton;
    private TextView _addSignatureButton;
    private TextView _addShipmentButton;
    private TextView _addAttachmentButton;

    // Animations
    private Animation _fadeIn;
    private Animation _fadeOut;
    private Animation _bsSlideIn;
    private Animation _bsSlideOut;
//    private Animation _fabSlideOut;
//    private Animation _fabSlideIn;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
    private ForLoopRunnable _forLoop;

    public WodBottomSheetView(Context context) {
        super(context);
        init();
    }

    public WodBottomSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WodBottomSheetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Log.e(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_bottomsheet, this);

        if (isInEditMode())
            return;

        _bottomSheetBackground = findViewById(R.id.bottomSheet_background);
        _bottomSheetBackground.setOnClickListener(_bottomSheet_onCancel);
        _bottomSheet = findViewById(R.id.bottomsheet);

        _addCounterOfferButton = findViewById(R.id.addCounterOffer_button);
        _addCounterOfferButton.setOnClickListener(_addCounterOffer_onClick);
        _addRequestNewPayButton = findViewById(R.id.addRequestNewPay_button);
        _addRequestNewPayButton.setOnClickListener(_addRequestNewPay_onClick);
        _addTimeLogButton = findViewById(R.id.addTimeLog_button);
        _addTimeLogButton.setOnClickListener(_addTimeLog_onClick);
        _addExpenseButton = findViewById(R.id.addExpense_button);
        _addExpenseButton.setOnClickListener(_addExpense_onClick);
        _addDiscountButton = findViewById(R.id.addDiscount_button);
        _addDiscountButton.setOnClickListener(_addDiscount_onClick);
        _addSignatureButton = findViewById(R.id.addSignature_button);
        _addSignatureButton.setOnClickListener(_addSignature_onClick);
        _addShipmentButton = findViewById(R.id.addShipment_button);
        _addShipmentButton.setOnClickListener(_addShipment_onClick);
        _addAttachmentButton = findViewById(R.id.addAttachment_button);
        _addAttachmentButton.setOnClickListener(_addAttachment_onClick);

        _fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        _fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        _bsSlideIn = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_in_bottom);
        _bsSlideOut = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_out_bottom);

        _fadeIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _bottomSheetBackground.setVisibility(View.VISIBLE);
            }
        });

        _fadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _bottomSheetBackground.setVisibility(View.GONE);
            }
        });

        _bsSlideIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _bottomSheet.setVisibility(View.VISIBLE);
            }
        });

        _bsSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _bottomSheet.setVisibility(View.GONE);
            }
        });

        _bottomSheetBackground.clearAnimation();
        _bottomSheetBackground.startAnimation(_fadeIn);
        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideIn);
    }

    public void setListener(Listener listener) {
        Log.e(TAG, "setListener");
        _listener = listener;
    }

    public void animateIn() {
        _bottomSheetBackground.setVisibility(VISIBLE);
        _bottomSheetBackground.clearAnimation();
        _bottomSheetBackground.startAnimation(_fadeIn);
        _bottomSheet.setVisibility(VISIBLE);
        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideIn);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        Log.e(TAG, "setWorkOrder");
        _workOrder = workOrder;
        populateUi();
    }

    public void populateUi() {
        Log.e(TAG, "populateUi");
        if (_workOrder == null)
            return;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _bottomSheet_onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _bottomSheetBackground.clearAnimation();
            _bottomSheetBackground.startAnimation(_fadeOut);
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.onBackgroundClick();
        }
    };

    private final View.OnClickListener _addCounterOffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addCounterOffer();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addRequestNewPay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addRequestNewPay();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addTimeLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addTimeLog();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addExpense();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addDiscount();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addSignature_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addSignature();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addShipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addShipment();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    private final View.OnClickListener _addAttachment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (_listener != null) _listener.addAttachment();

            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _bottomSheet_onCancel.onClick(view);
                }
            }, 500);
        }
    };

    public interface Listener {
        void addCounterOffer();

        void addRequestNewPay();

        void addTimeLog();

        void addExpense();

        void addDiscount();

        void addSignature();

        void addShipment();

        void addAttachment();

        void onBackgroundClick();
    }
}
