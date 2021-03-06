package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@Component
public class ComponentListener implements ListenerDelegate {
    @Override
    public void onBefore(ListenerContext context) {
        System.out.println(getClass().getSimpleName());
    }
}
