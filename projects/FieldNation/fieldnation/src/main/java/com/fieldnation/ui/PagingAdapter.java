package com.fieldnation.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by michael.carver on 11/21/2014.
 */
public abstract class PagingAdapter<T> extends BaseAdapter {

    private boolean _noMorePages = false;
    private List<List<T>> _pages = new LinkedList<List<T>>();
    private Set<Integer> _loadingPages = new HashSet<Integer>();
    private int _size;


    public void addPage(int page, List<T> items) {
        _pages.add(items);
        _size += items.size();

        if (_loadingPages.contains(page)) {
            _loadingPages.remove(page);
        }

        notifyDataSetChanged();
    }

    public void setNoMorePages() {
        _noMorePages = true;
        notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        if (!_noMorePages && !_loadingPages.contains(_pages.size())) {
            requestPage(_pages.size());
        }
    }

    @Override
    public int getCount() {
        return _size;
    }

    @Override
    public Object getItem(int position) {
        int count = 0;
        List<T> page = null;
        for (int i = 0; i < _pages.size(); i++) {
            page = _pages.get(i);
            count = count + page.size();
            if (count > position)
                break;
        }

        if (_pages.get(_pages.size() - 1) == page && !_noMorePages && !_loadingPages.contains(_pages.size())) {
            _loadingPages.add(_pages.size());
            requestPage(_pages.size());
        }

        count = count - page.size();
        return page.get(position - count);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int count = 0;
        List<T> page = null;
        int page_num = 0;
        for (page_num = 0; page_num < _pages.size(); page_num++) {
            page = _pages.get(page_num);
            count = count + page.size();
            if (count > position)
                break;
        }

        if (_pages.get(_pages.size() - 1) == page && !_noMorePages && !_loadingPages.contains(_pages.size())) {
            _loadingPages.add(page.size());
            requestPage(_pages.size());
        }

        count = count - _pages.size();
        T obj = page.get(position - count);

        return getView(page_num, position - count, obj, convertView, parent);
    }

    public abstract View getView(int page, int position, T object, View convertView, ViewGroup parent);

    public abstract void requestPage(int page);

}
