package com.fieldnation.analytics.trackers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fieldnation.fnlog.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 10/31/17.
 */

public class AttachmentTrackerSQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "AttachmentTrackerSQLHelper";

    private static final int TABLE_VER = 1;
    public static final String TABLE_NAME = "attachment_trackers";

    private static AttachmentTrackerSQLHelper _instance = null;

    public enum Column {
        ID(0, "_id", "integer primary key autoincrement"),
        PARENT_UUID(1, "parent_uuid", "text", true),
        UUID(2, "uuid", "text", true);

        private final int _index;
        private final String _name;
        private final String _declaration;
        private boolean _doIndex = false;

        private static String[] _names = null;

        Column(int index, String name, String declaration, boolean doIndex) {
            this(index, name, declaration);
            _doIndex = doIndex;
        }

        Column(int index, String name, String declaration) {
            _index = index;
            _name = name;
            _declaration = declaration;
        }

        public int getIndex() {
            return _index;
        }

        public String getName() {
            return _name;
        }

        public String getDeclaration() {
            return _declaration;
        }

        public boolean hasIndex() {
            return _doIndex;
        }

        public static String[] getColumnNames() {
            if (_names == null) {
                Column[] vs = values();
                _names = new String[vs.length];
                for (int i = 0; i < vs.length; i++) {
                    _names[i] = vs[i]._name;
                }
            }
            return _names;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    private AttachmentTrackerSQLHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME + ".db", null, TABLE_VER);
    }

    public static AttachmentTrackerSQLHelper getInstance(Context context) {
        synchronized (TAG) {
            if (_instance == null) {
                _instance = new AttachmentTrackerSQLHelper(context.getApplicationContext());
            }
            return _instance;
        }
    }

    public static void stop() {
        synchronized (TAG) {
            if (_instance != null) {
                _instance.close();
            }
            _instance = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        List<String> indicies = new LinkedList<>();

        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        Column[] vs = Column.values();
        for (int i = 0; i < vs.length; i++) {
            Column c = vs[i];
            sb.append(c.getName()).append(" ").append(c.getDeclaration());
            if (i < vs.length - 1) {
                sb.append(",");
            }

            if (c.hasIndex()) {
                indicies.add("CREATE INDEX " + TABLE_NAME + "_" + c.getName() + " ON " + TABLE_NAME + " (" + c.getName() + ");");
            }
        }
        sb.append(");");
        db.execSQL(sb.toString());

        for (int i = 0; i < indicies.size(); i++) {
            db.execSQL(indicies.get(i));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    public static String[] getColumnNames() {
        return Column.getColumnNames();
    }

    public String getTableAsString() {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor allRows = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (allRows.moveToFirst()) {
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name : columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        return tableString;
    }
}
