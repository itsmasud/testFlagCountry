package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.StarView;
import com.fieldnation.v2.data.model.WorkOrder;

import java.lang.ref.WeakReference;

/**
 * Created by shoaib.ahmed on 07/28/2016.
 */
public class RateBuyerDialog extends FullScreenDialog {
    private static final String TAG = "RateBuyerDialog";

    // State
    private static final String PARAM_WORKORDER = "workOrder";
    private static final String STATE_GOLD_STAR = "RateBuyerDialog:STATE_GOLD_STAR";
    private static final String STATE_SCOPE_RATING = "RateBuyerDialog:STATE_SCOPE_RATING";
    private static final String STATE_RESPECT_RATING = "RateBuyerDialog:STATE_RESPECT_RATING";
    private static final String STATE_COMMENT_TEXT = "RateBuyerDialog:STATE_COMMENT_TEXT";

    private static final String INTENT_WORKORDER = "INTENT_WORKORDER";

    // UI
    private TextView _titleTextView;
    private ProfilePicView _picView;
    private TextView _companyNameTextView;
    private TextView _locationTextView;
    private StarView _rateStarView;
    private TextView _expectationNoTextView;
    private TextView _expectationYesTextView;
    private TextView _chkProfessionalNoTextView;
    private TextView _chkProfessionalYesTextView;
    private EditText _otherThoughtsEditText;

    private Button _submitButton;
    private Button _cancelButton;

    // Data
    private boolean _clear = false;
    private WorkOrder _workOrder;
    private Listener _listener;
    //    private final int MAX_THOUGHTS_LENGTH = 120;
    private boolean _hasToastShown = false;
    private int _goldStar = 0;
    private Boolean _hasSelectedScopeRating = null;
    private Boolean _hasSelectedRespectRating = null;
    private String _commentText;
    private PhotoClient _photos;
    private WeakReference<Drawable> _profilePic = null;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public RateBuyerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_rate_buyer, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _rateStarView = (StarView) v.findViewById(R.id.star_rating);
        _picView = (ProfilePicView) v.findViewById(R.id.pic_view);
        _companyNameTextView = (TextView) v.findViewById(R.id.company_name_textview);
        _locationTextView = (TextView) v.findViewById(R.id.location_textview);
        _expectationNoTextView = (TextView) v.findViewById(R.id.expectation_no_textview);
        _expectationYesTextView = (TextView) v.findViewById(R.id.expectation_yes_textview);
        _chkProfessionalNoTextView = (TextView) v.findViewById(R.id.chk_professional_no_textview);
        _chkProfessionalYesTextView = (TextView) v.findViewById(R.id.chk_professinal_yes_textview);
        _otherThoughtsEditText = (EditText) v.findViewById(R.id.other_thoughts_edittext);
        _submitButton = (Button) v.findViewById(R.id.submit_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();

        _picView.setProfilePic(R.drawable.missing_circle);
        _expectationNoTextView.setOnClickListener(expectation_onClick_listener);
        _expectationYesTextView.setOnClickListener(expectation_onClick_listener);
        _chkProfessionalNoTextView.setOnClickListener(chkProfession_onClick_listener);
        _chkProfessionalYesTextView.setOnClickListener(chkProfession_onClick_listener);
        _otherThoughtsEditText.addTextChangedListener(_textEditText_watcherListener);
        _submitButton.setOnClickListener(_submit_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);

    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        if (_clear) {
            _otherThoughtsEditText.setText("");
        } else {

            _rateStarView.setStars(_goldStar);

            if (_hasSelectedScopeRating != null) {
                if (_hasSelectedScopeRating) {
                    _expectationNoTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                    _expectationYesTextView.setBackgroundResource(R.drawable.circle_green);
                } else {
                    _expectationNoTextView.setBackgroundResource(R.drawable.circle_red);
                    _expectationYesTextView.setBackgroundResource(R.drawable.circle_dark_gray);

                }
            }

            if (_hasSelectedRespectRating != null) {
                if (_hasSelectedRespectRating) {
                    _chkProfessionalNoTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                    _chkProfessionalYesTextView.setBackgroundResource(R.drawable.circle_green);
                } else {
                    _chkProfessionalNoTextView.setBackgroundResource(R.drawable.circle_red);
                    _chkProfessionalYesTextView.setBackgroundResource(R.drawable.circle_dark_gray);

                }
            }

            if (!misc.isEmptyOrNull(_commentText)) {
                _otherThoughtsEditText.setText(_commentText);
            }

            if (_hasSelectedScopeRating != null && _hasSelectedRespectRating != null & _goldStar > 0)
                _submitButton.setEnabled(true);
            else
                _submitButton.setEnabled(false);
        }

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());

        populateUi();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        Log.v(TAG, "show");
        _workOrder = payload.getParcelable(PARAM_WORKORDER);
        super.show(payload, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        if (savedState != null) {
            if (savedState.containsKey(STATE_GOLD_STAR))
                _goldStar = savedState.getInt(STATE_GOLD_STAR);

            if (savedState.containsKey(STATE_SCOPE_RATING))
                _hasSelectedScopeRating = savedState.getBoolean(STATE_SCOPE_RATING);

            if (savedState.containsKey(STATE_RESPECT_RATING))
                _hasSelectedRespectRating = savedState.getBoolean(STATE_RESPECT_RATING);

            if (savedState.containsKey(STATE_COMMENT_TEXT))
                _commentText = savedState.getString(STATE_COMMENT_TEXT);
        }

        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        if (_rateStarView.getNumberOfGoldStar() != 0)
            outState.putInt(STATE_GOLD_STAR, _rateStarView.getNumberOfGoldStar());

        if (_hasSelectedScopeRating != null)
            outState.putBoolean(STATE_SCOPE_RATING, _hasSelectedScopeRating);

        if (_hasSelectedRespectRating != null)
            outState.putBoolean(STATE_RESPECT_RATING, _hasSelectedRespectRating);

        if (_commentText != null)
            outState.putString(STATE_COMMENT_TEXT, _commentText);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        if (_photos != null && _photos.isConnected()) {
            _photos.disconnect(App.get());
        }
        super.onPause();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_workOrder == null) return;

