package com.github.liuyehcf.framework.rpc.http.test.ares;

import com.github.liuyehcf.framework.rpc.http.AresMethod;
import com.github.liuyehcf.framework.rpc.http.AresRequestBody;
import com.github.liuyehcf.framework.rpc.http.AresRequestParam;
import com.github.liuyehcf.framework.rpc.http.constant.SerializeType;
import com.github.liuyehcf.framework.rpc.http.test.model.Person;

import java.util.Map;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@SuppressWarnings("all")
public interface TestClient {

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParamWithOneMoreParam(@AresRequestParam(name = "param1") Object param);

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParamWithOneMoreRequestBody(@AresRequestBody Object param);

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParam();

    @AresMethod(path = "/zeroRequestParam", responseDeserializeType = SerializeType.json)
    String zeroRequestParamWithWrongDeserializeType();

    @AresMethod(path = "/oneRequestParam", responseDeserializeType = SerializeType.string)
    String oneRequestParamMissingParam();

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWituoutParamAnnotation(Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithBothAnnotation(@AresRequestParam(name = "param1")
                                             @AresRequestBody Object param);

    @AresMethod(path = "/oneRequestParam", responseDeserializeType = SerializeType.string)
    String oneRequestParamWithRequestBody(@AresRequestBody(serializeType = SerializeType.json) Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithStringSerializeType(@AresRequestParam(name = "param1") Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithJsonSerializeType(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithOneMoreParam(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param,
                                           @AresRequestParam(name = "param2", serializeType = SerializeType.json) Object param2);

    @AresMethod(path = "/oneRequestParam", responseDeserializeType = SerializeType.json)
    String oneRequestParamWithWrongDeserializeType(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingFirstAnnotation(Object param1,
                                                 @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingSecondAnnotation(@AresRequestParam(name = "param1") Object param1,
                                                  Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingBothAnnotation(Object param1,
                                                Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithFirstBody(@AresRequestBody Object param1,
                                        @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithSecondBody(@AresRequestParam(name = "param1") Object param1,
                                         @AresRequestBody Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithBothBody(@AresRequestBody Object param1,
                                       @AresRequestBody Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithDuplicateQueryParam(@AresRequestParam(name = "param1") Object param1,
                                                  @AresRequestParam(name = "param1") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithStringString(@AresRequestParam(name = "param1") Object param1,
                                           @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithStringJson(@AresRequestParam(name = "param1") Object param1,
                                         @AresRequestParam(name = "param2", serializeType = SerializeType.json) Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithJsonString(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param1,
                                         @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithJsonJson(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param1,
                                       @AresRequestParam(name = "param2", serializeType = SerializeType.json) Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithOneMoreParam(@AresRequestParam(name = "param1", serializeType = SerializeType.json) Object param1,
                                           @AresRequestParam(name = "param2", serializeType = SerializeType.json) Object param2,
                                           @AresRequestParam(name = "param3", serializeType = SerializeType.json) Object param3);

    @AresMethod(path = "/twoRequestParam", responseDeserializeType = SerializeType.json)
    String twoRequestParamWithWrongDeserializeType(@AresRequestParam(name = "param1") Object param1,
                                                   @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/requestBody")
    String requestBodyWithoutAnnotation(Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyWithParam(@AresRequestParam(name = "param1") Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyString(@AresRequestBody Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyJson(@AresRequestBody(serializeType = SerializeType.json) Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyWithOneMoreParam(@AresRequestBody(serializeType = SerializeType.json) Object param1,
                                       @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/requestBody", responseDeserializeType = SerializeType.json)
    String requestBodyWithWrongDeserializeType(@AresRequestBody(serializeType = SerializeType.json) Object param1);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingFirstAnnotation(Object param1,
                                                                            @AresRequestBody Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingSecondAnnotation(@AresRequestParam(name = "param1") Object param1,
                                                                             Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingBothAnnotation(Object param1,
                                                                           Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyWithWrongDeserializeType(@AresRequestParam(name = "param1") Object param1,
                                                                              @AresRequestBody Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody", responseDeserializeType = SerializeType.json)
    Map<String, Person> oneRequestParamOneRequestBody(@AresRequestParam(name = "param1") Object param1,
                                                      @AresRequestBody Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody", responseDeserializeType = SerializeType.json)
    Map<String, Person> oneRequestParamOneRequestBodyWithOneMoreParam(@AresRequestParam(name = "param1") Object param1,
                                                                      @AresRequestBody Object requestBody,
                                                                      @AresRequestParam(name = "param2", serializeType = SerializeType.json) Object param2);

    @AresMethod(path = "/nullableQueryParamAndRequestBody")
    String nullableQueryParamAndRequestBody(@AresRequestParam(name = "param1") Object param1,
                                            @AresRequestBody Object requestBody);

    @AresMethod(path = "/nullableQueryParamAndRequestBody")
    void nullableQueryParamAndRequestBodyVoidReturn1(@AresRequestParam(name = "param1") Object param1,
                                                     @AresRequestBody Object requestBody);

    @AresMethod(path = "/nullableQueryParamAndRequestBody", responseDeserializeType = SerializeType.json)
    void nullableQueryParamAndRequestBodyVoidReturn2(@AresRequestParam(name = "param1") Object param1,
                                                     @AresRequestBody Object requestBody);

    @AresMethod(path = "/returnNull")
    String returnNull();
}