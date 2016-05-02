package com.hunk.nobank.contract.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.hunk.nobank.contract.Money;

import java.io.IOException;

/**
 *
 */
public class MoneyAdapter extends TypeAdapter<Money> {
    @Override
    public void write(JsonWriter jsonWriter, Money money) throws IOException {
        if (money != null) {
            jsonWriter.value(money.string());
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public Money read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            String jsonStr = jsonReader.nextString();
            if (jsonStr.length() != 0) {
                try {
                    return new Money(jsonStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
