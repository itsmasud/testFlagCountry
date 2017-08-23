package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.Expenses;
import com.fieldnation.v2.data.model.PayIncreases;
import com.fieldnation.v2.data.model.PayModifiers;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.Signatures;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WodBottomSheetView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "WodBottomSheetView";

    // Ui
//    private View _bottomSheetBackground;
    private View _background;
    private View _sheet;
    private View _sheetContainer;

    private View _addCounterOfferButton;
    private View _addRequestNewPayButton;
    private View _addTimeLogButton;
    private View _addExpenseButton;
    private View _addDiscountButton;
    private View _addSignatureButton;
    private View _addShipmentButton;
    private View _addAttachmentButton;

    private Button _fab;

    // Animations
    private Animation _bsSlideIn;
    private Animation _bsSlideOut;
    private Animation _fadeIn;
    private Animation _fadeOut;
    private Animation _fabSlideOut;
    private Animation _fabSlideIn;

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
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_bottomsheet, this);

        if (isInEditMode())
            return;
        _background = findViewById(R.id.background);
        _background.setOnClickListener(_bottomSheet_onCancel);
        _sheet = findViewById(R.id.sheet);
        _sheetContainer = findViewById(R.id.sheet_container);

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

        _fab = findViewById(R.id.fab_button);
        _fab.setOnClickListener(_fab_onClick);

        _bsSlideIn = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_in_bottom);
        _bsSlideOut = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_out_bottom);
        _fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        _fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        _fabSlideIn = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_in_right);
        _fabSlideOut = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_out_right);


        _bsSlideIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _sheet.setVisibility(View.VISIBLE);
                _sheetContainer.setVisibility(VISIBLE);
            }
        });
        _bsSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _sheet.setVisibility(View.GONE);
                _sheetContainer.setVisibility(GONE);
            }
        });
        _fadeIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _background.setVisibility(View.VISIBLE);
            }
        });
        _fadeOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _background.setVisibility(View.GONE);
            }
        });
        _fabSlideIn.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _fab.setVisibility(View.VISIBLE);
            }
        });
        _fabSlideOut.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                _fab.setVisibility(View.GONE);
            }
        });


        _sheet.clearAnimation();
        _sheet.startAnimation(_bsSlideIn);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void animateIn() {
        _sheetContainer.setVisibility(VISIBLE);
        _sheet.setVisibility(VISIBLE);
        _sheet.clearAnimation();
        _sheet.startAnimation(_bsSlideIn);

        _fab.setVisibility(VISIBLE);
        _fab.clearAnimation();
        _fab.startAnimation(_fabSlideOut);

        _background.setVisibility(View.VISIBLE);
        _background.clearAnimation();
        _background.startAnimation(_fadeIn);
    }

    public void animateOut() {
        _sheetContainer.setVisibility(VISIBLE);
        _sheet.setVisibility(VISIBLE);
        _sheet.clearAnimation();
        _sheet.startAnimation(_bsSlideOut);

        _background.setVisibility(View.VISIBLE);
        _background.clearAnimation();
        _background.startAnimation(_fadeOut);

        _fab.setVisibility(VISIBLE);
        _fab.clearAnimation();
        _fab.startAnimation(_fabSlideIn);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public boolean onBackPressed() {
        if (_sheet.getVisibility() == VISIBLE) {
            animateOut();
            return true;
        }
        return false;
    }

    public void populateUi() {
        if (_workOrder == null)
            return;

        if (_workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER))
            _addCounterOfferButton.setVisibility(VISIBLE);
        else _addCounterOfferButton.setVisibility(GONE);

        if (_workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD))
            _addRequestNewPayButton.setVisibility(VISIBLE);
        else _addRequestNewPayButton.setVisibility(GONE);

        if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD))
            _addTimeLogButton.setVisibility(VISIBLE);
        else _addTimeLogButton.setVisibility(GONE);

        if (_workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD))
            _addExpenseButton.setVisibility(VISIBLE);
        else _addExpenseButton.setVisibility(GONE);

        if (_workOrder.getPay().getDiscounts().getActionsSet().contains(PayModifiers.ActionsEnum.ADD))
            _addDiscountButton.setVisibility(VISIBLE);
        else _addDiscountButton.setVisibility(GONE);

        if (_workOrder.getSignatures().getActionsSet().contains(Signatures.ActionsEnum.ADD))
            _addSignatureButton.setVisibility(VISIBLE);
        else _addSignatureButton.setVisibility(GONE);

        if (_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD))
            _addShipmentButton.setVisibility(VISIBLE);
        else _addShipmentButton.setVisibility(GONE);

        final AttachmentFolder[] folders = _workOrder.getAttachments().getResults();
        for (AttachmentFolder attachmentFolder : folders) {
            if (attachmentFolder.getResults().length > 0
                    && (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT))) {
                _addAttachmentButton.setVisibility(VISIBLE);
                break;
            }
            _addAttachmentButton.setVisibility(GONE);
        }

        if (shouldFabVisible())
            _fab.setVisibility(View.VISIBLE);
        else _fab.setVisibility(View.GONE);
    }

    private boolean shouldFabVisible() {
        if (_workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER))
            return true;
        else if (_workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD))
            return true;
        else if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD))
            return true;
        else if (_workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD))
            return true;
        else if (_workOrder.getPay().getDiscounts().getActionsSet().contains(PayModifiers.ActionsEnum.ADD))
            return true;
        else if (_workOrder.getSignatures().getActionsSet().contains(Signatures.ActionsEnum.ADD))
            return true;
        else if (_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD))
            return true;
        else {
            final AttachmentFolder[] folders = _workOrder.getAttachments().getResults();
            for (AttachmentFolder attachmentFolder : folders) {
                if (attachmentFolder.getResults().length > 0
                        && (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                        || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT))) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _fab_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            misc.hideKeyboard(v);
            animateIn();
        }
    };

    private final View.OnClickListener _bottomSheet_onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            animateOut();
        }
    };

    private final View.OnClickListener _addCounterOffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addCounterOffer();
        }
    };

    private final View.OnClickListener _addRequestNewPay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addRequestNewPay();
        }
    };

    private final View.OnClickListener _addTimeLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addTimeLog();
        }
    };

    private final View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addExpense();
        }
    };

    private final View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addDiscount();
        }
    };

    private final View.OnClickListener _addSignature_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addSignature();
        }
    };

    private final View.OnClickListener _addShipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addShipment();
        }
    };

    private final View.OnClickListener _addAttachment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            animateOut();
            if (_listener != null) _listener.addAttachment();
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
    }
}
