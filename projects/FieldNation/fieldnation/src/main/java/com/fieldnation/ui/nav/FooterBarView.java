package com.fieldnation.ui.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.inbox.InboxActivity;

/**
 * Created by Michael on 8/31/2016.
 */
public class FooterBarView extends RelativeLayout {
    private static final String TAG = "FooterBarView";

    // Ui
    private IconFontTextView _inboxTextView;
    private IconFontTextView _menuTextView;

    public FooterBarView(Context context) {
        super(context);
        init();
    }

    public FooterBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_footerbar, this);

        if (isInEditMode())
            return;

        _inboxTextView = (IconFontTextView) findViewById(R.id.inbox_textview);
        _inboxTextView.setOnClickListener(_inbox_onClick);
        _menuTextView = (IconFontTextView) findViewById(R.id.menu_textview);
        _menuTextView.setOnClickListener(_menu_onClick);
    }

    private final View.OnClickListener _inbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            InboxActivity.startNew(v.getContext());
        }
    };

    private final View.OnClickListener _menu_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsActivity.startNew(v.getContext());
        }
    };
}