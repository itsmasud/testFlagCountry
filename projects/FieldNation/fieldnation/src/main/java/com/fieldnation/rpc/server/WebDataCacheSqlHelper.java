package com.fieldnation.rpc.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class WebDataCacheSqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "rpc.server.WebDataCacheSqlHelper";
    private static final int TABLE_VER = 4;
    public static final String TABLE_NAME = "data_cache";

    public enum Column {
        ID(0, "_id", "integer primary key autoincrement"),
        EXPIRES_ON(1, "expires_on", "integer not null", true),
        KEY(2, "key", "text not null", true),
        RESPONSE_DATA(3, "response_data", "text not null"),
        RESPONSE_CODE(4, "response_code", "integer not null");

        private int _index;
        private String _name;
        private String _declaration;
        private boolean _doIndex = false;

        private static String[] _names = null;

        private Column(int index, String name, String declaration, boolean doIndex) {
            this(index, name, declaration);
            _doIndex = doIndex;
        }

        private Column(int index, String name, String declaration) {
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

    public WebDataCacheSqlHelper(Context context) {
        super(context, TABLE_NAME + ".db", null, TABLE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        List<String> indicies = new LinkedList<String>();

        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        Column[] vs = Column.values();
        for (int i = 0; i < vs.length; i++) {
            Column c = vs[i];
            sb.append(c.getName() + " " + c.getDeclaration());
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
