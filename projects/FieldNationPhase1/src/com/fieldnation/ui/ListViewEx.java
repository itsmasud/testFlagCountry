package com.fieldnation.ui;

import com.fieldnation.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A custom listview class that provides hooks into the overscroll detection
 * 
 * @author michael.carver
 * 
 */
public class ListViewEx extends ListView {
	private static final String TAG = "ui.ListViewEx";

	private static final int _TAP_TO_REFRESH = 1;
	private static final int _PULL_TO_REFRESH = 2;
	private static final int _RELEASE_TO_REFRESH = 3;
	private static final int _REFRESHING = 4;

	// Listeners
	private OnRefreshListener _onRefreshListener;
	private OnScrollListener _onScrollListener;
	// private LayoutInflater mInflater;

	// UI
	private RelativeLayout _refreshView;
	private TextView _refreshTextView;
	private ImageView _refreshImageView;
	private ProgressBar _refreshProgressView;
	private TextView _refreshViewLastUpdated;

	// State
	private int _currentScrollState;
	private int _refreshState;

	// Animation
	private RotateAnimation _flipAnimation;
	private RotateAnimation _reverseFlipAnimation;

	private int _refreshViewHeight;
	private int _refreshOriginalTopPadding;
	private int _lastMotionY;

	private boolean _bounceHack;

	// Lifecycle
	public ListViewEx(Context context) {
		this(context, null, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ListViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Load all of the animations we need in code rather than through XML
		_flipAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		_flipAnimation.setInterpolator(new LinearInterpolator());
		_flipAnimation.setDuration(250);
		_flipAnimation.setFillAfter(true);
		_reverseFlipAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		_reverseFlipAnimation.setInterpolator(new LinearInterpolator());
		_reverseFlipAnimation.setDuration(250);
		_reverseFlipAnimation.setFillAfter(true);

		// TODO make into custom view
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_refreshView = (RelativeLayout) inflater.inflate(R.layout.view_listviewex_header, this, false);
		_refreshTextView = (TextView) _refreshView.findViewById(R.id.pull_to_refresh_text);
		_refreshImageView = (ImageView) _refreshView.findViewById(R.id.pull_to_refresh_image);
		_refreshProgressView = (ProgressBar) _refreshView.findViewById(R.id.pull_to_refresh_progress);
		_refreshViewLastUpdated = (TextView) _refreshView.findViewById(R.id.pull_to_refresh_updated_at);

		_refreshImageView.setMinimumHeight(50);
		_refreshView.setOnClickListener(new OnClickRefreshListener());
		_refreshOriginalTopPadding = _refreshView.getPaddingTop();

		_refreshState = _TAP_TO_REFRESH;

		addHeaderView(_refreshView);

		super.setOnScrollListener(_this_onScrollListener);

		measureView(_refreshView);
		_refreshViewHeight = _refreshView.getMeasuredHeight();

		prepareForRefresh();
	}

	private OnScrollListener _this_onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			_currentScrollState = scrollState;

			if (_currentScrollState == SCROLL_STATE_IDLE) {
				_bounceHack = false;
			}

			if (_onScrollListener != null) {
				_onScrollListener.onScrollStateChanged(view, scrollState);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// When the refresh view is completely visible, change the text to
			// say
			// "Release to refresh..." and flip the arrow drawable.
			if (_currentScrollState == SCROLL_STATE_TOUCH_SCROLL && _refreshState != _REFRESHING) {
				if (firstVisibleItem == 0) {
					_refreshImageView.setVisibility(View.VISIBLE);
					if ((_refreshView.getBottom() >= _refreshViewHeight + 20 || _refreshView.getTop() >= 0) && _refreshState != _RELEASE_TO_REFRESH) {
						_refreshTextView.setText(R.string.pull_to_refresh_release_label);
						_refreshImageView.clearAnimation();
						_refreshImageView.startAnimation(_flipAnimation);
						_refreshState = _RELEASE_TO_REFRESH;
					} else if (_refreshView.getBottom() < _refreshViewHeight + 20 && _refreshState != _PULL_TO_REFRESH) {
						_refreshTextView.setText(R.string.pull_to_refresh_pull_label);
						if (_refreshState != _TAP_TO_REFRESH) {
							_refreshImageView.clearAnimation();
							_refreshImageView.startAnimation(_reverseFlipAnimation);
						}
						_refreshState = _PULL_TO_REFRESH;
					}
				} else {
					_refreshImageView.setVisibility(View.GONE);
					resetHeader();
				}
			} else if (_currentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0 && _refreshState != _REFRESHING) {
				setSelection(1);
				_bounceHack = true;
			} else if (_bounceHack && _currentScrollState == SCROLL_STATE_FLING) {
				setSelection(1);
			}

			if (_onScrollListener != null) {
				_onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		}
	};

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setSelection(1);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(1);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		_onScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list should be refreshed.
	 * 
	 * @param onRefreshListener
	 *            The callback to run.
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		_onRefreshListener = onRefreshListener;
	}

