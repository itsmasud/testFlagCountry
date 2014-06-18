package com.fieldnation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

public class ListViewEx extends ListView {
	private static final String TAG = "OverScrollingListView";

	public ListViewEx(Context context) {
		this(context, null, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// TODO Method Stub: onOverScrolled()
		Log.v(TAG,
				"Method Stub: onOverScrolled(" + scrollX + ", " + scrollY + ", " + clampedX + ", " + clampedY + ")");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			superOnOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// TODO Method Stub: overScrollBy()
		Log.v(TAG, "Method Stub: overScrollBy(" + deltaX + "," + deltaY + ")");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return superOverScrollBy(deltaX, deltaY, scrollX, scrollY,
					scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
					isTouchEvent);
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void superOnOverScrolled(int scrollX, int scrollY,
			boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private boolean superOverScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

}
