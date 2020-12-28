package com.guk2zzada.runnerswar;

import java.util.HashMap;
import java.util.Map;

public class JsonCreator {
    Map<String, String> map = new HashMap<>();

    public JsonCreator() {

    }

    public JsonCreator put(String key, String value) {
        map.put(key, value);
        return new JsonCreator();
    }
}
