package com.fieldnation.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.R;

/**
 * Created by michael.carver on 12/3/2014.
 */
public class ThankYouFragment extends FragmentBase {
    private static final String TAG = "ui.ThankYouFragment";

    // Ui
    private Button _doneButton;

    // Data
    private Handler _handler;
    private Listener _listener;
    private Bitmap _summary;
    private Bitmap _signature;
    private Bitmap _combined;
    private boolean _combinedComplete = false;
    private boolean _timerComplete = false;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static ThankYouFragment getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ThankYouFragment.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_thank_you, container, false);

        _doneButton = (Button) v.findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_done_onClick);

        _handler = new Handler();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        _doneButton.setVisibility(View.GONE);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDoneButton();
            }
        }, 10000);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setImages(Bitmap summary, Bitmap signature) {
        _summary = summary;
        _signature = signature;
        _imageManipulator.executeEx(null);
        Log.v(TAG, "BP");
    }

    private AsyncTaskEx<Object, Object, Object> _imageManipulator = new AsyncTaskEx<Object, Object, Object>() {
        @Override
        protected Object doInBackground(Object... params) {

            int width = _summary.getWidth();
            int height = _summary.getHeight();

            if (_signature.getWidth() > width)
                width = _signature.getWidth();

            height += _signature.getHeight();

            _combined = Bitmap.createBitmap(width, height, _signature.getConfig());
            Canvas canvas = new Canvas(_combined);
            canvas.drawBitmap(_summary, 0, 0, null);
            canvas.drawBitmap(_signature, 0, _summary.getHeight(), null);

            _signature.recycle();
            _summary.recycle();

            // upload deliverable

            _combinedComplete = true;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            showDoneButton();
        }
    };

    private void showDoneButton() {
        if (!_combinedComplete)
            return;

        if (!_timerComplete)
            return;

        if (_doneButton != null) {
            _doneButton.setVisibility(View.VISIBLE);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private View.OnClickListener _done_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _listener.onDoneClick();
        }
    };

    public interface Listener {
        public void onDoneClick();
    }

}