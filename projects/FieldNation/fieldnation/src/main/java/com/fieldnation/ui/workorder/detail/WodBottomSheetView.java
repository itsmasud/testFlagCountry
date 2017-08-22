package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.ForLoopRunnable;
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
    private View _bottomSheet;
    private View _counterOfferLayout;
    private View _requestNewPayLayout;
    private View _timeLogLayout;
    private View _expenseLayout;
    private View _discountLayout;
    private View _signatureLayout;
    private View _shipmentLayout;
    private View _attachmentLayout;

    // Animations
    private Animation _bsSlideIn;
    private Animation _bsSlideOut;

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

        _bottomSheet = findViewById(R.id.bottomsheet);

        _counterOfferLayout = findViewById(R.id.counterOffer_layout);
        _counterOfferLayout.setOnClickListener(_addCounterOffer_onClick);
        _requestNewPayLayout = findViewById(R.id.requestNewPay_layout);
        _requestNewPayLayout.setOnClickListener(_addRequestNewPay_onClick);
        _timeLogLayout = findViewById(R.id.timeLog_layout);
        _timeLogLayout.setOnClickListener(_addTimeLog_onClick);
        _expenseLayout = findViewById(R.id.expense_layout);
        _expenseLayout.setOnClickListener(_addExpense_onClick);
        _discountLayout = findViewById(R.id.discount_layout);
        _discountLayout.setOnClickListener(_addDiscount_onClick);
        _signatureLayout = findViewById(R.id.signature_layout);
        _signatureLayout.setOnClickListener(_addSignature_onClick);
        _shipmentLayout = findViewById(R.id.shipment_layout);
        _shipmentLayout.setOnClickListener(_addShipment_onClick);
        _attachmentLayout = findViewById(R.id.attachment_layout);
        _attachmentLayout.setOnClickListener(_addAttachment_onClick);

        _bsSlideIn = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_in_bottom);
        _bsSlideOut = AnimationUtils.loadAnimation(getContext(), R.anim.fg_slide_out_bottom);


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

        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideIn);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void animateIn() {
        _bottomSheet.setVisibility(VISIBLE);
        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideIn);
    }

    public void animateOut() {
        _bottomSheet.setVisibility(VISIBLE);
        _bottomSheet.clearAnimation();
        _bottomSheet.startAnimation(_bsSlideOut);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void populateUi() {
        if (_workOrder == null)
            return;


        if (_workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER))
            _counterOfferLayout.setVisibility(VISIBLE);
        else _counterOfferLayout.setVisibility(GONE);

        if (_workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD))
            _requestNewPayLayout.setVisibility(VISIBLE);
        else _requestNewPayLayout.setVisibility(GONE);

        // no action for countered wo

        if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD))
            _timeLogLayout.setVisibility(VISIBLE);
        else _timeLogLayout.setVisibility(GONE);

        if (_workOrder.getPay().getExpenses().getActionsSet().contains(Expenses.ActionsEnum.ADD))
            _expenseLayout.setVisibility(VISIBLE);
        else _expenseLayout.setVisibility(GONE);

        if (_workOrder.getPay().getDiscounts().getActionsSet().contains(PayModifiers.ActionsEnum.ADD))
            _discountLayout.setVisibility(VISIBLE);
        else _discountLayout.setVisibility(GONE);

        if (_workOrder.getSignatures().getActionsSet().contains(Signatures.ActionsEnum.ADD))
            _signatureLayout.setVisibility(VISIBLE);
        else _signatureLayout.setVisibility(GONE);

        if (_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD))
            _shipmentLayout.setVisibility(VISIBLE);
        else _shipmentLayout.setVisibility(GONE);

        if (_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD))
            _shipmentLayout.setVisibility(VISIBLE);
        else _shipmentLayout.setVisibility(GONE);


        final AttachmentFolder[] folders = _workOrder.getAttachments().getResults();
        for (AttachmentFolder attachmentFolder : folders) {
            if (attachmentFolder.getResults().length > 0
                    && (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT))) {
                _attachmentLayout.setVisibility(VISIBLE);
                break;
            }
            _attachmentLayout.setVisibility(GONE);
        }
    }


    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/

    private final View.OnClickListener _addCounterOffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addCounterOffer();
        }
    };

    private final View.OnClickListener _addRequestNewPay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addRequestNewPay();
        }
    };

    private final View.OnClickListener _addTimeLog_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addTimeLog();
        }
    };

    private final View.OnClickListener _addExpense_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addExpense();
        }
    };

    private final View.OnClickListener _addDiscount_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addDiscount();
        }
    };

    private final View.OnClickListener _addSignature_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addSignature();
        }
    };

    private final View.OnClickListener _addShipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
            if (_listener != null) _listener.addShipment();
        }
    };

    private final View.OnClickListener _addAttachment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            _bottomSheet.clearAnimation();
            _bottomSheet.startAnimation(_bsSlideOut);
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
