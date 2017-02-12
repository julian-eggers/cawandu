package com.itelg.docker.cawandu.repository.http.parser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class RegistryImageTagListParser extends AbstractJsonConverter<List<String>>
{
    @Override
    public List<String> convertJson(String json) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("tags");
        List<String> tags = new LinkedList<>();

        for (int i = 0; i < jsonArray.length(); i++)
        {
            tags.add(jsonArray.getString(i));
        }

        Collections.sort(tags);
        Collections.reverse(tags);
        return tags;
    }
}