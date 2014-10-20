package com.fieldnation.j2j;

import java.util.Hashtable;

public class Config {
	public static final String ObjectPath = "c:/j2j/objects/";
	public static final String LogPath = "c:/j2j/logs/";

	public static Hashtable<String, String> ClassNameMap;

	static {
		ClassNameMap = new Hashtable<String, String>();

		ClassNameMap.put("Tasks", "Task");
		ClassNameMap.put("TaskDetail", "Task");
		ClassNameMap.put("Skillsets", "Skillset");
		ClassNameMap.put("AdditionalExpenses", "Expense");
		ClassNameMap.put("CustomFields", "CustomField");
		ClassNameMap.put("Deliverables", "Deliverable");
		ClassNameMap.put("Workorders", "Workorder");
		ClassNameMap.put("Discounts", "Discount");
		ClassNameMap.put("ToUser", "User");
		ClassNameMap.put("FromUser", "User");
		ClassNameMap.put("UploadedBy", "User");
		ClassNameMap.put("UpdatedBy", "User");
		ClassNameMap.put("Documents", "Document");
		ClassNameMap.put("Fees", "Fee");
		ClassNameMap.put("UploadSlots", "UploadSlot");
		ClassNameMap.put("UploadedDocuments", "UploadedDocument");
		

	}

}
