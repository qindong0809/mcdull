package io.gitee.dqcer.mcdull.frameowrk.mongodb.wrapper;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.ObjectUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class CriteriaVo extends Criteria {

    public static CriteriaVo create(){
        return new CriteriaVo();
    }


    /**
     * 精确匹配
     * @param fun
     * @param value
     * @return
     */
    public <T> CriteriaVo isN (SFunction<T,Object> fun,Object value){
        if(!ObjectUtils.isEmpty(value)){
            this.and(columnToString(fun)).is(value);
        }
        return this;
    }

    /**
     * 大于
     * @param fun
     * @param value
     * @param <T>
     * @return
     */
    public <T> CriteriaVo gteN (SFunction<T,Object> fun,Object value){
        if(!ObjectUtils.isEmpty(value)){
            this.and(columnToString(fun)).gte(value);
        }
        return this;
    }

    /**
     * 小于
     * @param fun
     * @param value
     * @param <T>
     * @return
     */
    public <T> CriteriaVo lteN (SFunction<T,Object> fun,Object value){
        if(!ObjectUtils.isEmpty(value)){
            this.and(columnToString(fun)).lte(value);
        }
        return this;
    }

    public <T,R> CriteriaVo elemMatchN (SFunction<T,Object> fun1, SFunction<R,Object> fun2, Object value){
        if(!ObjectUtils.isEmpty(value)){
            this.and(columnToString(fun1)).elemMatch(Criteria.where(columnToString(fun2)).is(value));
        }
        return this;
    }


    public static <T> String columnToString(SFunction<T, ?> func) {
        String fieldName = "";
        try {
            Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(func);
            String methodName = serializedLambda.getImplMethodName();
            if(methodName.startsWith("get") || methodName.startsWith("set")){
                fieldName =  methodName.substring(3);
            }else {
                fieldName = methodName;
            }
            fieldName = Introspector.decapitalize(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fieldName;
    }
}
