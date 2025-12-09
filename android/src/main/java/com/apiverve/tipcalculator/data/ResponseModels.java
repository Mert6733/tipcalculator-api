// Converter.java

// To use this code, add the following Maven dependency to your project:
//
//
//     com.fasterxml.jackson.core     : jackson-databind          : 2.9.0
//     com.fasterxml.jackson.datatype : jackson-datatype-jsr310   : 2.9.0
//
// Import this package:
//
//     import com.apiverve.data.Converter;
//
// Then you can deserialize a JSON string with
//
//     TipCalculatorData data = Converter.fromJsonString(jsonString);

package com.apiverve.tipcalculator.data;

import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Converter {
    // Date-time helpers

    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
            .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .appendOptional(DateTimeFormatter.ISO_INSTANT)
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    public static OffsetDateTime parseDateTimeString(String str) {
        return ZonedDateTime.from(Converter.DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime();
    }

    private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_TIME)
            .appendOptional(DateTimeFormatter.ISO_OFFSET_TIME)
            .parseDefaulting(ChronoField.YEAR, 2020)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    public static OffsetTime parseTimeString(String str) {
        return ZonedDateTime.from(Converter.TIME_FORMATTER.parse(str)).toOffsetDateTime().toOffsetTime();
    }
    // Serialize/deserialize helpers

    public static TipCalculatorData fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(TipCalculatorData obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                String value = jsonParser.getText();
                return Converter.parseDateTimeString(value);
            }
        });
        mapper.registerModule(module);
        reader = mapper.readerFor(TipCalculatorData.class);
        writer = mapper.writerFor(TipCalculatorData.class);
    }

    private static ObjectReader getObjectReader() {
        if (reader == null) instantiateMapper();
        return reader;
    }

    private static ObjectWriter getObjectWriter() {
        if (writer == null) instantiateMapper();
        return writer;
    }
}

// TipCalculatorData.java

package com.apiverve.tipcalculator.data;

import com.fasterxml.jackson.annotation.*;

public class TipCalculatorData {
    private long billAmount;
    private long tipPercentage;
    private long tipAmount;
    private long totalAmount;
    private String currency;
    private long splitBetween;
    private PerPerson perPerson;
    private CommonTipAmount[] commonTipAmounts;

    @JsonProperty("bill_amount")
    public long getBillAmount() { return billAmount; }
    @JsonProperty("bill_amount")
    public void setBillAmount(long value) { this.billAmount = value; }

    @JsonProperty("tip_percentage")
    public long getTipPercentage() { return tipPercentage; }
    @JsonProperty("tip_percentage")
    public void setTipPercentage(long value) { this.tipPercentage = value; }

    @JsonProperty("tip_amount")
    public long getTipAmount() { return tipAmount; }
    @JsonProperty("tip_amount")
    public void setTipAmount(long value) { this.tipAmount = value; }

    @JsonProperty("total_amount")
    public long getTotalAmount() { return totalAmount; }
    @JsonProperty("total_amount")
    public void setTotalAmount(long value) { this.totalAmount = value; }

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }

    @JsonProperty("split_between")
    public long getSplitBetween() { return splitBetween; }
    @JsonProperty("split_between")
    public void setSplitBetween(long value) { this.splitBetween = value; }

    @JsonProperty("per_person")
    public PerPerson getPerPerson() { return perPerson; }
    @JsonProperty("per_person")
    public void setPerPerson(PerPerson value) { this.perPerson = value; }

    @JsonProperty("common_tip_amounts")
    public CommonTipAmount[] getCommonTipAmounts() { return commonTipAmounts; }
    @JsonProperty("common_tip_amounts")
    public void setCommonTipAmounts(CommonTipAmount[] value) { this.commonTipAmounts = value; }
}

// CommonTipAmount.java

package com.apiverve.tipcalculator.data;

import com.fasterxml.jackson.annotation.*;

public class CommonTipAmount {
    private long percentage;
    private long tipAmount;
    private long total;
    private double perPerson;

    @JsonProperty("percentage")
    public long getPercentage() { return percentage; }
    @JsonProperty("percentage")
    public void setPercentage(long value) { this.percentage = value; }

    @JsonProperty("tip_amount")
    public long getTipAmount() { return tipAmount; }
    @JsonProperty("tip_amount")
    public void setTipAmount(long value) { this.tipAmount = value; }

    @JsonProperty("total")
    public long getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(long value) { this.total = value; }

    @JsonProperty("per_person")
    public double getPerPerson() { return perPerson; }
    @JsonProperty("per_person")
    public void setPerPerson(double value) { this.perPerson = value; }
}

// PerPerson.java

package com.apiverve.tipcalculator.data;

import com.fasterxml.jackson.annotation.*;

public class PerPerson {
    private long billAmount;
    private double tipAmount;
    private double totalAmount;

    @JsonProperty("bill_amount")
    public long getBillAmount() { return billAmount; }
    @JsonProperty("bill_amount")
    public void setBillAmount(long value) { this.billAmount = value; }

    @JsonProperty("tip_amount")
    public double getTipAmount() { return tipAmount; }
    @JsonProperty("tip_amount")
    public void setTipAmount(double value) { this.tipAmount = value; }

    @JsonProperty("total_amount")
    public double getTotalAmount() { return totalAmount; }
    @JsonProperty("total_amount")
    public void setTotalAmount(double value) { this.totalAmount = value; }
}