package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomDisplayFields;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.utils.misc;

public class SummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.SummaryView";

    // UI
    private WoProgressBar _progress;
    private TextView _projectNameTextView;
    private TextView _workorderIdTextView;
    private TextView _customDisplayFieldsTextView;
    private TextView _worktypeTextView;
    private TextView _companyTextView;
    private TextView _descriptionTextView;
    private TextView _confidentialTextView;
    private TextView _policiesTextView;
    private LinearLayout _contentLayout;
    private RelativeLayout _loadingLayout;
    private TextView _bundleWarningTextView;

    // Data
    private Listener _listener;
    private Workorder _workorder;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public SummaryView(Context context) {
        super(context);
        init();
    }

    public SummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_sum, this);

        if (isInEditMode())
            return;

        _contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        _progress = (WoProgressBar) findViewById(R.id.substatus_progressbar);
        _projectNameTextView = (TextView) findViewById(R.id.projectname_textview);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);
        _customDisplayFieldsTextView = (TextView) findViewById(R.id.customdisplayfields_textview);
        _worktypeTextView = (TextView) findViewById(R.id.worktype_textview);
        _companyTextView = (TextView) findViewById(R.id.company_textview);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);

        _confidentialTextView = (TextView) findViewById(R.id.confidential_textview);
        _confidentialTextView.setOnClickListener(_confidential_onClick);

        _policiesTextView = (TextView) findViewById(R.id.policies_textview);
        _policiesTextView.setOnClickListener(_policies_onClick);

        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

        _bundleWarningTextView = (TextView) findViewById(R.id.bundlewarning_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);

        setVisibility(View.GONE);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        _workorder.addListener(_workorder_listener);
        refresh();
    }

    private void setLoading(boolean isLoading) {
        // if (isLoading) {
        // _loadingLayout.setVisibility(View.VISIBLE);
        // _contentLayout.setVisibility(View.GONE);
        // } else {
        // _loadingLayout.setVisibility(View.GONE);
        // _contentLayout.setVisibility(View.VISIBLE);
        // }
    }

    private void refresh() {
        setLoading(false);
        setVisibility(View.VISIBLE);

        _progress.setSubstatus(_workorder.getStatus().getWorkorderSubstatus());
        _projectNameTextView.setText(_workorder.getTitle());

        _workorderIdTextView.setText("Work order Id: " + _workorder.getWorkorderId());

        if (_workorder.getCustomDisplayFields() != null && _workorder.getCustomDisplayFields().length > 0) {
            StringBuilder sb = new StringBuilder();

            CustomDisplayFields[] cdfs = _workorder.getCustomDisplayFields();
            for (int i = 0; i < cdfs.length; i++) {
                CustomDisplayFields cdf = cdfs[i];
                sb.append(cdf.getLabel()).append(": ").append(cdf.getValue()).append("\n");
            }

            _customDisplayFieldsTextView.setText(sb.toString().trim());
        }

        if (misc.isEmptyOrNull(_workorder.getCompanyName()))
            _companyTextView.setVisibility(GONE);
        else {
            _companyTextView.setVisibility(VISIBLE);
            _companyTextView.setText(_workorder.getCompanyName());
        }

        _descriptionTextView.setText(misc.linkifyHtml(_workorder.getFullWorkDescription(), Linkify.ALL));
        _descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());

        _worktypeTextView.setText(_workorder.getTypeOfWork());

        if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
            _bundleWarningTextView.setVisibility(View.VISIBLE);
            _bundleWarningTextView.setText(String.format(getContext().getString(R.string.workorder_bundle_warning), _workorder.getBundleCount()));
        } else {
            _bundleWarningTextView.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(_workorder.getCustomerPoliciesProcedures())) {
            _policiesTextView.setVisibility(View.VISIBLE);
            //_policiesTextView.setText(_workorder.getCustomerPoliciesProcedures());
        } else {
            _policiesTextView.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(_workorder.getConfidentialInformation())) {
            _confidentialTextView.setVisibility(View.VISIBLE);
            //_confidentialTextView.setText(_workorder.getConfidentialInformation());
        } else {
            _confidentialTextView.setVisibility(View.GONE);
        }

        if (!_workorder.canViewConfidentialInfo()) {
            _policiesTextView.setVisibility(View.GONE);
            _confidentialTextView.setVisibility(View.GONE);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getContext().startActivity(intent);
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            setLoading(true);
        }
    };

    private View.OnClickListener _confidential_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showConfidentialInfo(_workorder.getConfidentialInformation());
        }
    };

    private View.OnClickListener _policies_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showCustomerPolicies(_workorder.getCustomerPoliciesProcedures());
        }
    };


    public interface Listener {
        public void showConfidentialInfo(String body);

        public void showCustomerPolicies(String body);
    }
}