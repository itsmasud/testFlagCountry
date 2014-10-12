package com.fieldnation.data.workorder;

import java.text.ParseException;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class AdditionalExpense implements Parcelable {
	@Json(name = "statusDescription")
	private String _statusDescription;
	@Json(name = "status")
	private String _status;
	@Json(name = "expenseId")
	private Integer _expenseId;
	@Json(name = "description")
	private String _description;
	@Json(name = "price")
	private Double _price;
	@Json(name = "approved")
	private Boolean _approved;
	@Json(name = "categoryId")
	private Integer _categoryId;

	public AdditionalExpense() {
	}

	public String getStatusDescription() {
		return _statusDescription;
	}

	public String getStatus() {
		return _status;
	}

	public Integer getExpenseId() {
		return _expenseId;
	}

	public String getDescription() {
		return _description;
	}

	public Double getPrice() {
		return _price;
	}

	public Boolean getApproved() {
		return _approved;
	}

	public int getCategoryId() {
		return _categoryId;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(AdditionalExpense additionalExpense) {
		try {
			return Serializer.serializeObject(additionalExpense);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static AdditionalExpense fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(AdditionalExpense.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*-*************************************************-*/
	/*-				Human Generated Code				-*/
	/*-*************************************************-*/
	public AdditionalExpense(String description, double amount, ExpenseCategory category) {
		_categoryId = category.getId();
		_description = description;
		_price = amount;
		_status = "New";
	}

	/*-*********************************************-*/
	/*-			Parcelable Implementation			-*/
	/*-*********************************************-*/

	public static final Parcelable.Creator<AdditionalExpense> CREATOR = new Parcelable.Creator<AdditionalExpense>() {

		@Override
		public AdditionalExpense createFromParcel(Parcel source) {
			try {
				return AdditionalExpense.fromJson(new JsonObject(source.readString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public AdditionalExpense[] newArray(int size) {
			return new AdditionalExpense[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(toJson().toString());
	}

}
