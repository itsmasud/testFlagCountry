package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.market.MarketActivity;

/**
 * Created by Michael Carver on 2/2/2015.
 */
public class EmptyWoListView extends RelativeLayout {
    public EmptyWoListView(Context context) {
        super(context);
        init();
    }

    public EmptyWoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyWoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_empty_wo_list, this);

        setOnClickListener(_this_onClick);
    }

    private OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MarketActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
}
