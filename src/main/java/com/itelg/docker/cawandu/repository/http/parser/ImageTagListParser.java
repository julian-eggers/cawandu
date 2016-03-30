package com.itelg.docker.cawandu.repository.http.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageTagListParser implements Converter<String, List<String>>
{
    @Override
    public List<String> convert(String json)
    {
        JSONArray jsonArray = new JSONArray(json);
        List<String> tags = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++)
        {
            tags.add(jsonArray.getJSONObject(i).get("name").toString());
        }
        
        return tags;
    }
}