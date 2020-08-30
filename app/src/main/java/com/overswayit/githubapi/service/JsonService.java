package com.overswayit.githubapi.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class JsonService {

    private static final Gson gson;
    private static final JsonParser parser;

    static {
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        parser = new JsonParser();
    }

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> classy) {
        if (jsonString == null) {
            return null;
        }

        JsonElement je = parser.parse(jsonString);
        return fromJson(je, classy);
    }

    public static <T> T fromJson(JsonElement je, Class<T> classy) {
        return gson.fromJson(je, classy);
    }

    public static <T> ArrayList<T> arrayListFromJson(String jsonString, Class<T> elemClass) {
        return arrayListFromJson(parser.parse(jsonString), elemClass);
    }

    public static <T> ArrayList<T> arrayListFromJson(JsonElement je, Class<T> elemClass) {
        T[] ts = (T[]) fromJson(je, Array.newInstance(elemClass, 0).getClass());
        return new ArrayList<>(Arrays.asList(ts));
    }

    public static String fromMap(Map<String, Object> map) {
        return gson.toJson(map);
    }

    public static String getProperty(String jsonString, String propertyName) {
        JsonElement je = parser.parse(jsonString);

        if (je.isJsonObject()) {
            JsonElement obj = ((JsonObject) je).get(propertyName);

            if (obj != null) {
                return obj.getAsString();
            }
        }

        return null;
    }

    public static String optProperty(String jsonString, String propertyName) {
        try {
            return getProperty(jsonString, propertyName);
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static JsonElement parseJson(String jsonString) {
        try {
            return parser.parse(jsonString);
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    public static JsonElement getProperty(JsonObject jsonObject, String... properties) {
        JsonElement result = jsonObject;

        try {

            for (String p : properties) {
                result = ((JsonObject) result).get(p);
            }

        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    public static HashMap<String, Object> parseAsStringToObjectHashMap(JsonElement element) {
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        return gson.fromJson(element, type);
    }

    public static String asString(JsonElement element, String defaultVal) {
        try {
            return element.getAsString();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static boolean asBoolean(JsonElement element, boolean defaultVal) {
        try {
            return element.getAsBoolean();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Integer asInt(JsonElement element, Integer defaultVal) {
        try {
            return element.getAsInt();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static double asDouble(JsonElement element, double defaultVal) {
        try {
            return element.getAsDouble();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static JsonObject asJsonObject(JsonElement element) {
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }

        return null;
    }

    public static JsonObject asJsonObject(HashMap<String, Object> map) {
        return gson.toJsonTree(map).getAsJsonObject();
    }

    public static JsonArray asJsonArray(JsonElement element) {
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        return null;
    }

    public static HashMap<String, Object> asHashMap(String parentKey, JsonObject json) {
        HashMap<String, Object> temp = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                temp.put(entry.getKey(), asHashMap(entry.getKey() + ".", JsonService.asJsonObject(entry.getValue())));
            } else if (entry.getValue().isJsonArray()) {
                JsonArray jsonArray = entry.getValue().getAsJsonArray();
                ArrayList<HashMap<String, Object>> listOfMaps = new ArrayList<>();

                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    listOfMaps.add(asHashMap("", jsonObject));
                }

                temp.put(entry.getKey(), listOfMaps);
            } else {
                JsonPrimitive primitive;
                if (entry.getValue().isJsonPrimitive()) {
                    primitive = entry.getValue().getAsJsonPrimitive();
                } else if (entry.getValue().isJsonNull()) {
                    temp.put(entry.getKey(), null);
                    continue;
                } else {
                    String something = entry.getValue().getAsString();
                    primitive = new Gson().fromJson(something, JsonPrimitive.class);
                }

                if (primitive.isBoolean()) {
                    temp.put(entry.getKey(), entry.getValue().getAsBoolean());
                } else {
                    temp.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }

        return temp;
    }

}
