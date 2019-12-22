package com.wander.baseframe.library.net.retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Bruce on 2017/7/17.
 */

public class ByteConvertFactory extends Converter.Factory {

    public static ByteConvertFactory create() {
        return new ByteConvertFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, byte[]>() {
            @Override
            public byte[] convert(ResponseBody value) throws IOException {
                return value.bytes();
            }
        };
    }
}
