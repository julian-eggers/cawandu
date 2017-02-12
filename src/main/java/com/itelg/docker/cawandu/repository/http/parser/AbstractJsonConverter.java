package com.itelg.docker.cawandu.repository.http.parser;

import org.json.JSONException;
import org.springframework.core.convert.converter.Converter;

public abstract class AbstractJsonConverter<T> implements Converter<String, T>
{
    @Override
    public T convert(String json)
    {
        try
        {
            return convertJson(json);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected abstract T convertJson(String json) throws JSONException;
}