package com.itelg.docker.cawandu.repository.http.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class RegistryAuthTokenParser extends AbstractJsonConverter<String>
{
    @Override
    public String convertJson(String json) throws JSONException
    {
        return new JSONObject(json).getString("token");
    }
}