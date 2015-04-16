package com.fieldnation.json;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

public class JsonArray implements Parcelable {

    private Bundle _objs = new Bundle();

    public JsonArray() {
    }

    JsonArray(Bundle bundle) {
        _objs = bundle;
    }

    public JsonArray(byte[] data) throws ParseException {
        this(new String(data));
    }

    public JsonArray(String string) throws ParseException {
        JsonTokenizer tokenizer = new JsonTokenizer(string);
        fromTokenizer(tokenizer);
    }

    public JsonArray(JsonTokenizer tokenizer) throws ParseException {
        fromTokenizer(tokenizer);
    }

    private void fromTokenizer(JsonTokenizer tokenizer) throws ParseException {
        if (!tokenizer.isNextTokenStartArray()) {
            throw new ParseException("Not an array!", -1);
        }
        // pop the open brace
        tokenizer.nextToken();

        // check for empty array
        if (tokenizer.isNextTokenFinishArray()) {
            tokenizer.nextToken();
            return;
        }

        while (true) {
            addThis(tokenizer.parseValue());

            // if not comma, then it should've been a ']'
            if (!tokenizer.nextToken().equals(",")) {
                break;
            }
        }

    }

    public int size() {
        return _objs.size();
    }

    public Object get(int index) {
        return _objs.get(index + "");
    }

    public Object get(String path) {
        List<String> dir = JsonTokenizer.parsePath(path);

        return get(dir);
    }

