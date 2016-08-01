package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.StarView;
import com.fieldnation.utils.misc;

import java.lang.ref.WeakReference;

/**
 * Created by shoaib.ahmed on 07/28/2016.
 */
public class RateBuyerDialog extends DialogFragmentBase {
    private static final String TAG = "RateBuyerDialog";

    // State
    private static final String STATE_WORKORDER = "RateBuyerDialog:STATE_WORKORDER";
    private static final String STATE_GOLD_STAR = "RateBuyerDialog:STATE_GOLD_STAR";
    private static final String STATE_SCOPE_RATING = "RateBuyerDialog:STATE_SCOPE_RATING";
    private static final String STATE_RESPECT_RATING = "RateBuyerDialog:STATE_RESPECT_RATING";
    private static final String STATE_COMMENT_TEXT = "RateBuyerDialog:STATE_COMMENT_TEXT";

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
    private TextView _characterCountingTextView;

    private Button _submitButton;
    private Button _cancelButton;

    // Data
    private Workorder _workorder;
    private Listener _listener;
    private boolean _clear = false;
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
    public static RateBuyerDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, RateBuyerDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
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
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_rate_buyer, container);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _rateStarView = (StarView) v.findViewById(R.id.star_rating);

        _picView = (ProfilePicView) v.findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);

        _companyNameTextView = (TextView) v.findViewById(R.id.company_name_textview);
        _locationTextView = (TextView) v.findViewById(R.id.location_textview);

        _expectationNoTextView = (TextView) v.findViewById(R.id.expectation_no_textview);
        _expectationNoTextView.setOnClickListener(expectation_onClick_listener);
        _expectationYesTextView = (TextView) v.findViewById(R.id.expectation_yes_textview);
        _expectationYesTextView.setOnClickListener(expectation_onClick_listener);

        _chkProfessionalNoTextView = (TextView) v.findViewById(R.id.chk_professional_no_textview);
        _chkProfessionalNoTextView.setOnClickListener(chkProfession_onClick_listener);
        _chkProfessionalYesTextView = (TextView) v.findViewById(R.id.chk_professinal_yes_textview);
        _chkProfessionalYesTextView.setOnClickListener(chkProfession_onClick_listener);

        _otherThoughtsEditText = (EditText) v.findViewById(R.id.other_thoughts_edittext);
        _otherThoughtsEditText.addTextChangedListener(_textEditText_watcherListener);

        _characterCountingTextView = (TextView) v.findViewById(R.id.character_counting_textview);

        _submitButton = (Button) v.findViewById(R.id.submit_button);
        _submitButton.setOnClickListener(_submit_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void reset() {
        Log.v(TAG, "reset");
        super.reset();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

        if (_clear) {
            _otherThoughtsEditText.setText("");
            _characterCountingTextView.setText(getString(R.string.character_counting, 0));

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
            else _submitButton.setEnabled(false);
        }

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());

        populateUi();
    }

    @Override
    public void dismiss() {
//        Log.e(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        Log.e(TAG, "onDismiss");
        super.onDismiss(dialog);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Workorder workorder) {
        _workorder = workorder;
        super.show();
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
            else _submitButton.setEnabled(false);


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
            else _submitButton.setEnabled(false);

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

                _characterCountingTextView.setText(getString(R.string.character_counting, numberOfCharacter));
            } else {
                _characterCountingTextView.setText(getString(R.string.character_counting, 0));
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

            dismiss();

        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    private final StarView.Listener _startView_onClick = new StarView.Listener() {
        @Override
        public void onClick(int goldStar) {
            _goldStar = goldStar;

            if (_hasSelectedScopeRating != null && _hasSelectedRespectRating != null & _goldStar > 0)
                _submitButton.setEnabled(true);
            else _submitButton.setEnabled(false);
        }
    };


    private interface Listener {
        void onOk(long workorderId, int satisfactionRating, int scopeRating,
                  int respectRating, String otherComments);
    }
}
