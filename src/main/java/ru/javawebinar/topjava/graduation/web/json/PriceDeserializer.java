package ru.javawebinar.topjava.graduation.web.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import static ru.javawebinar.topjava.graduation.web.json.PriceSerializer.PRECISION;

public class PriceDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return getIntegerPrice(p.getDecimalValue());
    }

    @Override
    public Class<Integer> handledType() {
        return Integer.class;
    }

    private Integer getIntegerPrice(BigDecimal price) {
        return price.multiply(PRECISION, MathContext.DECIMAL32).intValue();
    }
}
