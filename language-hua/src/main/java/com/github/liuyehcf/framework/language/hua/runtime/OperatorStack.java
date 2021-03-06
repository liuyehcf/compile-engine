package com.github.liuyehcf.framework.language.hua.runtime;

import java.util.LinkedList;

/**
 * 操作数栈
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
public class OperatorStack {

    private final LinkedList<Object> stack = new LinkedList<>();

    public void push(Object value) {
        stack.push(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T pop() {
        return (T) stack.pop();
    }
}
