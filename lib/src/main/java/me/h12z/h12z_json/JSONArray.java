package me.h12z.h12z_json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {
    private final List<Object> list;

    public JSONArray() {
        this.list = new ArrayList<>();
    }

    public void add(Object value) {
        list.add(value);
    }

    public JSONObject get(int index) {
        if (!(list.get(index) instanceof JSONObject))
            return null;
        return (JSONObject) list.get(index);
    }

    public JSONArray getAJsonArray(int index) {
        if (!(list.get(index) instanceof JSONArray))
            return null;
        return (JSONArray) list.get(index);
    }

    public String getAsString(int index) {
        if (!(list.get(index) instanceof String))
            return null;
        return (String) list.get(index);
    }

    public Integer getAsInteger(int index) {
        if (!(list.get(index) instanceof Integer))
            return null;
        return (Integer) list.get(index);
    }

    public Double getAsDouble(int index) {
        if (!(list.get(index) instanceof Double))
            return null;
        return (Double) list.get(index);
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("[");

        int size = this.list.size();
        int i = 0;

        for (Object element : this.list) {
            if (element instanceof JSONObject) {
                json.append(element.toString());
            } else if (element instanceof JSONArray) {
                json.append(((JSONArray) element).toString());
            } else if (element instanceof String) {
                json.append("\"").append(element).append("\"");
            } else {
                json.append(element);
            }

            if (i < size - 1) {
                json.append(", ");
            }

            i++;
        }

        json.append("]");
        return json.toString();
    }

}
