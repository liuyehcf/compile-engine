package com.github.liuyehcf.framework.compile.engine.utils;

/**
 * 字符工具类
 *
 * @author hechenfeng
 * @date 2018/6/29
 */
public abstract class CharacterUtil {

    public static boolean isBlankChar(char c) {
        return 9 <= c && c <= 13 || c == 32;
    }
}
