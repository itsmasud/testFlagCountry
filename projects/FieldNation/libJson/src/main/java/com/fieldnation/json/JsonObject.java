package com.fieldnation.json;

import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class JsonObject {

    private boolean _isNullObject;
    private Hashtable<String, Object> _fields = new Hashtable<String, Object>();

    public static JsonObject JsonNULL = new JsonObject(true);

    public JsonObject() {
        _isNullObject = false;
    }

    private JsonObject(boolean isNullObject) {
        _isNullObject = isNullObject;
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

            _fields.put(key, tokenizer.parseValue());

            if (!tokenizer.nextToken().equals(",")) {
                break;
            }
        }
    }

    public int size() {
        return _fields.size();
    }

    /**
     * Should be int,long,string,float,double,JsonObject, or JsonArray
     *
     * @param key
     * @param value
     * @throws ParseException
     */
    public void put(String key, Object value) throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }

        List<String> directions = JsonTokenizer.parsePath(key);

        put(directions, value);
    }

    protected void put(List<String> directions, Object value) throws ParseException {
        if (directions.size() == 0)
            throw new ParseException("Invalid path", 0);

        String item = directions.remove(0);

        if (directions.size() == 0) {
            // goes in this object
            if (value == null)
                _fields.put(item, JsonNULL);
            else
                _fields.put(item, value);
        } else {
            // goes into a child
            if (_fields.containsKey(item)) {
                Object obj = _fields.get(item);

                if (obj instanceof JsonObject) {
                    JsonObject jo = (JsonObject) obj;

                    jo.put(directions, value);
                    return;
                } else if (obj instanceof JsonArray) {
                    JsonArray ja = (JsonArray) obj;

                    String child = directions.get(0);
                    if (child.equals("[]"))
                        ja.add(directions, value);
                    else
                        ja.set(directions, value);

                    return;
                }
            } else {
                // need to create object
                String child = directions.get(0);

                if (child.startsWith("[") && child.endsWith("]")) {
                    JsonArray ja = new JsonArray();

                    _fields.put(item, ja);

                    if (child.equals("[]"))
                        ja.add(directions, value);
                    else
                        ja.set(directions, value);

                    return;
                } else {
                    JsonObject jo = new JsonObject();

                    _fields.put(item, jo);

                    jo.put(directions, value);
                    return;
                }

            }
        }

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

        Object obj = _fields.get(item);

        if (obj instanceof JsonObject) {
            return ((JsonObject) obj).get(directions);
        } else if (obj instanceof JsonArray) {
            return ((JsonArray) obj).get(directions);
        }

        return obj;

    }

    public String getString(String path) throws ParseException {
        return get(path).toString();
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

    public Enumeration<String> keys() throws ParseException {
        if (_isNullObject) {
            throw new ParseException("JsonNULL cannot contain keys!", -1);
        }
        return _fields.keys();
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

        if (!_fields.containsKey(item))
            return false;
        else if (directions.size() == 0)
            return true;

        Object obj = get(item);

        if (obj instanceof JsonObject) {
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
            return _fields.remove(item);
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
        Enumeration<String> keys = jsonObject.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

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

        int size = _fields.size() - 1;
        int i = 0;

        Enumeration<String> keys = _fields.keys();

        while (keys.hasMoreElements()) {

            String key = keys.nextElement();

            sb.append(JsonTokenizer.repeat("  ", depth + 1));
            sb.append(JsonTokenizer.escapeString(key));
            sb.append(":");

            Object obj = _fields.get(key);

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

        if (_isNullObject) {
            sb.append("null");
            return sb;
        }

        sb.append("{");

        int size = _fields.size() - 1;
        int i = 0;

        Enumeration<String> keys = _fields.keys();

        while (keys.hasMoreElements()) {

            String key = keys.nextElement();

            sb.append(JsonTokenizer.escapeString(key));
            sb.append(":");

            Object obj = _fields.get(key);

            if (obj == null) {
                sb.append("null");
            } else if (obj instanceof JsonObject) {
                sb.append(((JsonObject) obj).toStringBuilder());
            } else if (obj instanceof JsonArray) {
                sb.append(((JsonArray) obj).toStringBuilder());
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

        return sb;
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
        Enumeration<String> e = src.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();
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
        Enumeration<String> e = src.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();
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

}
