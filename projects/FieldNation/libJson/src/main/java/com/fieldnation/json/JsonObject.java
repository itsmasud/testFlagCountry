package com.fieldnation.json;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public class JsonObject implements Parcelable {

    private boolean _isNullObject;

    private Bundle _fieldsB = new Bundle();

    public static final JsonObject JsonNULL = new JsonObject(true);

    public JsonObject() {
        _isNullObject = false;
    }

    private JsonObject(boolean isNullObject) {
        _isNullObject = isNullObject;
    }

    JsonObject(Bundle bundle) {
        _fieldsB = bundle;
    }

    public JsonObject(byte[] data) throws ParseException {
        this(new String(data));
    }

    public JsonObject(String string) throws ParseException {
        _isNullObject = false;
        JsonTokenizer tokenizer = new JsonTokenizer(string);
        fromTokenizer(tokenizer);
    }

    public JsonObject(String key, Object value) throws ParseException {
        _isNullObject = false;
        put(key, value);
    }

    public JsonObject(JsonTokenizer tokenizer) throws ParseException {
        _isNullObject = false;
        fromTokenizer(tokenizer);
    }

    private void fromTokenizer(JsonTokenizer tokenizer) throws ParseException {
        if (!tokenizer.isNextTokenStartObject()) {
            throw new ParseException("Not an object!", -1);
        }

        // pop the open brace
        tokenizer.nextToken();

        if (tokenizer.isNextTokenFinishObject()) {
            tokenizer.nextToken();
            return;
        }

        while (true) {
            String key = tokenizer.nextToken();

            if (!tokenizer.nextToken().equals(":")) {
//                throw new ParseException(
//                        "token must be ':' (" + tokenizer.getTemp() + ")", 1);
                throw new ParseException(
                        "token must be ':' (bleh)", 1);
            }

            putThis(key, tokenizer.parseValue());

            if (!tokenizer.nextToken().equals(",")) {
                break;
            }
        }
    }

    public int size() {
        return _fieldsB.size();
    }

    private void putThis(String key, Object value) {
        if (value == null) {
            _fieldsB.putParcelable(key, null);
        } else if (value instanceof Parcelable) {
            _fieldsB.putParcelable(key, (Parcelable) value);
        } else if (value instanceof String) {
            _fieldsB.putString(key, (String) value);
        } else if (value instanceof Short) {
            _fieldsB.putShort(key, (Short) value);
        } else if (value instanceof Integer) {
            _fieldsB.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            _fieldsB.putLong(key, (Long) value);
        } else if (value instanceof Byte) {
            _fieldsB.putByte(key, (Byte) value);
        } else if (value instanceof Boolean) {
            _fieldsB.putBoolean(key, (Boolean) value);
        } else if (value instanceof Character) {
            _fieldsB.putChar(key, (Character) value);
        } else if (value instanceof Double) {
            _fieldsB.putDouble(key, (Double) value);
        } else if (value instanceof Float) {
            _fieldsB.putFloat(key, (Float) value);
        } else if (value instanceof Serializable) {
            _fieldsB.putSerializable(key, (Serializable) value);
        } else {
            Log.v("JsonObject", "can't put object of type: " + value.getClass().getName());
        }
    }

    /**
     * Should be int,long,string,float,double,JsonObject, or JsonArray
     *
     * @param key
     * @param value
     * @throws ParseException
     */

    public JsonObject put(String key, Object value) throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }

        List<String> directions = JsonTokenizer.parsePath(key);

        put(directions, value);

        return this;
    }

    protected JsonObject put(List<String> directions, Object value) throws ParseException {
        if (directions.size() == 0) {
            throw new ParseException("Invalid path", 0);
        }

        String item = directions.remove(0);

        if (directions.size() == 0) {
            // goes in this object
            if (value == null) {
                _fieldsB.putParcelable(item, null);
            } else {
                putThis(item, value);
            }
        } else {
            // goes into a child
            if (_fieldsB.containsKey(item)) {
                Object obj = _fieldsB.get(item);

                if (obj instanceof JsonObject) {
                    JsonObject jo = (JsonObject) obj;

                    jo.put(directions, value);
                } else if (obj instanceof JsonArray) {
                    JsonArray ja = (JsonArray) obj;

                    String child = directions.get(0);
                    if (child.equals("[]")) {
                        ja.add(directions, value);
                    } else {
                        ja.set(directions, value);
                    }
                }
            } else {
                // need to create object
                String child = directions.get(0);

                if (child.startsWith("[") && child.endsWith("]")) {
                    JsonArray ja = new JsonArray();

                    _fieldsB.putParcelable(item, ja);

                    if (child.equals("[]")) {
                        ja.add(directions, value);
                    } else {
                        ja.set(directions, value);
                    }
                } else {
                    JsonObject jo = new JsonObject();

                    _fieldsB.putParcelable(item, jo);

                    jo.put(directions, value);
                }
            }
        }
        return this;
    }

    public Object get(String path) throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }

        List<String> directions = JsonTokenizer.parsePath(path);

        return get(directions);
    }

    protected Object get(List<String> directions) {
        if (directions.size() == 0) {
            if (this == JsonNULL)
                return null;
            else
                return this;
        }

        String item = directions.remove(0);

        Object obj = _fieldsB.get(item);

        if (obj == null && directions.size() == 0) {
            return null;
        } else if (obj instanceof JsonObject) {
            return ((JsonObject) obj).get(directions);
        } else if (obj instanceof JsonArray) {
            return ((JsonArray) obj).get(directions);
        }

        return obj;

    }

    public String getString(String path) throws ParseException {
        return get(path) + "";
    }

    public int getInt(String path) throws ParseException {
        return Integer.parseInt(getString(path));
    }

    public long getLong(String path) throws ParseException {
        return Long.parseLong(getString(path));
    }

    public float getFloat(String path) throws ParseException {
        return Float.parseFloat(getString(path));
    }

    public double getDouble(String path) throws ParseException {
        return Double.parseDouble(getString(path));
    }

    public boolean getBoolean(String path) throws ParseException {
        return "true".equals(getString(path));
    }

    public JsonObject getJsonObject(String path) throws ParseException {
        return (JsonObject) get(path);
    }

    public JsonArray getJsonArray(String path) throws ParseException {
        return (JsonArray) get(path);
    }

    public Iterator<String> keys() throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }
        return _fieldsB.keySet().iterator();
    }

    public boolean has(String path) throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }

        List<String> dir = JsonTokenizer.parsePath(path);

        return has(dir);
    }

    protected boolean has(List<String> directions) throws ParseException {
        if (directions.size() == 0) {
            throw new ParseException("Invalid path!", -1);
        }

        String item = directions.remove(0);

        if (!_fieldsB.containsKey(item))
            return false;
            // we have item and no more
        else if (directions.size() == 0)
            return true;

        Object obj = get(item);

        if (obj == null) {
            return false;
        } else if (obj instanceof JsonObject) {
            JsonObject jo = (JsonObject) obj;
            return jo.has(directions);
        } else if (obj instanceof JsonArray) {
            JsonArray ja = (JsonArray) obj;
            return ja.has(directions);
        }

        return false;
    }

    public Object remove(String path) throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }

        if (has(path)) {
            List<String> dir = JsonTokenizer.parsePath(path);

            return remove(dir);
        }

        return null;
    }

    protected Object remove(List<String> directions) throws ParseException {
        if (directions.size() == 0)
            throw new ParseException("Invalid path!", -1);

        String item = directions.remove(0);

        if (directions.size() == 0) {
            Object val = get(item);
            _fieldsB.remove(item);
            return val;
        } else {
            Object obj = get(item);

            if (obj instanceof JsonObject) {
                JsonObject jo = (JsonObject) obj;

                return jo.remove(directions);
            } else if (obj instanceof JsonArray) {
                JsonArray jo = (JsonArray) obj;

                return jo.remove(directions);
            }
        }

        return null;
    }

    public void mixin(JsonObject jsonObject) throws ParseException {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();

            put(key, jsonObject.get(key));
        }
    }

    public String display() {
        return display(0).toString();
    }

    protected StringBuilder display(int depth) {
        StringBuilder sb = new StringBuilder();

        if (_isNullObject) {
            sb.append("null");
            return sb;
        }

        sb.append("{\n");

        int size = _fieldsB.size() - 1;
        int i = 0;

        Iterator<String> keys = _fieldsB.keySet().iterator();

        while (keys.hasNext()) {

            String key = keys.next();

            sb.append(JsonTokenizer.repeat("  ", depth + 1));
            sb.append(JsonTokenizer.escapeString(key));
            sb.append(":");

            Object obj = _fieldsB.get(key);

            if (obj == null) {
                sb.append("null");
            } else if (obj instanceof JsonObject) {
                sb.append(((JsonObject) obj).display(depth + 1));
            } else if (obj instanceof JsonArray) {
                sb.append(((JsonArray) obj).display(depth + 1));
            } else if (obj instanceof String) {
                sb.append(JsonTokenizer.escapeString((String) obj));
            } else {
                sb.append(obj + "");
            }

            if (i < size) {
                sb.append(",");
            }

            sb.append("\n");
            i++;
        }
        sb.append(JsonTokenizer.repeat("  ", depth));
        sb.append("}");

        return sb;
    }

    public StringBuilder toStringBuilder() {
        StringBuilder sb = new StringBuilder();

        addToStringBuilder(sb);

        return sb;
    }

    public void addToStringBuilder(StringBuilder sb) {
        if (_isNullObject) {
            sb.append("null");
            return;
        }

        sb.append("{");

        int size = _fieldsB.size() - 1;
        int i = 0;

        Iterator<String> keys = _fieldsB.keySet().iterator();

        while (keys.hasNext()) {

            String key = keys.next();

            sb.append(JsonTokenizer.escapeString(key));
            sb.append(":");

            Object obj = _fieldsB.get(key);

            if (obj == null) {
                sb.append("null");
            } else if (obj instanceof JsonObject) {
                ((JsonObject) obj).addToStringBuilder(sb);
            } else if (obj instanceof JsonArray) {
                ((JsonArray) obj).addToStringBuilder(sb);
            } else if (obj instanceof String) {
                sb.append(JsonTokenizer.escapeString((String) obj));
            } else {
                sb.append(obj + "");
            }

            if (i < size) {
                sb.append(",");
            }

            i++;
        }

        sb.append("}");
    }

    /**
     * Merges the contents of source into this object.
     *
     * @param src       the object to read from.
     * @param overwrite if true, then when there is a key conflict, the key value from
     *                  src is used. If false, then it is not used.
     * @param copy      if true, then a deep copy of the data is made. Otherwise they
     *                  are just linked.
     * @throws ParseException
     * @throws CloneNotSupportedException
     */
    public void merge(JsonObject src, boolean overwrite, boolean copy) throws ParseException {
        Iterator<String> e = src.keys();

        while (e.hasNext()) {
            String key = e.next();
            Object value = src.get(key);

            if (has(key) && overwrite || !has(key)) {
                if (copy) {
                    try {
                        if (value instanceof JsonObject)
                            put(key, ((JsonObject) value).clone());
                        else if (value instanceof JsonArray)
                            put(key, ((JsonArray) value).clone());
                        else {
                            put(key, value);
                        }
                    } catch (CloneNotSupportedException ex) {
                    }

                } else {
                    put(key, value);
                }
            }
        }
    }

    public void deepmerge(JsonObject src) throws ParseException {
        Iterator<String> e = src.keys();

        while (e.hasNext()) {
            String key = e.next();
            Object value = src.get(key);

            if (has(key)) {
                if (value instanceof JsonObject && get(key) instanceof JsonObject) {
                    JsonObject json = getJsonObject(key);
                    json.deepmerge((JsonObject) value);
                    put(key, json);
                } else if (value instanceof JsonArray && get(key) instanceof JsonArray) {
                    JsonArray json = getJsonArray(key);
                    json.merge((JsonArray) value);
                    put(key, json);
                } else {
                    put(key, value);
                }
            } else {
                put(key, value);
            }

        }
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    public byte[] toByteArray() {
        return toString().getBytes();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return new JsonObject(this.toString());
        } catch (ParseException e) {
        }
        return null;
    }

    public JsonObject copy() {
        try {
            return new JsonObject(this.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static final Creator<JsonObject> CREATOR = new Creator<JsonObject>() {
        @Override
        public JsonObject createFromParcel(Parcel parcel) {
            return new JsonObject(parcel.readBundle(JsonObject.class.getClassLoader()));
        }

        @Override
        public JsonObject[] newArray(int i) {
            return new JsonObject[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(_fieldsB);
    }
}
