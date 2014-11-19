package com.fieldnation.ui.workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderActivity.PageRequestListener;

public abstract class WorkorderFragment extends Fragment {
    protected PageRequestListener pageRequestListener;
    protected LoadingListener _loadingListener;


    public abstract void update();

    public abstract void setWorkorder(Workorder workorder);

    public void setPageRequestListener(PageRequestListener listener) {
        pageRequestListener = listener;
    }

    public void setLoadingListener(LoadingListener listener) {
        _loadingListener = listener;
    }

    public void setLoading(boolean isLoading) {
        if (_loadingListener != null)
            _loadingListener.setLoading(isLoading);
    }

    public abstract void doAction(Bundle bundle);

    public interface LoadingListener {
        public void setLoading(boolean isLoading);
    }

}