    protected Object get(List<String> directions) {
        if (directions.size() == 0)
            return this;

        String value = directions.remove(0);

        value = value.replace("[", "");
        value = value.replace("]", "");

        int item = Integer.parseInt(value);

        Object obj = _objs.get(item + "");

        if (obj == null && directions.size() == 0) {
            return null;
        } else if (obj instanceof JsonObject) {
            return ((JsonObject) obj).get(directions);
        } else if (obj instanceof JsonArray) {
            return ((JsonArray) obj).get(directions);
        }

        return obj;
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public JsonObject getJsonObject(int index) {
        return (JsonObject) get(index);
    }

    public JsonArray getJsonArray(int index) {
        return (JsonArray) get(index);
    }

    public int getInt(int index) {
        return Integer.parseInt(getString(index));
    }

    public long getLong(int index) {
        return Long.parseLong(getString(index));
    }

    public float getFloat(int index) {
        return Float.parseFloat(getString(index));
    }

    public double getDouble(int index) {
        return Double.parseDouble(getString(index));
    }

    public void merge(JsonArray source) {
        for (int i = 0; i < source.size(); i++) {
            add(source.get(i));
        }
    }

    public JsonArray set(int index, Object value) {
        while (index >= _objs.size())
            addThis(null);

        setThis(index, value);
        return this;
    }

    protected JsonArray set(List<String> directions, Object value) throws ParseException {
        if (directions.size() == 0) {
            throw new ParseException("Invalid path", 0);
        }

        String item = directions.remove(0);

        item = item.replaceAll("\\[", "");
        item = item.replaceAll("\\]", "");

        int index = Integer.parseInt(item);

        if (directions.size() == 0) {
            set(index, value);
        } else {
            // already exists
            if (_objs.size() > index) {
                Object obj = _objs.get(index + "");

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

                String child = directions.get(0);
                if (child.startsWith("[") && child.endsWith("]")) {
                    JsonArray ja = new JsonArray();

                    set(index, ja);

                    if (child.equals("[]")) {
                        ja.add(directions, value);
                    } else {
                        ja.set(directions, value);
                    }
                } else {
                    JsonObject jo = new JsonObject();

                    set(index, jo);

                    jo.put(directions, value);
                }
            }

        }
        return this;
    }

    private void addThis(Object value) {
        String key = _objs.size() + "";
        if (value == null) {
            _objs.putParcelable(key, null);
        } else if (value instanceof Parcelable) {
            _objs.putParcelable(key, (Parcelable) value);
        } else if (value instanceof String) {
            _objs.putString(key, (String) value);
        } else if (value instanceof Short) {
            _objs.putShort(key, (Short) value);
        } else if (value instanceof Integer) {
            _objs.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            _objs.putLong(key, (Long) value);
        } else if (value instanceof Byte) {
            _objs.putByte(key, (Byte) value);
        } else if (value instanceof Boolean) {
            _objs.putBoolean(key, (Boolean) value);
        } else if (value instanceof Character) {
            _objs.putChar(key, (Character) value);
        } else if (value instanceof Double) {
            _objs.putDouble(key, (Double) value);
        } else if (value instanceof Float) {
            _objs.putFloat(key, (Float) value);
        } else if (value instanceof Serializable) {
            _objs.putSerializable(key, (Serializable) value);
        } else {
            Log.v("JsonObject", "can't put object of type: " + value.getClass().getName());
        }
    }

    private void setThis(int index, Object value) {
        String key = index + "";
        if (value == null) {
            _objs.putParcelable(key, null);
        } else if (value instanceof Parcelable) {
            _objs.putParcelable(key, (Parcelable) value);
        } else if (value instanceof String) {
            _objs.putString(key, (String) value);
        } else if (value instanceof Short) {
            _objs.putShort(key, (Short) value);
        } else if (value instanceof Integer) {
            _objs.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            _objs.putLong(key, (Long) value);
        } else if (value instanceof Byte) {
            _objs.putByte(key, (Byte) value);
        } else if (value instanceof Boolean) {
            _objs.putBoolean(key, (Boolean) value);
        } else if (value instanceof Character) {
            _objs.putChar(key, (Character) value);
        } else if (value instanceof Double) {
            _objs.putDouble(key, (Double) value);
        } else if (value instanceof Float) {
            _objs.putFloat(key, (Float) value);
        } else if (value instanceof Serializable) {
            _objs.putSerializable(key, (Serializable) value);
        } else {
            Log.v("JsonObject", "can't put object of type: " + value.getClass().getName());
        }
    }

    public JsonArray add(Object value) {
        addThis(value);
        return this;
    }

    public JsonArray add(int index, Object value) {
        setThis(index, value);
        return this;
    }

    protected JsonArray add(List<String> directions, Object value) throws ParseException {
        if (directions.size() == 0)
            throw new ParseException("Invalid path", 0);

        String item = directions.remove(0);

        if (!item.equals("[]"))
            throw new ParseException("This should never happen!", 0);

        if (directions.size() == 0) {
            add(value);
        } else {
            String child = directions.get(0);
            if (child.startsWith("[") && child.endsWith("]")) {
                JsonArray ja = new JsonArray();

                _objs.putParcelable(_objs.size() + "", ja);

                if (child.equals("[]"))
                    ja.add(directions, value);
                else
                    ja.set(directions, value);
            } else {
                JsonObject jo = new JsonObject();

                _objs.putParcelable(_objs.size() + "", jo);

                jo.put(directions, value);
            }
        }
        return this;
    }

    private void removeThis(int index) {
        int last = _objs.size() - 1;
        for (int i = index; i < last - 1; i++) {
            setThis(index, _objs.get((index + 1) + ""));
        }
    }

    public Object remove(int index) {
        Object obj = get(index);
        removeThis(index);
        return obj;
    }

    protected Object remove(List<String> directions) throws ParseException {
        if (directions.size() == 0)
            throw new ParseException("Invalid path!", -1);

        String item = directions.remove(0);

        item = item.replaceAll("\\[", "");
        item = item.replaceAll("\\]", "");

        int index = Integer.parseInt(item);

        if (directions.size() == 0) {
            return remove(index);
        } else {
            Object obj = get(index);

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

    public JsonArray clear() {
        _objs.clear();
        return this;
    }

    public String display() {
        return display(0).toString();
    }

    protected StringBuilder display(int depth) {
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");
        for (int i = 0; i < _objs.size(); i++) {
            Object obj = _objs.get(i + "");

            sb.append(JsonTokenizer.repeat("  ", depth + 1));
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

            if (i < _objs.size() - 1) {
                sb.append(",");
            }

            sb.append("\n");
        }
        sb.append(JsonTokenizer.repeat("  ", depth));
        sb.append("]");

        return sb;
    }

    public StringBuilder toStringBuilder() {
        StringBuilder sb = new StringBuilder();

        addToStringBuilder(sb);

        return sb;
    }

    public void addToStringBuilder(StringBuilder sb) {
        sb.append("[");
        for (int i = 0; i < _objs.size(); i++) {
            Object obj = _objs.get(i + "");

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

            if (i < _objs.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
    }

    public boolean has(int index) {
        if (index < _objs.size())
            return true;

        return false;
    }

    protected boolean has(List<String> directions) throws ParseException {
        if (directions.size() == 0)
            throw new ParseException("Invalid path!", -1);

        String item = directions.remove(0);

        item = item.replaceAll("\\[", "");
        item = item.replaceAll("\\]", "");

        int index = Integer.parseInt(item);

        if (!has(index))
            return false;
        else if (directions.size() == 0)
            return true;

        Object obj = get(index);

        if (obj instanceof JsonObject) {
            JsonObject jo = (JsonObject) obj;

            return jo.has(directions);
        } else if (obj instanceof JsonArray) {
            JsonArray ja = (JsonArray) obj;

            return ja.has(directions);
        }

        return false;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    public byte[] toByteArray() {
        return toStringBuilder().toString().getBytes();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return new JsonArray(this.toString());
        } catch (ParseException e) {
        }
        return null;
    }

    public JsonArray copy() {
        try {
            return new JsonArray(this.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Creator<JsonArray> CREATOR = new Creator<JsonArray>() {
        @Override
        public JsonArray createFromParcel(Parcel parcel) {
            return new JsonArray(parcel.readBundle(JsonArray.class.getClassLoader()));
        }

        @Override
        public JsonArray[] newArray(int i) {
            return new JsonArray[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(_objs);
    }
}
