package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.StarView;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrderRatingsBuyerOverall;
import com.fieldnation.v2.data.model.WorkOrderRatingsBuyerOverallPercentApproval;
import com.fieldnation.v2.ui.ListItemTwoHorizView;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

/**
 * Created by Michael Carver on 5/22/2015.
 */
public class CompanySummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "CompanySummaryView";


    private TextView _companyNameTextView;
    private TextView _companyNotesTextView;
    private StarView _starRating;
    private ListItemTwoHorizView _clearExpectationView;
    private ListItemTwoHorizView _professionalismView;
    private ListItemTwoHorizView _reviewPeriodView;
    private RelativeLayout _ttaLayout;
    private TextView _ttaNotesTextView;
    private TextView _ttaDaysTextView;


    private WorkOrder _workOrder;

    public CompanySummaryView(Context context) {
        super(context);
        init();
    }

    public CompanySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompanySummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_company_summary, this);

        Log.v(TAG, "Init");

        if (isInEditMode())
            return;

        _companyNameTextView = findViewById(R.id.companyName_textview);
        _companyNotesTextView = findViewById(R.id.companyNotes_textview);
        _starRating = findViewById(R.id.star_rating);
        _clearExpectationView = findViewById(R.id.clearExpectation_view);
        _professionalismView = findViewById(R.id.professionalism_view);
        _reviewPeriodView = findViewById(R.id.reviewPeriod_view);
        _ttaLayout = findViewById(R.id.tta_layout);
        _ttaNotesTextView = findViewById(R.id.ttaNotes_textview);
        _ttaDaysTextView = findViewById(R.id.ttaDays_textview);

        setVisibility(GONE);

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void populateUi() {

        if (_ttaDaysTextView == null)
            return;

        if (_workOrder == null)
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
        }

        _companyNameTextView.setText(R.string.company_name_hidden);

        if (_workOrder.getCompany() != null) {
            if (!misc.isEmptyOrNull(_workOrder.getCompany().getName())) {
                _companyNameTextView.setText(_workOrder.getCompany().getName());
            } else {
                _companyNameTextView.setText(R.string.company_name_hidden);
            }
        } else {
            _companyNameTextView.setText(R.string.company_name_hidden);
        }

        // City Stat, # reviews
        String desc = "";
        if (_workOrder.getCompany() != null
                && _workOrder.getCompany().getLocation() != null
                && !misc.isEmptyOrNull(_workOrder.getCompany().getLocation().getCity())
                && !misc.isEmptyOrNull(_workOrder.getCompany().getLocation().getState())) {
            desc = _workOrder.getCompany().getLocation().getCity() + ", " + _workOrder.getCompany().getLocation().getState();
        }

        if (_workOrder.getRatings() != null
                && _workOrder.getRatings().getBuyer() != null
                && _workOrder.getRatings().getBuyer().getOverall() != null
                && _workOrder.getRatings().getBuyer().getOverall().getRatings() != null) {
            if (misc.isEmptyOrNull(desc)) {
                desc = _workOrder.getRatings().getBuyer().getOverall().getRatings() + " Reviews";
            } else {
                desc += " \u2022 " + _workOrder.getRatings().getBuyer().getOverall().getRatings() + " Reviews";
            }
        }

        if (misc.isEmptyOrNull(desc)) {
            _companyNotesTextView.setVisibility(GONE);
        } else {
            _companyNotesTextView.setVisibility(VISIBLE);
            _companyNotesTextView.setText(desc);
        }

        WorkOrderRatingsBuyerOverall overall = _workOrder.getRatings().getBuyer().getOverall();
        // stars
        if (overall != null && overall.getStars() != null) {
            _starRating.setStars(overall.getStars().intValue());
        } else {
            _starRating.setStars(0);
        }

        // Expectations
        if (overall != null && overall.getPercentClearExpectations() != null) {
            _clearExpectationView.setVisibility(VISIBLE);
            _clearExpectationView.set("Clear Expectations", overall.getPercentClearExpectations() + "%");
        } else {
            _clearExpectationView.setVisibility(GONE);
        }

        // Professionalism
        if (overall != null && overall.getPercentRespectful() != null) {
            _professionalismView.setVisibility(VISIBLE);
            _professionalismView.set("Professionalism", overall.getPercentRespectful() + "%");
        } else {
            _professionalismView.setVisibility(GONE);
        }

        // Review Period
        if (overall != null && overall.getApprovalPeriod() != null) {
            _reviewPeriodView.setVisibility(VISIBLE);
            _reviewPeriodView.set("Review Period", overall.getApprovalPeriod() + " days");
        } else {
            _reviewPeriodView.setVisibility(GONE);
        }

        // Time To Approval
        if (overall != null && overall.getApprovalDays() != null) {
            _ttaLayout.setVisibility(VISIBLE);
            _ttaDaysTextView.setText(overall.getApprovalDays() + " days");
            boolean found = false;
            for (WorkOrderRatingsBuyerOverallPercentApproval ob : overall.getPercentApproval()) {
                if (ob.getDays().equals(overall.getApprovalDays())) {
                    _ttaNotesTextView.setText(getResources().getString(R.string.company_percentage_approval, ob.getPercent()));
                    found = true;
                    break;
                }
            }
            if (found) {
                _ttaNotesTextView.setVisibility(VISIBLE);
            } else {
                _ttaNotesTextView.setVisibility(GONE);
            }
        } else {
            _ttaLayout.setVisibility(GONE);
        }
    }
}
