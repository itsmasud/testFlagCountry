package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 7/11/17.
 */

public class ListItemTwoVertView extends RelativeLayout {
    private static final String TAG = "ListItemTwoVertView";

    // Ui
    private TextView _keyTextView;
    private IconFontTextView _keyIconView;
    private TextView _valueTextView;
    private TextView _actionTextView;
    private TextView _alertTextView;
    private ProgressBar _progressBar;

    // Data
    private String _key;
    private String _value;
    private String _action;
    private String _iconText = null;
    private String _alertText = null;
    private int _iconTextColor = -1;
    private boolean _actionVisible = false;
    private boolean _alertVisible = false;
    private boolean _progressVisible = false;
    private boolean _isOffline = false;
    private int _progress = -1;

    // Listener
    private OnActionClickListener _actionOnclickListener;

    public ListItemTwoVertView(Context context) {
        super(context);
        init();
    }

    public ListItemTwoVertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemTwoVertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_two_line_action, this);

        if (isInEditMode())
            return;

        _keyTextView = findViewById(R.id.key);
        _keyIconView = findViewById(R.id.key_iconView);
        _valueTextView = findViewById(R.id.value);
        _actionTextView = findViewById(R.id.action);
        _actionTextView.setOnClickListener(_action_onClick);
        _alertTextView = findViewById(R.id.alert);
        _progressBar = findViewById(R.id.progressBar);

        populateUi();
    }

    public void setActionVisible(boolean visible) {
        _actionVisible = visible;
        populateUi();
    }

    public void setActionString(String action) {
        _action = action;
        _actionVisible = true;
        populateUi();
    }

    public void setAlertVisible(boolean visible) {
        _alertVisible = visible;

        populateUi();
    }

    public void setOffline(boolean isOffline) {
        _isOffline = isOffline;
    }

    public void setIcon(String iconText, int iconTextColor) {
        _iconText = iconText;
        _iconTextColor = iconTextColor;

        populateUi();
    }


    public void setProgressVisible(boolean visible) {
        _progressVisible = visible;
        populateUi();
    }

    public void setProgress(int progress) {
        _progress = progress;
        populateUi();
    }

    public void setOnActionClickedListener(OnActionClickListener onActionClickedListener) {
        _actionOnclickListener = onActionClickedListener;
    }

    public void set(String key, String value) {
        _key = key;
        _value = value;
        populateUi();
    }

    public void set(String key, String value, boolean isOffline) {
        _key = key;
        _value = value;
        _isOffline = isOffline;
        populateUi();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _keyTextView.setEnabled(enabled);
        _keyIconView.setEnabled(enabled);
        _valueTextView.setEnabled(enabled);
        _actionTextView.setEnabled(enabled);
        _alertTextView.setEnabled(enabled);
        _progressBar.setEnabled(enabled);
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }

    private void populateUi() {
        if (_keyTextView == null)
            return;

        if (misc.isEmptyOrNull(_key)) {
            _keyTextView.setText("");
        } else {
            _keyTextView.setText(_key);
        }

        if (misc.isEmptyOrNull(_value)) {
            _valueTextView.setVisibility(GONE);
        } else {
            _valueTextView.setVisibility(VISIBLE);
            _valueTextView.setText(_value);
        }

        if (_actionVisible) {
            _actionTextView.setVisibility(VISIBLE);
            if (misc.isEmptyOrNull(_action)) {
                _actionTextView.setText(R.string.icon_x);
            } else {
                _actionTextView.setText(_action);
                _actionTextView.setVisibility(VISIBLE);
            }
        } else {
            _actionTextView.setVisibility(GONE);
        }

        if (_progressVisible) {
            _progressBar.setVisibility(VISIBLE);
            if (_progress == -1) {
                _progressBar.setIndeterminate(true);
            } else {
                _progressBar.setIndeterminate(false);
                _progressBar.setProgress(_progress);
            }
        } else {
            _progressBar.setVisibility(GONE);
        }

        if (misc.isEmptyOrNull(_iconText))
            _keyIconView.setVisibility(GONE);
        else {
            _keyIconView.setVisibility(VISIBLE);
            _keyIconView.setText(_iconText);
        }

        if (_iconTextColor != -1)
            _keyIconView.setTextColor(_iconTextColor);

        // offline logic
        if (_isOffline) {
            _alertText = _alertTextView.getResources().getString(R.string.icon_cloud_off);
            if (_keyIconView.getVisibility() == VISIBLE)
                _keyIconView.setTextColor(getResources().getColor(R.color.fn_dark_text_50));
            _alertTextView.setTextColor(getResources().getColor(R.color.fn_dark_text_50));
        }

        if (!misc.isEmptyOrNull(_alertText))
            _alertTextView.setText(_alertText);

        if (_alertVisible || _isOffline) {
            _alertTextView.setVisibility(VISIBLE);
        } else {
            _alertTextView.setVisibility(GONE);
        }
    }

    private final OnClickListener _action_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
            if (_actionOnclickListener != null)
                _actionOnclickListener.onClick(ListItemTwoVertView.this, view);
        }
    };

    public interface OnActionClickListener {
        void onClick(View twoLineActionTile, View actionView);
    }
}
