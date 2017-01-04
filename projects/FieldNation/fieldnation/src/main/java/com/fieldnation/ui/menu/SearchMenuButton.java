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
import com.fieldnation.ui.dialog.v2.SearchDialog;
import com.fieldnation.ui.search.EditSearchActivity;

public class SearchMenuButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("SearchActionBarButton");

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public SearchMenuButton(Context context) {
        super(context);
        init();
    }

    public SearchMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchMenuButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_action_bar, this);

        if (isInEditMode())
            return;

        //setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //EditSearchActivity.startNew(getContext());
            SavedSearchTracker.onClick(App.get(), SavedSearchTracker.Item.SEARCH);
            SearchDialog.Controller.show(App.get());
        }
    };
}