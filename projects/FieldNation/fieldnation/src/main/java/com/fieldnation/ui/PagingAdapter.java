package com.fieldnation.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    private int _rateMePosition = 2;
    private boolean _showRateMe = false;
    private boolean _noMorePages = false;
    private Hashtable<Integer, List<T>> _pages = new Hashtable<>();
    private Set<Integer> _loadingPages = new HashSet<>();
    private int _size;
    private Listener _listener;

    public PagingAdapter() {
    }

    public void setShowRateMe(boolean show) {
        _showRateMe = show;
    }

    public void setPage(int page, List<T> items) {
//        Log.v(TAG, "setPage()");
        if (items == null || items.size() == 0) {
            int i = page;
            while (_pages.containsKey(i)) {
                _pages.remove(i);
                i++;
            }
            countItems();
        } else if (items.size() > 0) {
            _pages.put(page, items);
            countItems();
        }

        if (_loadingPages.contains(page)) {
            _loadingPages.remove(page);
        }

        if (_loadingPages.size() == 0 && _listener != null) {
            _listener.onLoadingComplete();
        }

        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void refreshPages() {
        for (int i = 0; i <= _pages.size(); i++) {
            preRequestPage(i, false);
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
        // Log.v(TAG, "setNoMorePages()");
        _noMorePages = true;
        notifyDataSetChanged();
        if (_listener != null) {
            _listener.onLoadingComplete();
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // Log.v(TAG, "registerDataSetObserver()");
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
        for (int i = 0; i < _pages.size(); i++) {
            page = _pages.get(i);
            count = count + page.size();
            if (count > position)
                break;
        }

        // if  not last page, and there could be more pages then request the next page
        if (_pages.get(_pages.size() - 1) == page && !_noMorePages) {
            preRequestPage(_pages.size(), true);
        }

        // find the begining of the page
        count = count - page.size();
        // pull out the item
        return page.get(position - count);
    }

    @Override
    public long getItemId(int position) {
//        Log.v(TAG, "getItemId()");
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
            return _rateMeView;
        } else if (position > _rateMePosition && _showRateMe) {
            position--;
        }

        int count = 0;
        List<T> page = null;
        int page_num = 0;
//        Log.v(TAG, "_pages:" + _pages.size());
        for (page_num = 0; page_num < _pages.size(); page_num++) {
            page = _pages.get(page_num);
            if (page == null) {
//                Log.v(TAG, "page is null");
                continue;
            }
            count = count + page.size();
            if (count > position)
                break;
        }

        // if  not last page, and there could be more pages then request the next page
        if (_pages.get(_pages.size() - 1) == page && !_noMorePages) {
            preRequestPage(_pages.size(), true);
        }


        // find the begining of the page
        count = count - page.size();
        // get the page
        T obj = page.get(position - count);

        // create the view
        return getView(page_num, position - count, obj, convertView, parent);
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

    public abstract View getView(int page, int position, T object, View convertView, ViewGroup parent);

    public abstract void requestPage(int page, boolean allowCache);

    public interface Listener {
        void onLoadingComplete();
    }

}
