package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;
import java.util.Random;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.rpc.client.PhotoService;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageRcvdView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.MessageRcvdView";

	private int WEB_GET_PHOTO = 1;
	// UI
	private TextView _messageTextView;
	private ImageView _profileImageView;
	private TextView _timeTextView;
	private ImageView _checkImageView;

	// Data
	private GlobalState _gs;
	private Message _message = null;
	private PhotoService _service;
	private Random _rand = new Random();

	public MessageRcvdView(Context context) {
		super(context);
		init();
	}

	public MessageRcvdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MessageRcvdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_message_rcvd, this);

		_gs = (GlobalState) getContext().getApplicationContext();

		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_profileImageView = (ImageView) findViewById(R.id.profile_imageview);
		_timeTextView = (TextView) findViewById(R.id.time_textview);
		_checkImageView = (ImageView) findViewById(R.id.check_imageview);

		_service = new PhotoService(_gs, _resultReceiver);
	}

	public void setMessage(Message message) {
		_message = message;

		populateUi();
	}

	private void getPhoto() {
		if (_service == null)
			return;

		if (_message == null)
			return;

		WEB_GET_PHOTO = _rand.nextInt();
		_gs.startService(_service.getPhoto(WEB_GET_PHOTO, _message.getFromUser().getPhotoThumbUrl()));
	}

	private void populateUi() {
		if (_service == null)
			return;

		if (_message == null)
			return;

		_messageTextView.setText(_message.getMessage());
		try {
			_timeTextView.setText(misc.formatMessageTime(ISO8601.toCalendar(_message.getMsgCreateDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		_profileImageView.setBackgroundResource(R.drawable.missing);

		if (_message.getMsgRead() == 0) {
			_checkImageView.setBackgroundResource(R.drawable.ic_message_thumb);
		} else {
			_checkImageView.setBackgroundResource(R.drawable.ic_check);
		}

		getPhoto();
	}

	private ResultReceiver _resultReceiver = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == WEB_GET_PHOTO) {
				byte[] data = resultData.getByteArray(PhotoServiceConstants.KEY_RESPONSE_DATA);
				if (data != null) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					bitmap = misc.extractCircle(bitmap);
					if (bitmap != null) {
						Drawable draw = new BitmapDrawable(getContext().getResources(), bitmap);
						_profileImageView.setBackgroundDrawable(draw);
					}
				}
			}
			super.onReceiveResult(resultCode, resultData);
		}
	};
}
