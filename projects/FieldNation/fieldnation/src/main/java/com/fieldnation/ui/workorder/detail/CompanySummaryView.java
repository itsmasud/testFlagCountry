package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.StarView;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

/**
 * Created by Michael Carver on 5/22/2015.
 */
public class CompanySummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "CompanySummaryView";

    private TextView _nameTextView;
    private StarView _starRating;
    private TextView _reviewsTextView;
    private TextView _newBuyerTextView;
    private RelativeLayout _detailsLayout;
    private ProgressBar _expectationsProgressBar;
    private ProgressBar _professionalismProgressBar;
    private TextView _expectationsTextView;
    private TextView _professionalismTextView;
    private TextView _daysTextView;


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

        _nameTextView = (TextView) findViewById(R.id.company_textview);
        _starRating = (StarView) findViewById(R.id.star_rating);
        _reviewsTextView = (TextView) findViewById(R.id.reviews_textview);
        _newBuyerTextView = (TextView) findViewById(R.id.newBuyer_textview);
        _detailsLayout = (RelativeLayout) findViewById(R.id.details_layout);
        _expectationsProgressBar = (ProgressBar) findViewById(R.id.expectations_progressBar);
        _professionalismProgressBar = (ProgressBar) findViewById(R.id.professionalism_progressBar);
        _expectationsTextView = (TextView) findViewById(R.id.expectations_textView);
        _professionalismTextView = (TextView) findViewById(R.id.professionalism_textView);
        _daysTextView = (TextView) findViewById(R.id.days_textview);

        setVisibility(GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void populateUi() {
/*
        if (_nameTextView == null)
            return;

        if (_workOrder == null || _workOrder.getCompany() == null)
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
        }

        if (!misc.isEmptyOrNull(_workOrder.getCompany().getName())) {
            _nameTextView.setText(_workOrder.getCompany().getName());
        } else {
            _nameTextView.setText("Company Name Hidden");
        }
*/


/*        if (_workOrder.getRatings() != null
                && _workOrder.getRatings().getBuyer() != null
                && _workOrder.getRatings().getBuyer().getCompany() != null) {
            _newBuyerTextView.setVisibility(GONE);
            _detailsLayout.setVisibility(VISIBLE);

            RatingCompanyMine rating = _workOrder.getRating().getCompany().getMine();

            if (rating.getAverageDaysToApproval() != null
                    && rating.getAverageDaysToApproval() != 0) {
                _daysTextView.setVisibility(VISIBLE);
                _daysTextView.setText(rating.getAverageDaysToApproval() + " Days");
            } else {
                _daysTextView.setText("");
                _daysTextView.setVisibility(GONE);
            }

            if (rating.getStars() != null) {
                _starRating.setStars(rating.getStars().intValue());
            } else {
                _starRating.setStars(0);
            }

            if (rating.getClearExpectations() != null) {
                _expectationsProgressBar.setProgress(rating.getClearExpectations());
                _expectationsTextView.setText(rating.getClearExpectations() + "%");
            }

            if (rating.getRespectRating() != null) {
                _professionalismProgressBar.setProgress(rating.getRespectRating());
                _professionalismTextView.setText(rating.getRespectRating() + "%");
            }

            if (rating.getTotalRatings() != null) {
                _reviewsTextView.setVisibility(VISIBLE);
                _reviewsTextView.setText(rating.getTotalRatings() + " Reviews");
                _newBuyerTextView.setVisibility(GONE);
            } else {
                _newBuyerTextView.setVisibility(VISIBLE);
                _reviewsTextView.setVisibility(GONE);
            }


        } else
        if (_workOrder.getRatings() != null
                && _workOrder.getRatings().getBuyer() != null
                && _workOrder.getRatings().getBuyer().getOverall() != null) {

            WorkOrderRatingsBuyerOverall overall = _workOrder.getRatings().getBuyer().getOverall();

            if (overall.getApprovalDays() != null
                    && overall.getApprovalDays() != 0
                    && overall.getPercentApproval() != null) {
                _daysTextView.setVisibility(VISIBLE);
                _daysTextView.setText(overall.getAverageDaysToApproval() + " Days");
            } else {
                _daysTextView.setText("");
                _daysTextView.setVisibility(GONE);
            }

            if (rating.getStars() != null) {
                _starRating.setStars(rating.getStars().intValue());
            } else {
                _starRating.setStars(0);
            }

            if (rating.getClearExpectations() != null) {
                _expectationsProgressBar.setProgress(rating.getClearExpectations());
                _expectationsTextView.setText(rating.getClearExpectations() + "%");
            }

            if (rating.getRespectRating() != null) {
                _professionalismProgressBar.setProgress(rating.getRespectRating());
                _professionalismTextView.setText(rating.getRespectRating() + "%");
            }

            if (rating.getTotalRatings() != null) {
                _reviewsTextView.setVisibility(VISIBLE);
                _reviewsTextView.setText(rating.getTotalRatings() + " Reviews");
                _newBuyerTextView.setVisibility(GONE);
            } else {
                _newBuyerTextView.setVisibility(VISIBLE);
                _reviewsTextView.setVisibility(GONE);
            }
        } else {*/
/*
        _newBuyerTextView.setVisibility(VISIBLE);
        _starRating.setStars(0);
        _detailsLayout.setVisibility(GONE);
        _reviewsTextView.setVisibility(GONE);
*/
        //}

/*
        if (_workOrder.getManager() == null) {
        } else {
            _newBuyerTextView.setVisibility(GONE);
        }
*/
    }
}
