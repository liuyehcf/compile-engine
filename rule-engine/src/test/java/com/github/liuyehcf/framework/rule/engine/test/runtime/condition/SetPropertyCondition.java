package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyCondition extends BaseCondition {

    private DelegateField name;
    private DelegateField value;
    private DelegateField output;

    @Override
    boolean doCondition(ConditionContext context) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();
        boolean conditionOutput = output.getValue();

        context.setProperty(propertyName, propertyValue);

        return conditionOutput;
    }
}