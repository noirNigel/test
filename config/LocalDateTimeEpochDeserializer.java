package org.example.demomanagementsystemcproject.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 支持两种输入：
 *  - 毫秒时间戳（long，毫秒）：1764432000000
 *  - 格式化字符串："yyyy-MM-dd HH:mm:ss" 或 ISO_LOCAL_DATE_TIME
 *
 *  反序列化为 LocalDateTime（按 Asia/Shanghai 时区从 epoch 毫秒转换）。
 */
public class LocalDateTimeEpochDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.currentToken();

        // 如果是数字（毫秒）
        if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
            long ts = p.getLongValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZONE);
        }

        // 否则按字符串解析
        String text = p.getText();
        if (text == null) return null;
        text = text.trim();
        if (text.isEmpty()) return null;

        try {
            return LocalDateTime.parse(text, DTF);
        } catch (Exception ex) {
            // 备选：ISO_LOCAL_DATE_TIME（例如 "2025-11-30T15:00:00"）
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
