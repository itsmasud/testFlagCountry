package com.fieldnation;

import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * A custom listview class that provides hooks into the overscoll detection
 * 
 * @author michael.carver
 * 
 */
public class ListViewEx extends ListView {
	private static final String TAG = "OverScrollingListView";
	private int offset = 0;
	private List<OnOverscrollListener> _onOverscrollListeners = new LinkedList<OnOverscrollListener>();

	public ListViewEx(Context context) {
		this(context, null, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public void setOnOverScrollListener(OnOverscrollListener l) {
		_onOverscrollListeners.add(l);
	}

	private void dispatchOverscroll() {
		for (int i = 0; i < _onOverscrollListeners.size(); i++) {
			_onOverscrollListeners.get(i).onOverScroll(offset);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if ((ev.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
			offset = 0;
			dispatchOverscroll();
		}
		return super.onTouchEvent(ev);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		offset += deltaY;

		dispatchOverscroll();

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	public interface OnOverscrollListener {
		public void onOverScroll(int offset);
	}
}
