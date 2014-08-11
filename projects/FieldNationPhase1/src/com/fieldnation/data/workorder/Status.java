package com.fieldnation.data.workorder;

import java.util.Hashtable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Status {
	@Json(name = "status")
	private String _status;
	@Json(name = "colorIntent")
	private String _colorIntent;
	@Json(name = "subStatus")
	private String _subStatus;

	public Status() {
	}

	public String getStatus() {
		return _status;
	}

	public String getColorIntent() {
		return _colorIntent;
	}

	public String getSubStatus() {
		return _subStatus;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Status status) {
		try {
			return Serializer.serializeObject(status);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Status fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Status.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*-*************************************************-*/
	/*-				Human Generated Code				-*/
	/*-*************************************************-*/

	public WorkorderSubstatus getWorkorderSubstatus() {
		return WorkorderSubstatus.fromValue(_subStatus);
	}

	public WorkorderStatus getWorkorderStatus() {
		return WorkorderStatus.fromValue(_status);
	}

	public StatusIntent getStatusIntent() {
		switch (getWorkorderStatus()) {
		case AVAILABLE:
			switch (getWorkorderSubstatus()) {
			case AVAILABLE:
				return StatusIntent.NORMAL;
			case ROUTED:
				return StatusIntent.WARNING;
			case COUNTEROFFERED:
				return StatusIntent.WAITING;
			case REQUESTED:
				return StatusIntent.SUCCESS;
			default:
				return null;
			}
		case ASSIGNED:
			switch (getWorkorderSubstatus()) {
			case UNCONFIRMED:
				return StatusIntent.SUCCESS;
			case CONFIRMED:
				return StatusIntent.NORMAL;
			case ONHOLD_UNACKNOWLEDGED:
				return StatusIntent.WARNING;
			case ONHOLD_ACKNOWLEDGED:
				return StatusIntent.WAITING;
			default:
				return null;
			}
		case CANCELLED:
			switch (getWorkorderSubstatus()) {
			case CANCELLED:
				return StatusIntent.NORMAL;
			case CANCELLED_LATEFEEPROCESSING:
				return StatusIntent.SUCCESS;
			case CANCELLED_LATEFEEPAID:
				return StatusIntent.WAITING;
			default:
				return null;
			}
		case COMPLETED:
			switch (getWorkorderSubstatus()) {
			case PENDINGREVIEWED:
				return StatusIntent.NORMAL;
			case INREVIEW:
				return StatusIntent.NORMAL;
			case APPROVED_PROCESSINGPAYMENT:
				return StatusIntent.SUCCESS;
			case PAID:
				return StatusIntent.WAITING;
			default:
				return null;
			}
		case INPROGRESS:
			switch (getWorkorderSubstatus()) {
			case CHECKEDIN:
				return StatusIntent.SUCCESS;
			case CHECKEDOUT:
				return StatusIntent.NORMAL;
			case ONHOLD_UNACKNOWLEDGED:
				return StatusIntent.WARNING;
			case ONHOLD_ACKNOWLEDGED:
				return StatusIntent.WAITING;
			default:
				return null;
			}
		default:
			return null;
		}
	}
}
