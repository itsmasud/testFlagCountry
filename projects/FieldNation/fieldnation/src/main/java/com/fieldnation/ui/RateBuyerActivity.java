package com.fieldnation.ui;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;

import java.lang.ref.WeakReference;

/**
 * Created by shoaib.ahmed on 07/28/2016.
 */
public class RateBuyerActivity extends AuthFragmentActivity {
    private static final String TAG = "RateBuyerActivity";

    // State
    private static final String STATE_WORKORDER = "RateBuyerActivity:STATE_WORKORDER";
    private static final String STATE_GOLD_STAR = "RateBuyerActivity:STATE_GOLD_STAR";
    private static final String STATE_SCOPE_RATING = "RateBuyerActivity:STATE_SCOPE_RATING";
    private static final String STATE_RESPECT_RATING = "RateBuyerActivity:STATE_RESPECT_RATING";
    private static final String STATE_COMMENT_TEXT = "RateBuyerActivity:STATE_COMMENT_TEXT";

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
    private Workorder _workorder;
    private Listener _listener;
    private final int MAX_THOUGHTS_LENGTH = 120;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        setContentView(R.layout.activity_rate_buyer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_WORKORDER))
                _workorder = extras.getParcelable(INTENT_WORKORDER);
        }

        _titleTextView = (TextView) findViewById(R.id.title_textview);

        _rateStarView = (StarView) findViewById(R.id.star_rating);

        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);

        _companyNameTextView = (TextView) findViewById(R.id.company_name_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);

        _expectationNoTextView = (TextView) findViewById(R.id.expectation_no_textview);
        _expectationNoTextView.setOnClickListener(expectation_onClick_listener);
        _expectationYesTextView = (TextView) findViewById(R.id.expectation_yes_textview);
        _expectationYesTextView.setOnClickListener(expectation_onClick_listener);

        _chkProfessionalNoTextView = (TextView) findViewById(R.id.chk_professional_no_textview);
        _chkProfessionalNoTextView.setOnClickListener(chkProfession_onClick_listener);
        _chkProfessionalYesTextView = (TextView) findViewById(R.id.chk_professinal_yes_textview);
        _chkProfessionalYesTextView.setOnClickListener(chkProfession_onClick_listener);

        _otherThoughtsEditText = (EditText) findViewById(R.id.other_thoughts_edittext);
        _otherThoughtsEditText.addTextChangedListener(_textEditText_watcherListener);

        _submitButton = (Button) findViewById(R.id.submit_button);
        _submitButton.setOnClickListener(_submit_onClick);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_GOLD_STAR))
                _goldStar = savedInstanceState.getInt(STATE_GOLD_STAR);

            if (savedInstanceState.containsKey(STATE_SCOPE_RATING))
                _hasSelectedScopeRating = savedInstanceState.getBoolean(STATE_SCOPE_RATING);

            if (savedInstanceState.containsKey(STATE_RESPECT_RATING))
                _hasSelectedRespectRating = savedInstanceState.getBoolean(STATE_RESPECT_RATING);

            if (savedInstanceState.containsKey(STATE_COMMENT_TEXT))
                _commentText = savedInstanceState.getString(STATE_COMMENT_TEXT);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        if (_rateStarView.getNumberOfGoldStar() != 0)
            outState.putInt(STATE_GOLD_STAR, _rateStarView.getNumberOfGoldStar());

        if (_hasSelectedScopeRating != null)
            outState.putBoolean(STATE_SCOPE_RATING, _hasSelectedScopeRating);

        if (_hasSelectedRespectRating != null)
            outState.putBoolean(STATE_RESPECT_RATING, _hasSelectedRespectRating);

        if (_commentText != null)
            outState.putString(STATE_COMMENT_TEXT, _commentText);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        _submitButton.requestFocus();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
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
    protected void onStop() {
        _clear = true;
        super.onStop();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_workorder == null) return;

        _rateStarView.setListener(_startView_onClick);
        _rateStarView.setChangeEnabled(true);
        _rateStarView.setStarFontSize(getResources().getInteger(R.integer.textSizeBuyerRatingStar));

        _titleTextView.setText(getString(R.string.dialog_rate_buyer_title, _workorder.getWorkorderId()));

        if (_workorder.getCompanyName() != null) {
            _companyNameTextView.setText(_workorder.getCompanyName());
        }

        // sometimes city/ state info is not put into server
        if (_workorder.getLocation() != null) {
            String locationName = null;
            if (!misc.isEmptyOrNull(_workorder.getLocation().getCity()))
                locationName = _workorder.getLocation().getCity();
            if (!misc.isEmptyOrNull(_workorder.getLocation().getState()))
                locationName = misc.isEmptyOrNull(locationName) ? "" : locationName + ", " + _workorder.getLocation().getState();
            if (!misc.isEmptyOrNull(locationName))
                _locationTextView.setText(locationName);
            else _locationTextView.setVisibility(View.GONE);
        }

        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _workorder.getWorkorderManagerInfo().getPhotoThumbUrl();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(App.get(), url, true, false);
                _photos.subGet(url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
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
            int numberOfCharacter = _commentText.length();
            if (numberOfCharacter > 0) {
                if (numberOfCharacter >= MAX_THOUGHTS_LENGTH && !_hasToastShown) {
                    ToastClient.toast(App.get(), getString(R.string.toast_exceeded_max_limit_thoughts), Toast.LENGTH_LONG);
                    _hasToastShown = true;
                } else {
                    _hasToastShown = false;
                }
            }
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

            WorkorderClient.sendRating(App.get(), _workorder.getWorkorderId(),
                    _goldStar, _hasSelectedScopeRating == true ? 1 : 0,
                    _hasSelectedRespectRating == true ? 1 : 0, _commentText);
            onBackPressed();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
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


    private interface Listener {
        void onOk(long workorderId, int satisfactionRating, int scopeRating,
                  int respectRating, String otherComments);
    }
}
