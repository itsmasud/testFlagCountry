package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WoProgressBar extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.WoProgressBar";

	// UI
	private ProgressNode[] _nodes;
	private ImageView[] _sep;

	public WoProgressBar(Context context) {
		super(context);
		init();
	}

	public WoProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WoProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wodetail_progress, this);

		if (isInEditMode())
			return;

		_nodes = new ProgressNode[4];

		_nodes[0] = (ProgressNode) findViewById(R.id.progressNode1);
		_nodes[1] = (ProgressNode) findViewById(R.id.progressNode2);
		_nodes[2] = (ProgressNode) findViewById(R.id.progressNode3);
		_nodes[3] = (ProgressNode) findViewById(R.id.progressNode4);

		_sep = new ImageView[3];
		_sep[0] = (ImageView) findViewById(R.id.sep1);
		_sep[1] = (ImageView) findViewById(R.id.sep2);
		_sep[2] = (ImageView) findViewById(R.id.sep3);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.v(TAG, "onLayout " + changed);

		LinearLayout.LayoutParams p = null;

		boolean updated = false;
		for (int i = 0; i < 3; i++) {
			p = (LinearLayout.LayoutParams) _sep[i].getLayoutParams();

			int temp = -(_nodes[i].getWidth() - _nodes[i].getIconWidth()) / 2 + 10;
			if (temp != p.leftMargin)
				updated = true;
			p.leftMargin = temp;

			temp = -(_nodes[i + 1].getWidth() - _nodes[i + 1].getIconWidth()) / 2 + 10;
			if (temp != p.rightMargin)
				updated = true;
			p.rightMargin = temp;

			_sep[i].forceLayout();

			// _sep[i].setLayoutParams(p);
			// _sep[i].invalidate();
		}
		if (updated) {
			// _nodes[0].layout(_nodes[0].getLeft(), _nodes[0].getTop(),
			// _nodes[0].getRight(), _nodes[0].getBottom());
			// _sep[0].layout(_sep[0].getLeft(), _sep[0].getTop(),
			// _sep[0].getRight(), _sep[0].getBottom());
			// _nodes[1].layout(_nodes[1].getLeft(), _nodes[1].getTop(),
			// _nodes[1].getRight(), _nodes[1].getBottom());
			// _sep[1].layout(_sep[1].getLeft(), _sep[1].getTop(),
			// _sep[1].getRight(), _sep[1].getBottom());
			// _nodes[2].layout(_nodes[2].getLeft(), _nodes[2].getTop(),
			// _nodes[2].getRight(), _nodes[2].getBottom());
			// _sep[2].layout(_sep[2].getLeft(), _sep[2].getTop(),
			// _sep[2].getRight(), _sep[2].getBottom());
			// _nodes[3].layout(_nodes[3].getLeft(), _nodes[3].getTop(),
			// _nodes[3].getRight(), _nodes[3].getBottom());
			// requestLayout();
		}
		super.onLayout(changed, l, t, r, b);
	}
}
