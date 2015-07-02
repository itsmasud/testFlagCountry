package com.fieldnation.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by michael.carver on 11/21/2014.
 */
public abstract class PagingAdapter<T> extends BaseAdapter {
    private static final String TAG = "ui.PagingAdapter";

    private boolean _noMorePages = false;
    private TreeMap<Integer, List<T>> _pages = new TreeMap<>();
    private Set<Integer> _loadingPages = new HashSet<>();
    private int _size;
    private Listener _listener;

    public void setPage(int page, List<T> items, boolean isCached) {
        Log.v(TAG, "setPage()");

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

        // request an update if results are cached
        if (isCached)
            preRequestPage(page, false);

        if (_loadingPages.size() == 0 && _listener != null) {
            _listener.onLoadingComplete();
        }

        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void refreshPages() {
        _loadingPages.clear();
        for (int i = 0; i <= _pages.size(); i++) {
            preRequestPage(i, false);
        }
    }

    private void countItems() {
        int count = 0;
        Set<Integer> e = _pages.keySet();
        for (Integer key : e) {
            count += _pages.get(key).size();
            _size = count;
        }
    }

    public void setNoMorePages() {
        Log.v(TAG, "setNoMorePages()");
        _noMorePages = true;
        notifyDataSetChanged();
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
//        Log.v(TAG, "getCount()");
        return _size;
    }

    @Override
    public Object getItem(int position) {
//        Log.v(TAG, "getItem()");

        // find the page that has this item
        int count = 0;
        List<T> page = null;
        Set<Integer> set = _pages.keySet();
        for (Integer s : set) {
            page = _pages.get(s);
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
//        Log.v(TAG, "getView()");

        // find the page
        int count = 0;
        List<T> page = null;
        int page_num = 0;
        Set<Integer> set = _pages.keySet();
        for (Integer s : set) {
            page_num = s;
            page = _pages.get(page_num);
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
//            Log.v(TAG, "preRequestPage(), requesting");
            _loadingPages.add(page);
            requestPage(page, allowCache);
            return;
        }
//        Log.v(TAG, "preRequestPage(), skipping");
    }

    public abstract View getView(int page, int position, T object, View convertView, ViewGroup parent);

    public abstract void requestPage(int page, boolean allowCache);

    public interface Listener {
        void onLoadingComplete();
    }

}
