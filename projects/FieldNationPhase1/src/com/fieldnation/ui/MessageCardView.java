package com.fieldnation.ui;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageCardView extends RelativeLayout {
	private static final String TAG = "ui.MessageView";
	private View _statusView;
	private TextView _titleTextView;
	private TextView _substatusTextView;
	private TextView _messageBodyTextView;
	private TextView _timeTextView;
	private ImageView _profileImageView;
	private int _viewId;

	private PhotoService _photoService;
	private Message _message;

	/*-*****************************-*/
	/*-			LifeCycle			-*/
	/*-*****************************-*/
	public MessageCardView(Context context) {
		this(context, null, -1);
	}

	public MessageCardView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessageCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.view_message_card, this);

		if (isInEditMode())
			return;

		_photoService = new PhotoService(context, _resultReceiver);

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
		_substatusTextView = (TextView) findViewById(R.id.substatus_textview);
		_timeTextView = (TextView) findViewById(R.id.time_textview);
		_profileImageView = (ImageView) findViewById(R.id.profile_imageview);
	}

	public void setMessage(Message message) {
		_message = message;
		_viewId = (int) (message.getMessageId() % Integer.MAX_VALUE);
		try {
			_titleTextView.setText(message.getMessageId() + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			_messageBodyTextView.setText(message.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Calendar cal = ISO8601.toCalendar(message.getDate());

			String date = misc.formatDateLong(cal);

			_timeTextView.setText(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			_profileImageView.setBackgroundDrawable(null);
			String url = message.getPhotoUrl();
			getContext().startService(_photoService.getPhoto(_viewId, url));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Message getMessage() {
		return _message;
	}

	private ResultReceiver _resultReceiver = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == _viewId) {
				byte[] data = resultData.getByteArray(PhotoServiceConstants.KEY_RESPONSE_DATA);

				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (bitmap != null) {
					bitmap = misc.extractCircle(bitmap);
					Drawable draw = new BitmapDrawable(getContext().getResources(), bitmap);
					_profileImageView.setBackgroundDrawable(draw);
				}
			}
			super.onReceiveResult(resultCode, resultData);
		}
	};

}
