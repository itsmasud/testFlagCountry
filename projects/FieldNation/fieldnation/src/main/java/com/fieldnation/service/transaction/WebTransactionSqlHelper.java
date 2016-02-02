package com.fieldnation.service.transaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fieldnation.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Michael Carver on 2/27/2015.
 */
class WebTransactionSqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "WebTransactionSqlHelper";
    // Note: increment this value every time the structure of the database is
    // changed.
    private static final int TABLE_VER = 2;
    public static final String TABLE_NAME = "transactions";

    private static WeakHashMap<Context, WebTransactionSqlHelper> _instances = new WeakHashMap<>();

    public enum Column {
        ID(0, "_id", "integer primary key autoincrement"),
        HANDLER(1, "handler", "text"),
        HANDLER_PARAMS(2, "handler_params", "blob"),
        USE_AUTH(3, "use_auth", "integer not null"),
        IS_SYNC(4, "is_sync", "integer not null"),
        STATE(5, "state", "integer not null", true),
        REQUEST(6, "request", "text"),
        PRIORITY(7, "priority", "integer not null", true),
        KEY(8, "key", "text", true),
        QUEUE_TIME(9, "queue_time", "integer not null", true);

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

    private WebTransactionSqlHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME + ".db", null, TABLE_VER);
    }

    public static WebTransactionSqlHelper getInstance(Context context) {
        Context app = context.getApplicationContext();
        if (!_instances.containsKey(app)) {
            _instances.put(app, new WebTransactionSqlHelper(app));
        }

        return _instances.get(app);
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
