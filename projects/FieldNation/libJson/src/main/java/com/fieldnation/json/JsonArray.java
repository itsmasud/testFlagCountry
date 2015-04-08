package com.fieldnation.json;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class JsonArray {
    private List<Object> _objects = new LinkedList<Object>();

    public JsonArray() {
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
            _objects.add(tokenizer.parseValue());

            // if not comma, then it should've been a ']'
            if (!tokenizer.nextToken().equals(",")) {
                break;
            }
        }

    }

    public int size() {
        return _objects.size();
    }

    public Object get(int index) {
        return _objects.get(index);
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

        Object obj = _objects.get(item);

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
        while (index >= _objects.size())
            _objects.add(null);

        _objects.set(index, value);
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
            if (_objects.size() > index) {
                Object obj = _objects.get(index);

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

    public JsonArray add(Object value) {
        _objects.add(value);
        return this;
    }

    public JsonArray add(int index, Object value) {
        _objects.add(index, value);
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

                _objects.add(ja);

                if (child.equals("[]"))
                    ja.add(directions, value);
                else
                    ja.set(directions, value);
            } else {
                JsonObject jo = new JsonObject();

                _objects.add(jo);

                jo.put(directions, value);
            }
        }
        return this;
    }

    public Object remove(int index) {
        return _objects.remove(index);
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
        _objects.clear();
        return this;
    }

    public String display() {
        return display(0).toString();
    }

    protected StringBuilder display(int depth) {
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");
        for (int i = 0; i < _objects.size(); i++) {
            Object obj = _objects.get(i);

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

            if (i < _objects.size() - 1) {
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
        for (int i = 0; i < _objects.size(); i++) {
            Object obj = _objects.get(i);

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

            if (i < _objects.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
    }

    public boolean has(int index) {
        if (index < _objects.size())
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

}
