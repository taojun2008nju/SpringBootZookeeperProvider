package com.test.util;

import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author chenx
 * @date 2020/5/18
 */
public class SpElUtil {

    /**
     * 获取spel 定义的参数值
     *
     * @param arguments 参数容器
     * @param key     key
     * @param signatureMethod
     * @return 参数值
     */
    public static String getValue(String key,Object[] arguments, Method signatureMethod) {
        EvaluationContext context = getContext(arguments, signatureMethod);
        if(context == null){
            return null;
        }
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        return expression.getValue(context, String.class);
    }

    /**
     * 获取spel 定义的参数值数组
     *
     * @param arguments 参数容器
     * @param key     key
     * @param signatureMethod
     * @return 参数值
     */
    public static List<String> getValues(String key,Object[] arguments, Method signatureMethod) {
        EvaluationContext context = getContext(arguments, signatureMethod);
        if(context == null){
            return null;
        }
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        Object value = expression.getValue(context);
        if(value == null){
            return null;
        }
        if(value instanceof List){
            return (List<String>) ((List) value).stream().map(o -> String.valueOf(o)).collect(Collectors.toList());
        }else {
            return Lists.newArrayList(String.valueOf(value));
        }
    }

    /**
     * 获取参数容器
     *
     * @param arguments       方法的参数列表
     * @param signatureMethod 被执行的方法体
     * @return 装载参数的容器
     */
    private static EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
        if(ArrayUtils.isEmpty(arguments)){
            return null;
        }
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(signatureMethod);
        if (ArrayUtils.isEmpty(parameterNames)) {
            return null;
        }

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }
        return context;
    }


//    public static void main(String[] args) {
//        EvaluationContext context = new StandardEvaluationContext();
//        List<String> list = Lists.newArrayList("1","2","3");
//        context.setVariable("list",list);
//        ItemPriceQuery itemPriceQuery = new ItemPriceQuery();
//        itemPriceQuery.setItemCode("123");
//        context.setVariable("itemPriceQuery",itemPriceQuery);
//        String key = "T(com.ysten.vas.constant.CacheKeyConstants).CACHE_PRODUCT_ID+#list";
//        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
//        Expression expression = spelExpressionParser.parseExpression(key);
//        Object value = expression.getValue(context);
//        System.out.println(value);
//
//        String key1 = "T(com.ysten.vas.constant.CacheKeyConstants).CACHE_PRODUCT_ID+#itemPriceQuery";
//        SpelExpressionParser spelExpressionParser1 = new SpelExpressionParser();
//        Expression expression1 = spelExpressionParser1.parseExpression(key1);
//        Object value1 = expression1.getValue(context);
//        System.out.println(value1);
//    }
}
