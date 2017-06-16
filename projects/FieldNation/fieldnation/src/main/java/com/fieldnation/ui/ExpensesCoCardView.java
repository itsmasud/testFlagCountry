package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Expense;

/**
 * Created by shoaib.ahmed on 06/09/2017.
 */
public class ExpensesCoCardView extends RelativeLayout {
    private static final String TAG = "ExpensesCoCardView";

    // Ui
    private EditText _descriptionEditText;
    private EditText _amountEditText;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/

    public ExpensesCoCardView(Context context) {
        super(context);
        init();
    }

    public ExpensesCoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpensesCoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_expenses_card, this);

        if (isInEditMode())
            return;

        _descriptionEditText = (EditText) findViewById(R.id.description_edittext);
        _amountEditText = (EditText) findViewById(R.id.amount_edittext);
    }

    public Expense getExpense() {
        try {
            return new Expense().description(getDescription()).amount(getAmount());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public boolean isValidExpense() {
        return !misc.isEmptyOrNull(getDescription()) && getAmount() != 0;
    }

    public void setExpense(Expense expense) {
        _descriptionEditText.setText(expense.getDescription());
        _amountEditText.setText(expense.getAmount().toString());
    }


    private String getDescription() {
        return _descriptionEditText.getText().toString().trim();
    }

    private Double getAmount() {
        try {
            return Double.parseDouble(_amountEditText.getText().toString());
        } catch (Exception ex) {
            return 0.0;
        }
    }

}
