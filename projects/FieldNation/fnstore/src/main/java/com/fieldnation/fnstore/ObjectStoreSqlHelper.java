package com.fieldnation.fnstore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class ObjectStoreSqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "ObjectStoreSqlHelper";
    // Note: increment this value every time the structure of the database is
    // changed.
    private static final int TABLE_VER = 3;
    public static final String TABLE_NAME = "object_store";
    private static ObjectStoreSqlHelper _instance = null;

    public enum Column {
        ID(0, "_id", "integer primary key autoincrement"),
        PROFILE_ID(1, "profile_id", "integer not null", true),
        LAST_UPDATED(2, "last_updated", "integer not null", true),
        OBJ_KEY(3, "obj_key", "text not null", true),
        OBJ_NAME(4, "obj_name", "text not null", true),
        IS_FILE(5, "is_file", "integer not null"),
        EXPIRES(6, "expires", "integer not null"),
        DATA(7, "data", "blob"),
        HASH(8, "hash", "blob");

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

    private ObjectStoreSqlHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME + ".db", null, TABLE_VER);
    }

    public static ObjectStoreSqlHelper getInstance(Context context) {
        synchronized (TAG) {
            if (_instance == null)
                _instance = new ObjectStoreSqlHelper(context.getApplicationContext());
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");

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
