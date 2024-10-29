package me.h12z.h12z_json;

import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONObject {
    private final Map<String, Object> map;

    public JSONObject() {
        this.map = new HashMap<>();
    }

    public void add(String path, Object value) {
        String[] tokens = path.split("/");
        Object current = map;

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            boolean isLastToken = (i == tokens.length - 1);
            current = navigateAndPut(current, token, isLastToken ? value : null, isLastToken);
        }
    }

    private Object navigateAndPut(Object current, String token, Object value, boolean isLastToken) {
        Pattern arrayPattern = Pattern.compile("([a-zA-Z0-9_]+)\\[(\\d+|\\*)\\]");
        Matcher matcher = arrayPattern.matcher(token);

        if (matcher.matches()) {
            String key = matcher.group(1);
            String indexOrAsterisk = matcher.group(2);

            if (current instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) current;
                current = map.computeIfAbsent(key, k -> new ArrayList<>());
            }

            if (current instanceof List) {
                List<Object> list = (List<Object>) current;

                if (indexOrAsterisk.equals("*")) {
                    if (isLastToken) {
                        list.add(value);
                    } else {
                        list.add(new HashMap<>());
                    }
                    return list.get(list.size() - 1);
                } else {
                    int index = Integer.parseInt(indexOrAsterisk);
                    while (list.size() <= index) {
                        list.add(new HashMap<>());
                    }
                    if (isLastToken) {
                        list.set(index, value);
                    }
                    return list.get(index);
                }
            } else {
                throw new IllegalArgumentException("Ungültiger Array-Zugriff: " + token);
            }
        } else {
            if (current instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) current;
                if (isLastToken) {
                    map.put(token, value);
                } else {
                    current = map.computeIfAbsent(token, k -> new HashMap<>());
                }
                return map.get(token);
            }
        }
        return null;
    }

    public Object getAsObject(String path) {
        String[] tokens = path.split("/");
        Object current = map;

        for (String token : tokens) {
            current = navigate(current, token);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private Object navigate(Object current, String token) {
        Pattern arrayPattern = Pattern.compile("([a-zA-Z0-9_]+)\\[(\\d+)\\]");
        Matcher matcher = arrayPattern.matcher(token);

        if (matcher.matches()) {
            String key = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2));

            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            }
            if (current instanceof List && index < ((List<?>) current).size()) {
                return ((List<?>) current).get(index);
            } else {
                return null;
            }
        } else {
            if (current instanceof Map) {
                return ((Map<?, ?>) current).get(token);
            }
        }
        return null;
    }

    public JSONObject get(String key) {
        if (!(getAsObject(key) instanceof JSONObject))
            return null;
        return (JSONObject) getAsObject(key);
    }

    public JSONArray getAsJsonArray(String key) {
        if (!(getAsObject(key) instanceof JSONArray))
            return null;
        return (JSONArray) getAsObject(key);
    }

    public String getAsString(String key) {
        if (!(getAsObject(key) instanceof String))
            return null;
        return (String) getAsObject(key);
    }

    public Double getAsDouble(String key) {
        if (!(getAsObject(key) instanceof Double))
            return 0.0;
        return (Double) getAsObject(key);
    }

    public Integer getAsInteger(String key) {
        if (!(getAsObject(key) instanceof Integer))
            return 0;
        return (Integer) getAsObject(key);
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{");

        int size = this.map.size();
        int i = 0;

        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\": ");

            if (entry.getValue() instanceof JSONObject) {
                json.append(entry.getValue().toString());
            } else if (entry.getValue() instanceof JSONArray) {
                json.append(((JSONArray) entry.getValue()).toString());
            } else if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }

            if (i < size - 1) {
                json.append(", ");
            }

            i++;
        }

        json.append("}");
        return json.toString();
    }

    public void save(File file) throws IOException {

        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if(!file.exists()) file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write(this.toString());

    }

    public void save(String path) throws IOException {

        save(new File(path));

    }

}
