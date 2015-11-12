package com.fieldnation.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 5/12/2015.
 */
public class ProfilePicView extends RelativeLayout {

    private ImageView _profileImageView;
    private RelativeLayout _unreadView;

    private boolean _alertOn = false;

    public ProfilePicView(Context context) {
        super(context);
        init();
    }

    public ProfilePicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfilePicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_pic, this, true);

        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _unreadView = (RelativeLayout) findViewById(R.id.unread_view);

        setAlertOn(_alertOn);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putBoolean("_alertOn", _alertOn);
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle s = (Bundle) state;
            if (s.containsKey("_alertOn"))
                _alertOn = s.getBoolean("_alertOn");
        }

        setAlertOn(_alertOn);
        super.onRestoreInstanceState(state);
    }

    public void setAlertOn(boolean alert) {
        _alertOn = alert;

        if (_unreadView == null)
            return;

        if (_alertOn) {
            _unreadView.setVisibility(VISIBLE);
        } else {
            _unreadView.setVisibility(GONE);
        }
    }

    public void setProfilePic(int res) {
        _profileImageView.setBackgroundDrawable(null);
        _profileImageView.setBackgroundResource(res);
    }

    public void setProfilePic(Drawable drawable) {
        _profileImageView.setBackgroundDrawable(drawable);
    }
}