	/**
	 * Set a text to represent when the list was last updated.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			_refreshViewLastUpdated.setVisibility(View.VISIBLE);
			_refreshViewLastUpdated.setText(lastUpdated);
		} else {
			_refreshViewLastUpdated.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int y = (int) event.getY();
		_bounceHack = false;

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (!isVerticalScrollBarEnabled()) {
				setVerticalScrollBarEnabled(true);
			}
			if (getFirstVisiblePosition() == 0 && _refreshState != _REFRESHING) {
				if ((_refreshView.getBottom() >= _refreshViewHeight || _refreshView.getTop() >= 0) && _refreshState == _RELEASE_TO_REFRESH) {
					// Initiate the refresh
					_refreshState = _REFRESHING;
					prepareForRefresh();
					onRefresh();
				} else if (_refreshView.getBottom() < _refreshViewHeight || _refreshView.getTop() <= 0) {
					// Abort refresh and scroll down below the refresh view
					resetHeader();
					setSelection(1);
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
			_lastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			applyHeaderPadding(event);
			break;
		}
		return super.onTouchEvent(event);
	}

	private void applyHeaderPadding(MotionEvent ev) {
		// getHistorySize has been available since API 1
		int pointerCount = ev.getHistorySize();

		for (int p = 0; p < pointerCount; p++) {
			if (_refreshState == _RELEASE_TO_REFRESH) {
				if (isVerticalFadingEdgeEnabled()) {
					setVerticalScrollBarEnabled(false);
				}

				int historicalY = (int) ev.getHistoricalY(p);

				// Calculate the padding to apply, we divide by 1.7 to
				// simulate a more resistant effect during pull.
				int topPadding = (int) (((historicalY - _lastMotionY) - _refreshViewHeight) / 1.7);

				_refreshView.setPadding(_refreshView.getPaddingLeft(), topPadding, _refreshView.getPaddingRight(),
						_refreshView.getPaddingBottom());
			}
		}
	}

	/**
	 * Sets the header padding back to original size.
	 */
	private void resetHeaderPadding() {
		_refreshView.setPadding(_refreshView.getPaddingLeft(), _refreshOriginalTopPadding,
				_refreshView.getPaddingRight(), _refreshView.getPaddingBottom());
	}

	/**
	 * Resets the header to the original state.
	 */
	private void resetHeader() {
		if (_refreshState != _TAP_TO_REFRESH) {
			Log.v(TAG, "resetHeader");
			_refreshState = _TAP_TO_REFRESH;

			resetHeaderPadding();

			// Set refresh view text to the pull label
			_refreshTextView.setText(R.string.pull_to_refresh_tap_label);
			// Replace refresh drawable with arrow drawable
			_refreshImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			// Clear the full rotation animation
			_refreshImageView.clearAnimation();
			// Hide progress bar and arrow.
			_refreshImageView.setVisibility(View.GONE);
			_refreshProgressView.setVisibility(View.GONE);
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void prepareForRefresh() {
		resetHeaderPadding();

		_refreshImageView.setVisibility(View.GONE);
		// We need this hack, otherwise it will keep the previous drawable.
		_refreshImageView.setImageDrawable(null);
		_refreshProgressView.setVisibility(View.VISIBLE);

		// Set refresh view text to the refreshing label
		_refreshTextView.setText(R.string.pull_to_refresh_refreshing_label);

		_refreshState = _REFRESHING;
	}

	public void onRefresh() {
		Log.d(TAG, "onRefresh");

		if (_onRefreshListener != null) {
			_onRefreshListener.onRefresh();
		}
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onRefreshComplete();
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 */
	public void onRefreshComplete() {
		Log.d(TAG, "onRefreshComplete");

		resetHeader();

		// If refresh view is visible when loading completes, scroll down to
		// the next item.
		if (getFirstVisiblePosition() == 0) {
			invalidateViews();
			setSelection(1);
		}
	}

	/**
	 * Invoked when the refresh view is clicked on. This is mainly used when
	 * there's only a few items in the list and it's not possible to drag the
	 * list.
	 */
	private class OnClickRefreshListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (_refreshState != _REFRESHING) {
				prepareForRefresh();
				onRefresh();
			}
		}

	}

	/**
	 * Interface definition for a callback to be invoked when list should be
	 * refreshed.
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the list should be refreshed.
		 * <p>
		 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
		 * expected to indicate that the refresh has completed.
		 */
		public void onRefresh();
	}

}
