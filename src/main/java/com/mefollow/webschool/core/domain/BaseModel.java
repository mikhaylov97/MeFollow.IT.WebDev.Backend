package com.mefollow.webschool.core.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mefollow.webschool.core.json.ObjectMapper.decode;
import static com.mefollow.webschool.core.json.ObjectMapper.encode;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

@SuppressWarnings("unchecked")
public abstract class BaseModel {

    @Id
    protected String id;

    @Transient
    private Map<String, Object> fluidFields = new LinkedHashMap<>();

    public Object get(String key) {
        return fluidFields.get(key);
    }

    public Object get(AdditionalFluidFields key) {
        return fluidFields.get(key.getFieldName());
    }

    public <T> T get(String key, Class<T> clazz) {
        return decode(fluidFields.get(key), clazz);
    }

    public <T> T get(AdditionalFluidFields key, Class<T> clazz) {
        return decode(fluidFields.get(key.getFieldName()), clazz);
    }

    public <T> List<T> getArray(AdditionalFluidFields key, Class<T> clazz) {
        return (List<T>) fluidFields.get(key.getFieldName());
    }

    @JsonAnyGetter
    public Map<String, Object> getFluidFields() {
        return fluidFields;
    }

    @JsonAnySetter
    public <T extends BaseModel> T set(String key, Object value) {
        if (isNull(key) || key.isEmpty()) throw new IllegalStateException("custom field key must be non empty");
        fluidFields.put(key, value);
        return (T) this;
    }

    public <T extends BaseModel> T set(AdditionalFluidFields key, Object value) {
        if (isNull(key) || key.getFieldName().isEmpty()) throw new IllegalStateException("custom field key must be non empty");
        fluidFields.put(key.getFieldName(), value);
        return (T) this;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getId() {
        return id;
    }

    public BaseModel setId(String id) {
        this.id = id;
        return this;
    }

    public boolean nonEquals(Object o) {
        return !equals(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseModel model = (BaseModel) o;
        return Objects.equals(id, model.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String collect = fluidFields.entrySet().stream().map(so -> so.getKey() + "=" + encode(so.getValue())).collect(joining(", "));
        return "Model{" +
                "id='" + id + '\'' +
                collect +
                '}';
    }
}
