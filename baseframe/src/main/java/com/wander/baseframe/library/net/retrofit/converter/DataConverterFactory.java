package com.wander.baseframe.library.net.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 */
public final class DataConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static DataConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static DataConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new DataConverterFactory(gson);
    }

    private final Gson gson;

    private DataConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //含有泛型的类型，处理特殊的泛型
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();   // 泛型的实际类型
            Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0]; // 泛型的参数
            if (rawType == ResponseData.class) {
                //通用结构 均返回 ResponseData
                if (typeArgument == Void.class) {
                    //不关心回包的数据，只处理code msg
                    TypeAdapter<ResponseDataSimple> adapter = gson.getAdapter(TypeToken.get(ResponseDataSimple.class));
                    return new ScalarResponseBodyConverters.SimpleResponseBodyConverter(gson, adapter);
                }
                //把data解析为string
                if (typeArgument == String.class) {
                    return ScalarResponseBodyConverters.StringDataBodyConverter.INSTANCE;
                }
            }
        } else if (type instanceof Class) {
            return parseType(type, annotations, retrofit);
        }
        return defaultConverter(type, annotations, retrofit);
    }

    private Converter<ResponseBody, ?> defaultConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter);
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }


    private Converter<ResponseBody, ?> parseType(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return ScalarResponseBodyConverters.StringResponseBodyConverter.INSTANCE;
        }
        if (type == JSONObject.class) {
            return ScalarResponseBodyConverters.ObjectResponseBodyConverter.INSTANCE;
        }
        return defaultConverter(type, annotations, retrofit);
    }
}