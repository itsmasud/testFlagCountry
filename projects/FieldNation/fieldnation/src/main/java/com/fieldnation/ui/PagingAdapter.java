package com.fieldnation.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.App;
import com.fieldnation.Log;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by michael.carver on 11/21/2014.
 */
public abstract class PagingAdapter<T> extends BaseAdapter {
    private static final String TAG = "PagingAdapter";

    private RateMeView _rateMeView = null;

    private Hashtable<Integer, List<T>> _pages = new Hashtable<>();
    private Set<Integer> _loadingPages = new HashSet<>();

    private int _rateMePosition = 5;
    private boolean _showRateMe = false;
    private boolean _noMorePages = false;

    private int _size = 0;
    private OnLoadingCompleteListener _onLoadingCompleteListener;

    public PagingAdapter() {
        _showRateMe = App.get().showRateMe();
    }

    public void setPage(int page, List<T> items) {
        Log.v(TAG, "setPage()");

        if (_loadingPages.contains(page)) {
            _loadingPages.remove(page);
        }

        // if list is empty, then this is the last page
        if (items == null || items.size() == 0) {
            int i = page;
            while (_pages.containsKey(i)) {
                _pages.remove(i);
                i++;
            }
            countItems();

            if (_loadingPages.size() == 0) {
                setNoMorePages();
            }

            // list is not empty
        } else if (items.size() > 0) {
            _pages.put(page, items);
            countItems();

            if (_onLoadingCompleteListener != null) {
                _onLoadingCompleteListener.onLoadingComplete();
            }
        }

        notifyDataSetChanged();
    }

    public void setOnLoadingCompleteListener(OnLoadingCompleteListener onLoadingCompleteListener) {
        _onLoadingCompleteListener = onLoadingCompleteListener;
    }

    public void refreshPages() {
        Log.v(TAG, "refreshPages");
        if (_pages.size() == 0) {
            preRequestPage(0, false);
        } else {
            for (int i = 0; i <= _pages.size(); i++) {
                preRequestPage(i, false);
            }
        }
    }

    private void countItems() {
        int count = 0;
        for (int i = 0; i < _pages.size(); i++) {
            List<T> page = _pages.get(i);
            if (page == null)
                break;
            count += page.size();
        }
        _size = count;
    }

    public void setNoMorePages() {
        Log.v(TAG, "setNoMorePages()");
        _noMorePages = true;
        if (_onLoadingCompleteListener != null) {
            _onLoadingCompleteListener.onLoadingComplete();
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
//        Log.v(TAG, "registerDataSetObserver()");
        super.registerDataSetObserver(observer);
        if (!_noMorePages && !_loadingPages.contains(_pages.size())) {
            requestPage(_pages.size(), true);
        }
    }

    @Override
    public int getCount() {
        // Log.v(TAG, "getCount()");
        return _size;
    }

    @Override
    public Object getItem(int position) {
//        Log.v(TAG, "getItem()");

        if (position == _rateMePosition && _showRateMe) {
            return null;
        } else if (position > _rateMePosition && _showRateMe) {
            position--;
        }

        // find the page that has this item
        int count = 0;
        List<T> page = null;
        int pageNum;
        for (pageNum = 0; pageNum < _pages.size(); pageNum++) {
            page = _pages.get(pageNum);
            count = count + page.size();
            if (count > position)
                break;
        }

        // if  not last page, and there could be more pages then request the next page
        if (_pages.get(_pages.size() - 1) == page && !_noMorePages) {
            preRequestPage(_pages.size(), true);
        } else if (!_pages.containsKey(pageNum + 1) && !_noMorePages) {
            preRequestPage(pageNum + 1, true);
        }

        // find the begining of the page
        count = count - page.size();
        // pull out the item
        return page.get(position - count);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Log.v(TAG, "getView()");
        // find the page
        if (position == _rateMePosition && _showRateMe) {
            if (_rateMeView == null) {
                _rateMeView = new RateMeView(parent.getContext());
                _rateMeView.setListener(_rateMe_listener);
            }
            App.get().setRateMeShown();
            return _rateMeView;
        }

        // get the page
        T obj = (T) getItem(position);

        // create the view
        return getView(obj, convertView, parent);
    }

    private void preRequestPage(int page, boolean allowCache) {
        if (!_loadingPages.contains(page)) {
            _loadingPages.add(page);
            requestPage(page, allowCache);
        }
    }

    private final RateMeView.Listener _rateMe_listener = new RateMeView.Listener() {
        @Override
        public void onHide() {
            _showRateMe = false;
            notifyDataSetInvalidated();
        }
    };

    public abstract View getView(T object, View convertView, ViewGroup parent);

    public abstract void requestPage(int page, boolean allowCache);

    public interface OnLoadingCompleteListener {
        void onLoadingComplete();
    }

}
