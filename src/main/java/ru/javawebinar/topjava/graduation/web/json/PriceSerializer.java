package ru.javawebinar.topjava.graduation.web.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceSerializer extends JsonSerializer<Integer> {

    static final BigDecimal PRECISION = BigDecimal.valueOf(100);

    @Override
    public void serialize(Integer price, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(getDecimalPrice(price));
    }

    @Override
    public Class<Integer> handledType() {
        return Integer.class;
    }

    private BigDecimal getDecimalPrice(Integer price) {
        return BigDecimal.valueOf(price).divide(PRECISION, 2, RoundingMode.HALF_UP);
    }
}