package com.fieldnation.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.EditTextAlertDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;

import java.util.Calendar;

/*-
 * 
 * Based on code from:
 * http://www.intertech.com/Blog/android-gestureoverlayview-to-capture-a-quick-signature-or-drawing/
 */

public class SignatureActivity extends FragmentActivity {
    private static final String TAG = "SignatureActivity";

    public static final String INTENT_KEY_BITMAP = "com.fieldnation.ui.SignatureActivity:SIGNATURE";
    public static final String INTENT_KEY_ARRIVAL = "com.fieldnation.ui.SignatureActivity:ARRIVAL";
    public static final String INTENT_KEY_DEPARTURE = "com.fieldnation.ui.SignatureActivity:DEPARTURE";
    public static final String INTENT_KEY_NAME = "com.fieldnation.ui.SignatureActivity:NAME";

    // UI
    private SignatureView _sigView;
    private TextView _arrivalTextView;
    private TextView _departureTextView;
    private TextView _nameTextView;
    private LinearLayout _arrivalLayout;
    private LinearLayout _departureLayout;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;
    private EditTextAlertDialog _textDialog;
    private Button _okButton;
    private Button _clearButton;

    // Data
    private String _name;
    private Calendar _arriveCal = Calendar.getInstance();
    private Calendar _departCal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        _arriveCal.set(Calendar.SECOND, 0);
        _departCal.set(Calendar.SECOND, 0);

        setContentView(R.layout.activity_signature);

        _sigView = findViewById(R.id.sig_view);
        _arrivalTextView = findViewById(R.id.arrival_textview);
        _arrivalLayout = findViewById(R.id.arrival_layout);
        _arrivalLayout.setOnClickListener(_arrival_onClick);
        _departureTextView = findViewById(R.id.departure_textview);
        _departureLayout = findViewById(R.id.departure_layout);
        _departureLayout.setOnClickListener(_departure_onClick);
        _nameTextView = findViewById(R.id.name_textview);
        _nameTextView.setOnClickListener(_name_onClick);
        _okButton = findViewById(R.id.done_button);
        _okButton.setOnClickListener(_ok_onClick);
        _clearButton = findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey(INTENT_KEY_ARRIVAL)) {
                String arrivalTime = bundle.getString(INTENT_KEY_ARRIVAL);
                if (!misc.isEmptyOrNull(arrivalTime)) {
                    try {
                        _arriveCal = ISO8601.toCalendar(arrivalTime);
                    } catch (Exception ex) {
                        _arriveCal = Calendar.getInstance();
                    }
                    _arrivalTextView.setText(DateUtils.formatDateLong(_arriveCal) + " " + DateUtils.formatTime(_arriveCal, false));
                }
            }
            if (bundle.containsKey(INTENT_KEY_DEPARTURE)) {
                String depatureTime = bundle.getString(INTENT_KEY_DEPARTURE);
                _departCal = Calendar.getInstance();
                if (!misc.isEmptyOrNull(depatureTime)) {
                    try {
                        _departCal = ISO8601.toCalendar(depatureTime);
                    } catch (Exception ex) {
                        _departCal = Calendar.getInstance();
                    }
                }
                _departureTextView.setText(DateUtils.formatDateLong(_departCal) + " " + DateUtils.formatTime(_departCal, false));
            }
            if (bundle.containsKey(INTENT_KEY_NAME)) {
                _name = bundle.getString(INTENT_KEY_NAME);
                if (!misc.isEmptyOrNull(_name))
                    _nameTextView.setText(_name);
            }
        }

        final Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        _datePicker = new DatePickerDialog(this, _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(this, _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.signature, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.done_menuitem:
//                onDone();
//                return true;
//            case R.id.clear_menuitem:
//                _sigView.clear();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            byte[] signature = _sigView.getSignatureSvg().getBytes();

            Log.v(TAG, "Sig size: " + signature.length);

            // validate input and ask for it
            if (misc.isEmptyOrNull(_name)) {
                _textDialog = new EditTextAlertDialog(SignatureActivity.this, "Signee Name", "Please enter the signee name");
                _textDialog.setPositiveButton("Ok", _editText_onOk_onClose);
                _textDialog.show();

                return;
            }

            Intent intent = new Intent();
            intent.putExtras(getIntent());
            intent.putExtra(INTENT_KEY_BITMAP, signature);
            intent.putExtra(INTENT_KEY_ARRIVAL, ISO8601.fromCalendar(_arriveCal));
            intent.putExtra(INTENT_KEY_DEPARTURE, ISO8601.fromCalendar(_departCal));
            intent.putExtra(INTENT_KEY_NAME, _name);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private final View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _sigView.clear();
        }
    };

    private final DialogInterface.OnClickListener _editText_onOk = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _name = _textDialog.getInput();
            if (misc.isEmptyOrNull(_name.trim())) {
                _nameTextView.setText(R.string.activity_signature_name);
            } else {
                _nameTextView.setText(_name);
            }
        }
    };

    private final DialogInterface.OnClickListener _editText_onOk_onClose = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _name = _textDialog.getInput();
            if (misc.isEmptyOrNull(_name.trim())) {
                _nameTextView.setText(R.string.activity_signature_name);
            } else {
                _nameTextView.setText(_name);
            }
            _ok_onClick.onClick(null);
        }
    };

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String tag = (String) _datePicker.getTag();
            if (tag == null) return;
            if (tag.equals("arrive")) {
                _arriveCal.set(year, monthOfYear, dayOfMonth);
            } else if (tag.equals("depart")) {
                _departCal.set(year, monthOfYear, dayOfMonth);
            }

            _timePicker.setTag(_datePicker.getTag());
            _timePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String tag = (String) _timePicker.getTag();
            if (tag == null) return;
            if (tag.equals("arrive")) {
                _arriveCal.set(_arriveCal.get(Calendar.YEAR), _arriveCal.get(Calendar.MONTH),
                        _arriveCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _arrivalTextView.setText(DateUtils.formatDateLong(_arriveCal) + " " + DateUtils.formatTime(_arriveCal, false));
            } else if (tag.equals("depart")) {
                _departCal.set(_departCal.get(Calendar.YEAR), _departCal.get(Calendar.MONTH),
                        _departCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _departureTextView.setText(DateUtils.formatDateLong(_departCal) + " " + DateUtils.formatTime(_departCal, false));
            }
        }
    };

    private final View.OnClickListener _arrival_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.setTag("arrive");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _departure_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            _datePicker.setTag("depart");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _name_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            _textDialog = new EditTextAlertDialog(SignatureActivity.this, "Signee Name", "Please enter the signee name");
            _textDialog.setPositiveButton("Ok", _editText_onOk);
            _textDialog.show();
        }
    };

}
