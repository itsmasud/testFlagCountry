package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WorkSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "WorkSummaryView";

    private static final int DESCRIPTION_HEIGHT = 250;

    // UI
    private TextView _bundleWarningTextView;
    private View _bundleWarningLayout;

    private View _descriptionContainer;
    private WebView _descriptionWebView;

    private TextView _confidentialTextView;
    private TextView _policiesTextView;
    private View _divider;

    private IconFontTextView _readMoreButton;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
    private Boolean _isEllipsis = null;
    private boolean _collapsed = true;

    // Animations
    private Animation _ccw;
    private Animation _cw;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public WorkSummaryView(Context context) {
        super(context);
        init();
    }

    public WorkSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_sum, this);

        if (isInEditMode())
            return;

        _descriptionContainer = findViewById(R.id.description_container);

        _descriptionWebView = findViewById(R.id.description_webview);
        getViewTreeObserver().addOnGlobalLayoutListener(_globalListener);

        _confidentialTextView = findViewById(R.id.confidential_textview);
        _confidentialTextView.setOnClickListener(_confidential_onClick);

        _policiesTextView = findViewById(R.id.policies_textview);
        _policiesTextView.setOnClickListener(_policies_onClick);


        _divider = findViewById(R.id.link_divider);

        _bundleWarningTextView = findViewById(R.id.bundlewarning_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);
        _bundleWarningLayout = findViewById(R.id.bundlewarning_layout);

        _readMoreButton = findViewById(R.id.readMore_button);
        _readMoreButton.setOnClickListener(_readMore_onClick);

        _ccw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_cw);

        setVisibility(View.GONE);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener _globalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) _descriptionWebView.getLayoutParams();

            // when wrap_content
            if (_collapsed) {
                if (layoutParams.height == LayoutParams.WRAP_CONTENT) {
                    if (_descriptionWebView.getHeight() < DESCRIPTION_HEIGHT) {
                        _readMoreButton.setVisibility(GONE);
                    } else {
                        _readMoreButton.setVisibility(VISIBLE);
                        layoutParams.height = DESCRIPTION_HEIGHT;
                        _descriptionWebView.setLayoutParams(layoutParams);
                        post(new Runnable() {
                            @Override
                            public void run() {
                                _readMoreButton.startAnimation(_ccw);
                            }
                        });
                    }
                }
            } else {
                if (layoutParams.height != LayoutParams.WRAP_CONTENT) {
                    layoutParams.height = LayoutParams.WRAP_CONTENT;
                    _descriptionWebView.setLayoutParams(layoutParams);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            _readMoreButton.startAnimation(_cw);
                        }
                    });
                }
            }
        }
    };

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        refresh();
    }

    private void refresh() {
        setVisibility(View.VISIBLE);

        if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
            _bundleWarningTextView.setVisibility(View.VISIBLE);
            _bundleWarningLayout.setVisibility(VISIBLE);
        } else {
            _bundleWarningTextView.setVisibility(View.GONE);
            _bundleWarningLayout.setVisibility(GONE);
        }

        _descriptionContainer.setVisibility(VISIBLE);
        int fontSize = getResources().getInteger(R.integer.textSizeWorkorderDescription);
        WebSettings _webSettings = _descriptionWebView.getSettings();
        _webSettings.setDefaultFontSize(fontSize);
        _descriptionWebView.loadData(_workOrder.getDescription().getHtml(), "text/html", "utf-8");

        if (misc.isEmptyOrNull(_workOrder.getPolicyAndProcedures().getHtml())) {
            _policiesTextView.setVisibility(View.GONE);
        } else {
            _policiesTextView.setVisibility(View.VISIBLE);
        }

        if (misc.isEmptyOrNull(_workOrder.getConfidential().getHtml())) {
            _confidentialTextView.setVisibility(View.GONE);
        } else {
            _confidentialTextView.setVisibility(View.VISIBLE);
        }

        if (misc.isEmptyOrNull(_workOrder.getStandardInstructions().getHtml())) {
//            _standardInstructionTextView.setVisibility(GONE);
        } else {
//            _standardInstructionTextView.setVisibility(VISIBLE);
        }

/*
        if (_standardInstructionTextView.getVisibility() == GONE
                && _confidentialTextView.getVisibility() == GONE
                && _policiesTextView.getVisibility() == GONE) {
            _divider.setVisibility(GONE);
        } else {
            _divider.setVisibility(VISIBLE);
        }
*/
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _readMore_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _collapsed = !_collapsed;
            _descriptionWebView.requestLayout();
        }
    };

    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BundleDetailActivity.startNew(App.get(), _workOrder.getBundle().getId());
        }
    };

    private final View.OnClickListener _confidential_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showConfidentialInfo(_workOrder.getConfidential().getHtml());
        }
    };

    private final View.OnClickListener _policies_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showCustomerPolicies(_workOrder.getPolicyAndProcedures().getHtml());
        }
    };

    private final OnClickListener _standardInstructions_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showStandardInstructions(_workOrder.getStandardInstructions().getHtml());
        }
    };

    public interface Listener {
        void showConfidentialInfo(String body);

        void showCustomerPolicies(String body);

        void showStandardInstructions(String body);
    }
}