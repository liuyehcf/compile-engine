package com.github.liuyehcf.framework.rpc.ares.test.controller;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@RestController
public class FeatureController {

    @RequestMapping("/zeroRequestParam")
    @ResponseBody
    public String zeroRequestParam() {
        return "zeroRequestParam()[]";
    }

    @RequestMapping("/oneRequestParam")
    @ResponseBody
    public String oneRequestParam(@RequestParam("param1") String param1) {
        return String.format("oneRequestParam(%s)[]", param1);
    }

    @RequestMapping("/twoRequestParam")
    @ResponseBody
    public String twoRequestParam(@RequestParam("param1") String param1,
                                  @RequestParam("param2") String param2) {
        return String.format("twoRequestParam(%s, %s)[]", param1, param2);
    }

    @RequestMapping("/oneRequestHeader")
    @ResponseBody
    public String oneRequestHeader(@RequestHeader("header1") String header1) {
        return String.format("oneRequestHeader(%s)[]", header1);
    }

    @RequestMapping("/twoRequestHeader")
    @ResponseBody
    public String twoRequestHeader(@RequestHeader("header1") String header1,
                                   @RequestHeader("header2") String header2) {
        return String.format("twoRequestHeader(%s, %s)[]", header1, header2);
    }

    @RequestMapping("/requestBody")
    @ResponseBody
    public String requestBody(@RequestBody String requestBody) {
        return String.format("requestBody()[%s]", requestBody);
    }

    @RequestMapping("/oneRequestParamOneRequestBody")
    @ResponseBody
    public String oneRequestParamOneRequestBody(@RequestParam("param1") String param1, @RequestBody String requestBody) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(param1, Person.builder()
                .name(requestBody)
                .build());
        return JSON.toJSONString(map);
    }

    @RequestMapping("/nullableQueryParamAndRequestBody")
    @ResponseBody
    public String nullableQueryParamAndRequestBody(@RequestParam(value = "param1", required = false) String param1, @RequestBody(required = false) String requestBody) {
        if (param1 == null && requestBody == null) {
            return "both null";
        } else if (param1 == null) {
            return "param1 null";
        } else if (requestBody == null) {
            return "requestBody null";
        } else {
            return "both not null";
        }
    }

    @RequestMapping("/differentPathVariable/{one}/{another}")
    @ResponseBody
    public String differentPathVariable(@PathVariable(value = "one") String one, @PathVariable(value = "another") String another) {
        return String.format("/differentPathVariable/%s/%s", one, another);
    }

    @RequestMapping("/samePathVariable/{one}/{one}")
    @ResponseBody
    public String samePathVariable(@PathVariable(value = "one") String one) {
        return String.format("/samePathVariable/%s/%s", one, one);
    }

    @RequestMapping("/returnNull")
    @ResponseBody
    public String returnNull() {
        return null;
    }

    @RequestMapping("/customizeContentType")
    public String customizeContentType(HttpServletRequest request) {
        return request.getContentType();
    }

    @RequestMapping("/status500")
    public void status404(HttpServletResponse response) throws Exception {
        response.sendError(500, "server error");
    }
}
