package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomDisplayFields;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.utils.misc;

public class WorkSummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "WorkSummaryView";

    // UI
    private TextView _projectNameTextView;
    private TextView _workorderIdTextView;
    private TextView _customDisplayFieldsTextView;
    private TextView _worktypeTextView;

    private TextView _bundleWarningTextView;

    private LinearLayout _descriptionContainer;
    private TextView _descriptionTextView;
    private EditText _descriptionEditText;
    private RelativeLayout _descriptionShortLayout;
    private TextView _descriptionShortTextView;

    private TextView _confidentialTextView;
    private TextView _policiesTextView;
    private TextView _standardInstructionTextView;

    private Button _readMoreButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;
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

        _projectNameTextView = (TextView) findViewById(R.id.projectname_textview);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);
        _customDisplayFieldsTextView = (TextView) findViewById(R.id.customdisplayfields_textview);
        _worktypeTextView = (TextView) findViewById(R.id.worktype_textview);

        _descriptionContainer = (LinearLayout) findViewById(R.id.description_container);

        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        _descriptionTextView.setOnLongClickListener(_editMode_listener);

        _descriptionShortLayout = (RelativeLayout) findViewById(R.id.descriptionShort_layout);
        _descriptionShortTextView = (TextView) findViewById(R.id.descriptionShort_textview);
        _descriptionShortTextView.setOnLongClickListener(_editMode_listener);
        _descriptionEditText = (EditText) findViewById(R.id.description_edittext);

        _confidentialTextView = (TextView) findViewById(R.id.confidential_textview);
        _confidentialTextView.setOnClickListener(_confidential_onClick);

        _policiesTextView = (TextView) findViewById(R.id.policies_textview);
        _policiesTextView.setOnClickListener(_policies_onClick);

        _standardInstructionTextView = (TextView) findViewById(R.id.standardInstructions_textview);
        _standardInstructionTextView.setOnClickListener(_standardInstructions_onClick);

        _bundleWarningTextView = (TextView) findViewById(R.id.bundlewarning_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);

        _readMoreButton = (Button) findViewById(R.id.readMore_button);
        _readMoreButton.setOnClickListener(_readMore_onClick);

        setVisibility(View.GONE);
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        setVisibility(View.VISIBLE);

        _projectNameTextView.setText(_workorder.getTitle());

        _workorderIdTextView.setText("Work Order Id: " + _workorder.getWorkorderId());

        if (_workorder.getCustomDisplayFields() != null && _workorder.getCustomDisplayFields().length > 0) {
            StringBuilder sb = new StringBuilder();

            CustomDisplayFields[] cdfs = _workorder.getCustomDisplayFields();
            for (CustomDisplayFields cdf : cdfs) {
                sb.append(cdf.getLabel()).append(": ").append(cdf.getValue()).append("\n");
            }
            _customDisplayFieldsTextView.setText(sb.toString().trim());
        }

        _worktypeTextView.setText(_workorder.getTypeOfWork());

        if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
            _bundleWarningTextView.setVisibility(View.VISIBLE);
        } else {
            _bundleWarningTextView.setVisibility(View.GONE);
        }

        if (misc.isEmptyOrNull(_workorder.getFullWorkDescription())) {
            _descriptionContainer.setVisibility(GONE);
        } else {
            _descriptionContainer.setVisibility(VISIBLE);
            _descriptionTextView.setText(misc.linkifyHtml(_workorder.getFullWorkDescription(), Linkify.ALL));
            _descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
            _descriptionEditText.setText(misc.linkifyHtml(_workorder.getFullWorkDescription(), Linkify.ALL));

            _descriptionShortTextView.setText(misc.linkifyHtml(_workorder.getFullWorkDescription(), Linkify.ALL));
            _descriptionShortTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (!_workorder.canViewConfidentialInfo()) {
            _policiesTextView.setVisibility(View.GONE);
            _confidentialTextView.setVisibility(View.GONE);
        } else {
            if (!misc.isEmptyOrNull(_workorder.getCustomerPoliciesProcedures())) {
                _policiesTextView.setVisibility(View.VISIBLE);
            } else {
                _policiesTextView.setVisibility(View.GONE);
            }

            if (!misc.isEmptyOrNull(_workorder.getConfidentialInformation())) {
                _confidentialTextView.setVisibility(View.VISIBLE);
            } else {
                _confidentialTextView.setVisibility(View.GONE);
            }
        }

        if (!misc.isEmptyOrNull(_workorder.getStandardInstruction())) {
            _standardInstructionTextView.setVisibility(VISIBLE);
        } else {
            _standardInstructionTextView.setVisibility(GONE);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _readMore_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _descriptionEditText.setVisibility(GONE);

            if (_descriptionShortLayout.getVisibility() == VISIBLE) {
                _descriptionTextView.setVisibility(View.VISIBLE);
                _descriptionShortLayout.setVisibility(View.GONE);
                _readMoreButton.setText(R.string.btn_read_less);
            } else {
                _descriptionTextView.setVisibility(View.GONE);
                _descriptionShortLayout.setVisibility(View.VISIBLE);
                _readMoreButton.setText(R.string.btn_read_more);
            }
        }
    };

    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getContext().startActivity(intent);
        }
    };

    private final View.OnClickListener _confidential_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showConfidentialInfo(_workorder.getConfidentialInformation());
        }
    };

    private final View.OnClickListener _policies_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showCustomerPolicies(_workorder.getCustomerPoliciesProcedures());
        }
    };

    private final OnClickListener _standardInstructions_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showStandardInstructions(_workorder.getStandardInstruction());
        }
    };

    private OnLongClickListener _editMode_listener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            _descriptionTextView.setVisibility(GONE);
            if (_descriptionShortLayout.getVisibility() == VISIBLE) {
                _descriptionTextView.setVisibility(View.GONE);
                _descriptionShortLayout.setVisibility(View.GONE);
                _readMoreButton.setText(R.string.btn_read_less);
            }
            _descriptionEditText.setVisibility(VISIBLE);
            if (Build.VERSION.SDK_INT >= 11) {
                _descriptionEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                _descriptionEditText.setTextIsSelectable(true);
            } else {
                _descriptionEditText.setRawInputType(InputType.TYPE_NULL);
                _descriptionEditText.setFocusable(true);
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