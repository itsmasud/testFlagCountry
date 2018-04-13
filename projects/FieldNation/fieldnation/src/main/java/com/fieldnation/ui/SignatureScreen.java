package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignatureScreen extends RelativeLayout {
    private static final String TAG = "SignatureScreen";

    // Ui
    private FnToolBarView _fnToolbarView;
    private EditText _nameEditText;
    private SignatureView _signatureView;
    private Button _clearButton;
    private Button _backButton;
    private Button _submitButton;

    // Data
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public SignatureScreen(Context context) {
        super(context);
        init();
    }

    public SignatureScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_signature, this);

        if (isInEditMode()) return;

        _fnToolbarView = findViewById(R.id.fnToolbar);
        _fnToolbarView.getToolbar().setNavigationIcon(null);
        _fnToolbarView.getToolbar().setVisibility(GONE);

        _nameEditText = findViewById(R.id.name_edittext);
        _signatureView = findViewById(R.id.signature_view);

        _clearButton = findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);

        _backButton = findViewById(R.id.back_button);
        _backButton.setOnClickListener(_back_onClick);

        _submitButton = findViewById(R.id.submit_button);
        _submitButton.setOnClickListener(_submit_onClick);

    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _signatureView.clear();
        }
    };

    private final View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onBack();
        }
    };

    private final View.OnClickListener _submit_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (misc.isEmptyOrNull(_nameEditText.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    // TODO flash name thing
                    _nameEditText.requestFocus();
                } else {
                    _listener.onSubmit(_nameEditText.getText().toString(), _signatureView.getSignatureSvg());
                }
            }
        }
    };

    public interface Listener {
        void onBack();

        void onSubmit(String name, String signatureJson);
    }
}
