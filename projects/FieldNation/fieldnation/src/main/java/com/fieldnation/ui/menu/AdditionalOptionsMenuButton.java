package com.fieldnation.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.SavedSearchTracker;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.ui.nav.AdditionalOptionsActivity;

/**
 * Created by mc on 12/21/16.
 */

public class AdditionalOptionsMenuButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("AdditionalOptionsActionBarButton");

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public AdditionalOptionsMenuButton(Context context) {
        super(context);
        init();
    }

    public AdditionalOptionsMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdditionalOptionsMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
            SavedSearchTracker.onClick(App.get(), SavedSearchTracker.Item.ADDITIONAL_OPTIONS);
            AdditionalOptionsActivity.startNew(getContext());
        }
    };
}
