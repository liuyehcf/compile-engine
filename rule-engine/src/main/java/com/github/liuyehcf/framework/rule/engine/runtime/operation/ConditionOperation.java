package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateResult;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;

/**
 * must guarantee optPromise will end in any cases
 *
 * @author hechenfeng
 * @date 2019/4/30
 */
class ConditionOperation extends AbstractOperation<Boolean> {

    private final Condition condition;
    private volatile DelegateResult delegateResult;
    private volatile boolean conditionOutput;

    ConditionOperation(OperationContext context, Condition condition) {
        this(context, condition, null);
    }

    ConditionOperation(OperationContext context, Condition condition, Promise<Boolean> optPromise) {
        super(context, optPromise);
        Assert.assertNotNull(condition);
        this.condition = condition;
    }

    @Override
    void operate() throws Throwable {
        context.setNode(condition);

        invokeNodeBeforeListeners(condition, this::conditionCondition);
    }

    private void conditionCondition() throws Throwable {
        DelegateInvocation delegateInvocation = context.getDelegateInvocation(condition, null, null);

        try {
            delegateResult = delegateInvocation.proceed();
        } catch (Throwable e) {
            invokeNodeFailureListeners(condition, e, () -> {
                if (optPromise != null) {
                    optPromise.tryFailure(e);
                }
                throwCause(e);
            });
            return;
        }

        if (delegateResult.isAsync()) {
            delegateResult.getDelegatePromise().addListener(promise -> processAsyncPromise(promise, this::continueSuccessListener));
        } else {
            continueSuccessListener();
        }
    }

    private void continueSuccessListener() throws Throwable {
        if (delegateResult.isAsync()) {
            conditionOutput = (boolean) delegateResult.getDelegatePromise().get();
        } else {
            conditionOutput = (boolean) delegateResult.getResult();
        }

        invokeNodeSuccessListeners(condition, conditionOutput, this::continueForward);
    }

    private void continueForward() {
        context.setConditionOutput(condition, conditionOutput);
        LinkType linkType = conditionOutput ? LinkType.TRUE : LinkType.FALSE;
        LinkType unReachableLinkType = conditionOutput ? LinkType.FALSE : LinkType.TRUE;

        if (optPromise != null) {
            optPromise.trySuccess(conditionOutput);
        }

        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), condition, unReachableLinkType));

        context.markElementFinished(condition);

        forward(linkType, condition.getSuccessors());
    }
}