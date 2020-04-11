package com.mefollow.webschool.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mefollow.webschool.core.domain.BaseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class ObjectMapper {

    private static final com.fasterxml.jackson.databind.ObjectMapper mapper = objectMapper();

    public static com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
        return mapper;
    }

    public static String encode(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T decode(String json, Class<T> target) {
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T decode(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T decode(Object o, Class<T> target) {
        return mapper.convertValue(o, target);
    }

    public static <T> List<T> decodeArray(Object o, Class<T> target) {
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, target);
        return mapper.convertValue(o, listType);
    }

    public static <T extends BaseModel> BaseModel reduce(T object, String... fields) {
        if (fields == null || fields.length == 0) return object;

        MappedModel map = mapper.convertValue(object, MappedModel.class);

        BaseModel model = model().setId(object.getId());
        for (int i = fields.length - 1; i >= 0; i--) {
            String field = fields[i];
            ofNullable(map.get(field)).ifPresent(val -> model.set(field, val));
        }
        return model;
    }

    public static BaseModel model() {
        return new MappedModel();
    }

    public static class MappedModel extends BaseModel {
    }
}
