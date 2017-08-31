package com.fieldnation.ui.workorder;

import android.support.v4.app.Fragment;

import com.fieldnation.v2.data.model.WorkOrder;

public abstract class WorkorderFragment extends Fragment {
    protected LoadingListener _loadingListener;


    public abstract void update();

    public abstract void setWorkOrder(WorkOrder workorder);

    public void setLoadingListener(LoadingListener listener) {
        _loadingListener = listener;
    }

    public abstract void setLoading(boolean isLoading);

    public void dispatchLoading(boolean isLoading) {
        if (_loadingListener != null)
            _loadingListener.setLoading(isLoading);
    }

    public interface LoadingListener {
        void setLoading(boolean isLoading);
    }

}
