package com.fieldnation.ui;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppPickerRowView extends RelativeLayout {
	private ImageView _icon;
	private TextView _name;
	private AppPickerPackage _pack;

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
	}

	@SuppressWarnings("deprecation")
	public void setInfo(AppPickerPackage pack) {
		_pack = pack;
		_name.setText(pack.appName);
		_icon.setBackgroundDrawable(pack.icon);
	}
}
