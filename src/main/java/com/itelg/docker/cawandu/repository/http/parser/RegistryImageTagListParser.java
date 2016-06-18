package com.itelg.docker.cawandu.repository.http.parser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegistryImageTagListParser implements Converter<String, List<String>>
{
    @Override
    public List<String> convert(String json)
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