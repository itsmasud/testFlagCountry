package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressNode extends LinearLayout {

	// UI
	private ImageView _iconImageView;
	private TextView _labelTextView;
	
	private static final int PROGRESSBAR_STEP_COMPLETED = 1;
	private static final int PROGRESSBAR_STEP_ACTIVE = 2;
	private static final int PROGRESSBAR_STEP_INACTIVE = 3;
	private static final int PROGRESSBAR_ON_HOLD_ACKNOWLEDGEMENT = 4;
	private static final int PROGRESSBAR_ON_HOLD_UNACKNOWLEDGEMENT = 5;
	

	// DATA

	public ProgressNode(Context context) {
		super(context);
		init(null);
	}

	public ProgressNode(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_progress_node, this);

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
	
	public void setActive(Integer ProgressBarStep) {
		switch(ProgressBarStep){
			case PROGRESSBAR_STEP_COMPLETED:
				_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_done);
				break;
			case PROGRESSBAR_STEP_ACTIVE:
				_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_active);
				break;			
			case PROGRESSBAR_ON_HOLD_ACKNOWLEDGEMENT:
				_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_hold);
				break;
			case PROGRESSBAR_ON_HOLD_UNACKNOWLEDGEMENT:
				_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_hold_alert);
				break;
			default:
				_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_inactive);
				break;
		}
		
		/*if (active) {			
			_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_active);			
		} else {
			_iconImageView.setBackgroundResource(R.drawable.ic_wo_detail_progress_inactive);
		}*/
		
		invalidate();

	}

	public void setLabel(int resId) {
		_labelTextView.setText(resId);
	}

	public void setLabel(String value) {
		_labelTextView.setText(value);
	}
}
