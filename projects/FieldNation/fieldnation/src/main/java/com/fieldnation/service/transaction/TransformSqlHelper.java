package com.fieldnation.service.transaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class TransformSqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "TransformSqlHelper";
    // Note: increment this value every time the structure of the database is
    // changed.
    private static final int TABLE_VER = 2;
    public static final String TABLE_NAME = "transforms";

    private static TransformSqlHelper _instance = null;

    public enum Column {
        ID(0, "_id", "integer primary key autoincrement"),
        TRANSACTION_ID(1, "transaction_id", "integer not null", true),
        OBJECT_NAME_KEY(2, "object_name_key", "text not null", true),
        OBJECT_NAME(3, "object_name", "text not null"),
        OBJECT_KEY(4, "object_key", "text not null"),
        ACTION(5, "action", "text not null"),
        DATA(6, "data", "blob not null");

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

    private TransformSqlHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME + ".db", null, TABLE_VER);
    }

    public static TransformSqlHelper getInstance(Context context) {
        synchronized (TAG) {
            if (_instance == null) {
                _instance = new TransformSqlHelper(context.getApplicationContext());
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

    public static String[] getColumnNames() {
        return Column.getColumnNames();
    }
}
