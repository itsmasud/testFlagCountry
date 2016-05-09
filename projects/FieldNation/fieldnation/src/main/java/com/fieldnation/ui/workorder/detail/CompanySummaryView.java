package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.BuyerRating;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.StarView;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 5/22/2015.
 */
public class CompanySummaryView extends RelativeLayout {
    private static final String TAG = "CompanySummaryView";

    private TextView _nameTextView;
    private StarView _starRating;
    private TextView _locationTextView;
    private TextView _reviewsTextView;
    private TextView _newBuyerTextView;
    private RelativeLayout _detailsLayout;
    private ProgressBar _expectationsProgressBar;
    private ProgressBar _professionalismProgressBar;
    private TextView _expectationsTextView;
    private TextView _professionalismTextView;
    private TextView _daysTextView;


    private Workorder _workorder;

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
        _locationTextView = (TextView) findViewById(R.id.location_textview);
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

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    public void populateUi() {
        if (_nameTextView == null)
            return;

        if (_workorder == null)
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
        }

        if (misc.isEmptyOrNull(_workorder.getCompanyName())) {
            _nameTextView.setText("Company Name Hidden");
        } else {
            _nameTextView.setText(_workorder.getCompanyName());
        }

        BuyerRating rating = _workorder.getBuyerRatingInfo();

        if (rating == null || rating.getTotalRating() == null) {
            _newBuyerTextView.setVisibility(VISIBLE);
            _starRating.setStars(0);
            _detailsLayout.setVisibility(GONE);
            _locationTextView.setText("Location not available");
            _reviewsTextView.setVisibility(GONE);
        } else {
            _newBuyerTextView.setVisibility(GONE);
            _detailsLayout.setVisibility(VISIBLE);

            _daysTextView.setText(rating.getTimeToApproval() + " Days");

            if (rating.getAvgRating() != null) {
                _starRating.setStars((int) (double) rating.getAvgRating());
            } else {
                _starRating.setStars(0);
            }

            if (rating.getClearExpectationRatingPercent() != null) {
                _expectationsProgressBar.setProgress((int) (double) rating.getClearExpectationRatingPercent());
                _expectationsTextView.setText(((int) (double) rating.getClearExpectationRatingPercent()) + "%");
            }

            if (rating.getProfessionalismRatingPercent() != null) {
                _professionalismProgressBar.setProgress((int) (double) rating.getProfessionalismRatingPercent());
                _professionalismTextView.setText(((int) (double) rating.getProfessionalismRatingPercent()) + "%");
            }

            if (rating.getTotalRating() != null) {
                if (misc.isEmptyOrNull(rating.getCity()) || misc.isEmptyOrNull(rating.getState())) {
                    _locationTextView.setText("Location not available");
                } else {
                    _locationTextView.setText(rating.getCity() + ", " + rating.getState());
                }
                _reviewsTextView.setVisibility(VISIBLE);
                _reviewsTextView.setText(rating.getTotalRating() + " Reviews");
                _newBuyerTextView.setVisibility(GONE);
            } else {
                _newBuyerTextView.setVisibility(VISIBLE);
                if (misc.isEmptyOrNull(rating.getCity()) || misc.isEmptyOrNull(rating.getState())) {
                    _locationTextView.setText("Location not available");
                } else {
                    _locationTextView.setText(rating.getCity() + ", " + rating.getState());
                }
                _reviewsTextView.setVisibility(GONE);
            }
        }
    }
}
