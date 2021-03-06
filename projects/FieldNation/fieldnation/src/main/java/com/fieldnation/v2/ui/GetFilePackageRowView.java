package com.fieldnation.v2.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.ApatheticOnClickListener;

public class GetFilePackageRowView extends RelativeLayout {
    private ImageView _icon;
    private TextView _name;
    private GetFilePackage _pack;
    private OnClickListener _listener;

    public GetFilePackageRowView(Context context) {
        super(context);
        init();
    }

    public GetFilePackageRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GetFilePackageRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_get_file_package_row, this);

        if (isInEditMode()) {
            return;
        }

        _icon = findViewById(R.id.icon_imageview);
        _name = findViewById(R.id.name_textview);

        setOnClickListener(_this_onClick);
    }

    public void setInfo(GetFilePackage pack) {
        _pack = pack;
        _name.setText(pack.appName);
        _icon.setImageDrawable(pack.icon);
    }

    public void setListener(OnClickListener listener) {
        _listener = listener;
    }

    private final View.OnClickListener _this_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) {
                Intent src = _pack.intent;
                ResolveInfo info = _pack.resolveInfo;

                src.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
                _listener.onClick(GetFilePackageRowView.this, src);
            }
        }
    };

    public interface OnClickListener {
        void onClick(GetFilePackageRowView row, Intent src);
    }
}