package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgressNode extends RelativeLayout {

	// UI
	private ImageView _iconImageView;
	private TextView _labelTextView;

	// DATA

	public ProgressNode(Context context) {
		super(context);
		init(null, -1);
	}

	public ProgressNode(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, -1);
	}

	public ProgressNode(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wodetail_progress_node, this);

		if (isInEditMode())
			return;

		_iconImageView = (ImageView) findViewById(R.id.icon_imageview);
		_labelTextView = (TextView) findViewById(R.id.label_textview);

		if (attrs != null) {
			TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.WorkorderDetail_ProgressLabel);
			_labelTextView.setText(t.getString(R.styleable.WorkorderDetail_ProgressLabel_text));
			t.recycle();
		}
	}

	public int getIconWidth() {
		return _iconImageView.getWidth();
	}
}
