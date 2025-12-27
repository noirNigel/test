package org.example.demomanagementsystemcproject.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 注册一个 SimpleModule（作为 Bean）——Spring Boot 会自动把它注册到默认的 ObjectMapper 中。
 * 支持前端传入毫秒时间戳(long 毫秒)或 yyyy-MM-dd HH:mm:ss 字符串，
 * 并统一序列化 LocalDateTime 为 yyyy-MM-dd HH:mm:ss 字符串。
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    @Bean
    public Module localDateTimeModule() {
        SimpleModule module = new SimpleModule("LocalDateTimeModule");

        // 反序列化：支持毫秒数或格式化字符串
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonToken t = p.currentToken();
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    long ts = p.getLongValue();
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZONE);
                } else {
                    String text = p.getText();
                    if (text == null) return null;
                    text = text.trim();
                    if (text.isEmpty()) return null;
                    try {
                        return LocalDateTime.parse(text, DTF);
                    } catch (Exception ex) {
                        return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                }
            }
        });

        // 序列化：统一输出为 yyyy-MM-dd HH:mm:ss 字符串
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(value.format(DTF));
                }
            }
        });

        return module;
    }
}
