package com.itelg.docker.cawandu.repository.http.parser;

import org.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegistryAuthTokenParser implements Converter<String, String>
{
    @Override
    public String convert(String json)
    {
        return new JSONObject(json).getString("token");
    }
}