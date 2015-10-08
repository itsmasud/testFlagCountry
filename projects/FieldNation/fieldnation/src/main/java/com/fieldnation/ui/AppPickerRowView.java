package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

public class AppPickerRowView extends RelativeLayout {
    private ImageView _icon;
    private TextView _name;
    private AppPickerPackage _pack;
    private OnClickListener _listener;

    public AppPickerRowView(Context context) {
        super(context);
        init();
    }

    public AppPickerRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppPickerRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_app_row, this);

        if (isInEditMode()) {
            return;
        }

        _icon = (ImageView) findViewById(R.id.icon_imageview);
        _name = (TextView) findViewById(R.id.name_textview);

        setOnClickListener(_this_onClick);
    }

    public void setInfo(AppPickerPackage pack) {
        _pack = pack;
        _name.setText(pack.appName);
        _icon.setBackgroundDrawable(pack.icon);
    }

    public void setListener(OnClickListener listener) {
        _listener = listener;
    }

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(AppPickerRowView.this, _pack);
        }
    };

    public interface OnClickListener {
        void onClick(AppPickerRowView row, AppPickerPackage pack);
    }
}
