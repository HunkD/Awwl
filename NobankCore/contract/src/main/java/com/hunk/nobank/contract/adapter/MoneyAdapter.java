package com.hunk.nobank.contract.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hunk.nobank.contract.Money;

import java.lang.reflect.Type;

/**
 *
 */
public class MoneyAdapter implements JsonSerializer<Money>, JsonDeserializer<Money> {
    @Override
    public Money deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.getAsString().length() == 0 ? null : new Money(json.getAsString());
    }

    @Override
    public JsonElement serialize(Money src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.string());
    }
}
