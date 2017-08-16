package com.sanjie.zy.http.converter;


import com.sanjie.zy.utils.log.ZYLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by LangSanJie on 2017/2/17.
 */

final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "JsonResponseBodyConverter";

    JsonResponseBodyConverter(){
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JSONObject object;
        try {
            object = new JSONObject(value.string());
            return (T) object;
        } catch(JSONException e) {
            ZYLog.e("Converter JSONException:" + e.getMessage());
            return null;
        }
    }
}
