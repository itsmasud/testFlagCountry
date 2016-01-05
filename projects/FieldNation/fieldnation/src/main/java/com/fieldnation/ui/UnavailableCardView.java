package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 6/24/2015.
 */
public class UnavailableCardView extends FrameLayout {
    private static final String TAG = "UnavailableCardView";

    // Ui
    private LinearLayout _layoutMarketplaceUnavailable;
    private LinearLayout _layoutMarketplaceAccessDenied;
    private LinearLayout _layoutMarketplaceSuspended;
    private TextView _titleTextView;
    private TextView _captionTexView;
    private Button _actionButton;

    public UnavailableCardView(Context context) {
        super(context);
        init();
    }

    public UnavailableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnavailableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_unavailable_card, this);

        if (isInEditMode())
            return;
         _layoutMarketplaceUnavailable = (LinearLayout) findViewById(R.id.marketplaceUnavailable_layout);
         _layoutMarketplaceAccessDenied = (LinearLayout) findViewById(R.id.marketplaceAccessDenied_layout);
         _layoutMarketplaceSuspended = (LinearLayout) findViewById(R.id.marketplaceSuspended_layout);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _captionTexView = (TextView) findViewById(R.id.caption_textview);
        _actionButton = (Button) findViewById(R.id.action_button);
    }

    public void setData(String title, String caption, String buttonText) {
        _titleTextView.setText(title);
        _captionTexView.setText(caption);
        _actionButton.setText(buttonText);
    }

    public void setActionOnClickListener(View.OnClickListener l) {
        _actionButton.setOnClickListener(l);
    }
}
