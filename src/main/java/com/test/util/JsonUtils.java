package com.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class JsonUtils {

    /**
     * 空的 {@code JSON} 数据 - <code>"{}"</code>。
     */
    public static final String EMPTY_JSON = "{}";

    /**
     * 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。
     */
    public static final String EMPTY_JSON_ARRAY = "[]";

    /**
     * 默认的 {@code JSON} 日期/时间字段的格式化模式。
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * json字符串转换成map
     * 
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> changeJsonStr2Map(String jsonStr) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(jsonStr)) {
            return jsonMap;
        }

        JSONObject json = JSONObject.parseObject(jsonStr);

        return json;
    }

    /**
     * <json字符串转换成对象>
     *
     * @param json
     *            json字符串
     * @param clazz
     *            转换的对象
     * @return T 泛型
     * @see [类、类#方法、类#成员]
     */
    public static <T> T json2Pojo(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * <对象转换成json字符串>
     *
     * @param obj
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String pojo2Json(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p/>
     * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回 <code>"{}"</code>； 集合或数组对象返回 <code>"[]"</code> </strong>
     *
     * @param target
     *            目标对象。
     * @param targetType
     *            目标对象的类型。
     * @param isSerializeNulls
     *            是否序列化 {@code null} 值字段。
     * @param datePattern
     *            日期字段的格式化模式。
     * @param excludesFieldsWithoutExpose
     *            是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, String datePattern,
        boolean excludesFieldsWithoutExpose) {
        if (target == null) {
            return EMPTY_JSON;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls) {
            builder.serializeNulls();
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        return toJson(target, targetType, builder);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target) {
        return toJson(target, null, true, null, false);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * </ul>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @param datePattern
     *            日期字段的格式化模式。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, String datePattern) {
        return toJson(target, null, true, datePattern, false);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @param excludesFieldsWithoutExpose
     *            是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, true, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
     * </ul>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @param targetType
     *            目标对象的类型。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, true, null, false);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target
     *            要转换成 {@code JSON} 的目标对象。
     * @param targetType
     *            目标对象的类型。
     * @param excludesFieldsWithoutExpose
     *            是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, true, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象根据{@code GsonBuilder} 所指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p/>
     * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，{@code JavaBean} 对象返回 <code>"{}"</code>； 集合或数组对象返回 <code>"[]"</code>。 其本基本类型，返回相应的基本值。
     *
     * @param target
     *            目标对象。
     * @param targetType
     *            目标对象的类型。
     * @param builder
     *            可定制的{@code Gson} 构建器。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.1
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) {
            return EMPTY_JSON;
        }
        Gson gson = null;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception ex) {
            log.warn("目标对象 {} 转换 JSON 字符串时，发生异常！Exception:{}", target.getClass().getName(), ex);
            if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?>
                || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     *
     * @param <T>
     *            要转换的目标类型。
     * @param json
     *            给定的 {@code JSON} 字符串。
     * @param clazz
     *            要转换的目标类。
     * @param datePattern
     *            日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (org.apache.commons.lang3.StringUtils.isBlank(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (org.apache.commons.lang3.StringUtils.isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);

        Gson gson = builder.create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception ex) {
            log.error("{} 无法转换为 {} 对象! Exception:{}", json, clazz.getName(), ex);
            return null;
        }
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     *
     * @param <T>
     *            要转换的目标类型。
     * @param json
     *            给定的 {@code JSON} 字符串。
     * @param clazz
     *            要转换的目标类。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }


    public static <T> T fromJson(String json, Type typeOfT) {
        return (T)fromJsonByTyoe( json, typeOfT,null);
    }

    public static <T> T fromJsonByTyoe(String json,Type typeOfT, String datePattern) {
        if (org.apache.commons.lang3.StringUtils.isBlank(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (org.apache.commons.lang3.StringUtils.isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);

        Gson gson = builder.create();
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception ex) {
            log.error("{} 无法转换为 {} 对象! Exception:{}", json, typeOfT, ex);
            return null;
        }
    }

    /**
     * 如果对象的属性值为null,做对应转换
     *
     * @param obj
     * @return
     */
    public static String toJsonSerializeNull(Object obj) {

        return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteSlashAsSpecial,
            SerializerFeature.WriteNullBooleanAsFalse);
    }

    /**
     * 如果对象的属性值为null,做对应转换
     *
     * @param obj
     * @return
     */
    public static String toJsonNotSerializeNull(Object obj) {

        return toJson(obj, null, false, null, false);
    }

    /**
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

            ArrayList<T> arrayList = new ArrayList<T>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
        } catch (Exception ex) {
            log.error("{} 无法转换为 {} 对象列表! Exception:{}", json, clazz.getName(), ex);
            return null;
        }

    }

    /**
     * 如果对象的属性值为null,做对应转换,并根据日期格式做相应转换
     *
     * @param obj
     * @return
     */
    public static String toJsonSerializeNullWithDateFormat(Object obj, String dateFormat) {

        return JSONObject.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteSlashAsSpecial,
            SerializerFeature.WriteNullBooleanAsFalse);
    }

    /**
     * 当属性的值有特殊字符时，不做转换例如>,<,=等
     *
     * @param target
     * @return
     */
    public static String toJsonDisableHtmlEscaping(Object target) {
        Gson gs = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gs.toJson(target);
    }
}
