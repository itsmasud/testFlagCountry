package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
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
    private LinearLayout _buyerRatingLayout;
    private TextView _expectationsTextView;
    private TextView _professionalismTextView;
    private TextView _approvalTextView;
    private LinearLayout _detailLayout;


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
        _buyerRatingLayout = (LinearLayout) findViewById(R.id.buyerRating_layout);
        _expectationsTextView = (TextView) findViewById(R.id.expectationsPercent_textview);
        _professionalismTextView = (TextView) findViewById(R.id.professionalism_textview);
        _approvalTextView = (TextView) findViewById(R.id.approval_textview);
        _detailLayout = (LinearLayout) findViewById(R.id.detail_layout);

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
            setVisibility(GONE);
            return;
        } else {
            _nameTextView.setText(_workorder.getCompanyName());
        }

        BuyerRating rating = _workorder.getBuyerRatingInfo();

        if (rating == null) {
            _buyerRatingLayout.setVisibility(GONE);
            _starRating.setVisibility(GONE);
        } else {
            if (rating.getStarRate() != null) {
                _starRating.setStars(rating.getStarRate());
                _starRating.setVisibility(VISIBLE);
            } else {
                _starRating.setVisibility(GONE);
            }

            if (rating.getRespectful() != null && rating.getScopeRating() != null) {
                // gah! no data!
            }
            _buyerRatingLayout.setVisibility(GONE);

        }


    }

}
