/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wander.baseframe.library.net.retrofit.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class ScalarResponseBodyConverters {
    private ScalarResponseBodyConverters() {
    }

    public static final class StringResponseBodyConverter implements Converter<ResponseBody, String> {
        static final StringResponseBodyConverter INSTANCE = new StringResponseBodyConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    public static final class StringDataBodyConverter implements Converter<ResponseBody, ResponseData<String>> {
        static final StringDataBodyConverter INSTANCE = new StringDataBodyConverter();

        @Override
        public ResponseData<String> convert(ResponseBody value) throws IOException {
            ResponseData<String> responseData = new ResponseData<>();
            responseData.setCode("error");
            String s = value.string();
            try {
                if (!TextUtils.isEmpty(s)) {
                    JSONObject jsonObject = new JSONObject(s);
                    responseData.setCode(jsonObject.optString("code"));
                    responseData.setMsg(jsonObject.optString("msg"));
                    boolean hasData = jsonObject.has("data");
                    if (hasData) {
                        String data = jsonObject.optString("data");
                        responseData.setData(data);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseData;
        }
    }

    public static final class ObjectResponseBodyConverter implements Converter<ResponseBody, JSONObject> {
        static final ObjectResponseBodyConverter INSTANCE = new ObjectResponseBodyConverter();

        @Override
        public JSONObject convert(ResponseBody value) throws IOException {
            String string = value.string();
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            try {
                return new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static final class SimpleResponseBodyConverter implements Converter<ResponseBody, ResponseData> {
        private final Gson gson;
        private final TypeAdapter<ResponseDataSimple> adapter;

        SimpleResponseBodyConverter(Gson gson, TypeAdapter<ResponseDataSimple> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public ResponseData convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader).toResponseData();
            } finally {
                value.close();
            }
        }
    }

    static final class ByteResponseBodyConverter implements Converter<ResponseBody, byte[]> {
        static final ByteResponseBodyConverter INSTANCE = new ByteResponseBodyConverter();

        @Override
        public byte[] convert(ResponseBody value) throws IOException {
            return value.bytes();
        }
    }

    static final class CharacterResponseBodyConverter implements Converter<ResponseBody, Character> {
        static final CharacterResponseBodyConverter INSTANCE = new CharacterResponseBodyConverter();

        @Override
        public Character convert(ResponseBody value) throws IOException {
            String body = value.string();
            if (body.length() != 1) {
                throw new IOException(
                        "Expected body of length 1 for Character conversion but was " + body.length());
            }
            return body.charAt(0);
        }
    }

    static final class DoubleResponseBodyConverter implements Converter<ResponseBody, Double> {
        static final DoubleResponseBodyConverter INSTANCE = new DoubleResponseBodyConverter();

        @Override
        public Double convert(ResponseBody value) throws IOException {
            return Double.valueOf(value.string());
        }
    }

    static final class FloatResponseBodyConverter implements Converter<ResponseBody, Float> {
        static final FloatResponseBodyConverter INSTANCE = new FloatResponseBodyConverter();

        @Override
        public Float convert(ResponseBody value) throws IOException {
            return Float.valueOf(value.string());
        }
    }

    static final class IntegerResponseBodyConverter implements Converter<ResponseBody, Integer> {
        static final IntegerResponseBodyConverter INSTANCE = new IntegerResponseBodyConverter();

        @Override
        public Integer convert(ResponseBody value) throws IOException {
            return Integer.valueOf(value.string());
        }
    }

    static final class LongResponseBodyConverter implements Converter<ResponseBody, Long> {
        static final LongResponseBodyConverter INSTANCE = new LongResponseBodyConverter();

        @Override
        public Long convert(ResponseBody value) throws IOException {
            return Long.valueOf(value.string());
        }
    }

    static final class ShortResponseBodyConverter implements Converter<ResponseBody, Short> {
        static final ShortResponseBodyConverter INSTANCE = new ShortResponseBodyConverter();

        @Override
        public Short convert(ResponseBody value) throws IOException {
            return Short.valueOf(value.string());
        }
    }
}