        _rateStarView.setListener(_startView_onClick);
        _rateStarView.setChangeEnabled(true);
        _rateStarView.setStarFontSize(getView().getResources().getInteger(R.integer.textSizeBuyerRatingStar));

        _titleTextView.setText(getView().getResources().getString(R.string.dialog_rate_buyer_title, _workOrder.getId()));

        if (_workOrder.getCompany() != null
                && _workOrder.getCompany().getName() != null) {
            _companyNameTextView.setText(_workOrder.getCompany().getName());
        }

        // sometimes city/ state info is not put into server
        if (_workOrder.getLocation() != null) {
            String locationName = null;
            if (!misc.isEmptyOrNull(_workOrder.getLocation().getCity()))
                locationName = _workOrder.getLocation().getCity();
            if (!misc.isEmptyOrNull(_workOrder.getLocation().getState()))
                locationName = misc.isEmptyOrNull(locationName) ? "" : locationName + ", " + _workOrder.getLocation().getState();
            if (!misc.isEmptyOrNull(locationName))
                _locationTextView.setText(locationName);
            else _locationTextView.setVisibility(View.GONE);
        }

        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _workOrder.getCompany().getPhoto();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(App.get(), url, true, false);
                _photos.subGet(url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }

        _otherThoughtsEditText.post(new Runnable() {
            @Override
            public void run() {
                misc.hideKeyboard(_otherThoughtsEditText);
            }
        });
    }

    private final View.OnClickListener expectation_onClick_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(_expectationNoTextView)) {
                _expectationNoTextView.setBackgroundResource(R.drawable.circle_red);
                _expectationYesTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                _hasSelectedScopeRating = false;
            }

            if (v.equals(_expectationYesTextView)) {
                _expectationYesTextView.setBackgroundResource(R.drawable.circle_green);
                _expectationNoTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                _hasSelectedScopeRating = true;
            }

            if (_hasSelectedScopeRating != null && _hasSelectedRespectRating != null & _goldStar > 0)
                _submitButton.setEnabled(true);
            else
                _submitButton.setEnabled(false);
        }
    };

    private final View.OnClickListener chkProfession_onClick_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(_chkProfessionalNoTextView)) {
                _chkProfessionalNoTextView.setBackgroundResource(R.drawable.circle_red);
                _chkProfessionalYesTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                _hasSelectedRespectRating = false;
            }

            if (v.equals(_chkProfessionalYesTextView)) {
                _chkProfessionalYesTextView.setBackgroundResource(R.drawable.circle_green);
                _chkProfessionalNoTextView.setBackgroundResource(R.drawable.circle_dark_gray);
                _hasSelectedRespectRating = true;
            }

            if (_hasSelectedScopeRating != null && _hasSelectedRespectRating != null & _goldStar > 0)
                _submitButton.setEnabled(true);
            else
                _submitButton.setEnabled(false);

        }
    };

    private final TextWatcher _textEditText_watcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            _commentText = _otherThoughtsEditText.getText().toString().trim();
//            int numberOfCharacter = _commentText.length();
//            if (numberOfCharacter > 0) {
//                if (numberOfCharacter >= MAX_THOUGHTS_LENGTH && !_hasToastShown) {
//                    ToastClient.toast(App.get(), getString(R.string.toast_exceeded_max_limit_thoughts), Toast.LENGTH_LONG);
//                    _hasToastShown = true;
//                } else {
//                    _hasToastShown = false;
//                }
//            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final PhotoClient.Listener _photo_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
            populateUi();
        }

        @Override
        public void onGet(String url, BitmapDrawable bitmapDrawable, boolean isCircle, boolean failed) {
            if (bitmapDrawable == null) {
                _picView.setProfilePic(R.drawable.missing_circle);
                return;
            }

            Drawable pic = bitmapDrawable;
            _profilePic = new WeakReference<>(pic);
            _picView.setProfilePic(pic);
        }
    };

    private final View.OnClickListener _submit_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            WorkorderClient.sendRating(App.get(), _workOrder.getId(),
                    _goldStar, _hasSelectedScopeRating == true ? 1 : 0,
                    _hasSelectedRespectRating == true ? 1 : 0, _commentText);
            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final StarView.Listener _startView_onClick = new StarView.Listener() {
        @Override
        public void onClick(int goldStar) {
            _goldStar = goldStar;

            if (_hasSelectedScopeRating != null && _hasSelectedRespectRating != null & _goldStar > 0)
                _submitButton.setEnabled(true);
            else
                _submitButton.setEnabled(false);
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workOrder);

        Controller.show(context, uid, RateBuyerDialog.class, params);
    }

    private interface Listener {
        void onOk(long workorderId, int satisfactionRating, int scopeRating,
                  int respectRating, String otherComments);
    }
}