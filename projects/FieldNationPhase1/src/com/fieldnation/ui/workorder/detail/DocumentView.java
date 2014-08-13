package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Document;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DocumentView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.DocumentView";

	// UI
	private ImageView _docTypeImageView;
	private TextView _filenameTextView;
	private TextView _dateTextView;
	private TextView _usernameTextView;
	private ImageButton _deleteButton;

	// Data
	private Document _doc;

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public DocumentView(Context context) {
		super(context);
		init();
	}

	public DocumentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DocumentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_detail_doc, this);

		if (isInEditMode())
			return;

		_docTypeImageView = (ImageView) findViewById(R.id.doctype_imageview);
		_filenameTextView = (TextView) findViewById(R.id.filename_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_usernameTextView = (TextView) findViewById(R.id.username_textview);
		_deleteButton = (ImageButton) findViewById(R.id.delete_imagebutton);

	}

	/*-*************************-*/
	/*-			Methods			-*/
	/*-*************************-*/
	public void setDocument(Document document) {
		_doc = document;
		populateUi();
	}

	private void populateUi() {
		if (_doc == null)
			return;

		_filenameTextView.setText(_doc.getFileName());
		_dateTextView.setText("No time Data");
		_usernameTextView.setText("Username");
		
		
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
}
