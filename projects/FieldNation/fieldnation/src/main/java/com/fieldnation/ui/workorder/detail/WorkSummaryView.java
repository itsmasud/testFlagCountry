package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WorkSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "WorkSummaryView";

    // UI
    private TextView _bundleWarningTextView;
    private View _bundleWarningLayout;

    private LinearLayout _descriptionContainer;
    private WebView _descriptionWebView;
    private RelativeLayout _descriptionShortLayout;
    private TextView _descriptionShortTextView;

    private TextView _confidentialTextView;
    private TextView _policiesTextView;
    private TextView _standardInstructionTextView;

    private Button _readMoreButton;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
    private Boolean _isEllipsis = null;

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

        _descriptionShortLayout = findViewById(R.id.descriptionShort_layout);
        _descriptionShortTextView = findViewById(R.id.descriptionShort_textview);
        _descriptionShortTextView.setOnLongClickListener(_editMode_listener);

        _confidentialTextView = findViewById(R.id.confidential_textview);
        _confidentialTextView.setOnClickListener(_confidential_onClick);

        _policiesTextView = findViewById(R.id.policies_textview);
        _policiesTextView.setOnClickListener(_policies_onClick);

        _standardInstructionTextView = findViewById(R.id.standardInstructions_textview);
        _standardInstructionTextView.setOnClickListener(_standardInstructions_onClick);

        _bundleWarningTextView = findViewById(R.id.bundlewarning_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);
        _bundleWarningLayout = findViewById(R.id.bundlewarning_layout);

        _readMoreButton = findViewById(R.id.readMore_button);
        _readMoreButton.setOnClickListener(_readMore_onClick);

        setVisibility(View.GONE);
    }


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

        if (misc.isEmptyOrNull(_workOrder.getDescription().getHtml())) {
            _descriptionContainer.setVisibility(GONE);
        } else {
            _descriptionContainer.setVisibility(VISIBLE);
            int fontSize = getResources().getInteger(R.integer.textSizeWorkorderDescription);
            WebSettings _webSettings = _descriptionWebView.getSettings();
            _webSettings.setDefaultFontSize(fontSize);

            _descriptionWebView.loadData(_workOrder.getDescription().getHtml(), "text/html", "utf-8");
            _descriptionShortTextView.setText(misc.linkifyHtml(_workOrder.getDescription().getHtml().trim(), Linkify.ALL));
            _descriptionShortTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

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
            _standardInstructionTextView.setVisibility(GONE);
        } else {
            _standardInstructionTextView.setVisibility(VISIBLE);
        }

    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _readMore_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_descriptionShortLayout.getVisibility() == VISIBLE) {
                _descriptionWebView.setVisibility(View.VISIBLE);
                _descriptionShortLayout.setVisibility(View.GONE);
                _readMoreButton.setText(R.string.btn_read_less);
            } else {
                _descriptionWebView.setVisibility(View.GONE);
                _descriptionShortLayout.setVisibility(View.VISIBLE);
                _readMoreButton.setText(R.string.btn_read_more);
            }
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

    private final OnLongClickListener _editMode_listener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_descriptionShortLayout.getVisibility() == VISIBLE) {
                _descriptionWebView.setVisibility(View.VISIBLE);
                _descriptionShortLayout.setVisibility(View.GONE);
                _readMoreButton.setText(R.string.btn_read_less);
            }
            return true;
        }
    };

    public interface Listener {
        void showConfidentialInfo(String body);

        void showCustomerPolicies(String body);

        void showStandardInstructions(String body);
    }
}