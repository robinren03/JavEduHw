package com.example.renyanyu;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.alibaba.fastjson.JSON;

class Converter {
    @TypeConverter
    @Nullable
    public static Date fromTimestamp(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return sdf.parse(data, pos);
    }

    @TypeConverter
    public static String toTimestamp(Date data){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(data);
    }

}

class ListConverter{
    @TypeConverter
    public static List<Map<String, String>> fromTimestamp(String data){
        List<Object> list = JSON.parseArray(data);
        List<Map<String, String>> listw = new ArrayList<Map<String, String>>();
        for (Object object : list) {
            Map<String, String> proMap = (Map<String, String>) object;
            listw.add(proMap);
        }
        return listw;
    }

    @TypeConverter
    public static String toTimestamp(List<Map<String, String>> data){
        return data.toString();
    }
}
