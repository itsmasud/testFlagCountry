package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.ui.nav.AdditionalOptionsActivity;

/**
 * Created by mc on 12/21/16.
 */

public class AdditionalOptionsActionBarButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("AdditionalOptionsActionBarButton");

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public AdditionalOptionsActionBarButton(Context context) {
        super(context);
        init();
    }

    public AdditionalOptionsActionBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdditionalOptionsActionBarButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_additional_options_action_bar, this);

        if (isInEditMode())
            return;

        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsActivity.startNew(getContext());
        }
    };
}
