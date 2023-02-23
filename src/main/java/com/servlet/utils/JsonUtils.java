package com.servlet.utils;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

final public class JsonUtils {
    private final Gson gson = new Gson();
    
    public final String toJson(Object target) {
        return gson.toJson(target);
    }
    
    public final <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, TypeToken.get(classOfT));
    }
    
    public final <T> T fromJson(Reader jsonReader, Class<T> classOfT) {
        return gson.fromJson(jsonReader, TypeToken.get(classOfT));
    }
}
