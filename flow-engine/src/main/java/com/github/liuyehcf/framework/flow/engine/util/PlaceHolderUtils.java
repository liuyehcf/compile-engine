package com.github.liuyehcf.framework.flow.engine.util;

import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public abstract class PlaceHolderUtils {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([a-zA-Z_]([0-9a-zA-Z_])*(\\.[a-zA-Z_]([0-9a-zA-Z_])*)*)\\}");

    public static Object parsePlaceHolder(Map<String, Object> env, final String originValue) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(originValue);

        if (!matcher.matches()) {
            return originValue;
        }

        try {
            return PropertyUtils.getProperty(env, matcher.group(1));
        } catch (Exception e) {
            throw new FlowException(FlowErrorCode.PLACE_HOLDER, e);
        }
    }
}
