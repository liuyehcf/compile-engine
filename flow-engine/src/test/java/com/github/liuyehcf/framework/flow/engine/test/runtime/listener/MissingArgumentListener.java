package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
@SuppressWarnings("all")
public class MissingArgumentListener extends BaseListener {

    private DelegateField notMissing1;
    private DelegateField notMissing2;
    private DelegateField missing1;
    private DelegateField missing2;

    public void setNotMissing1(DelegateField notMissing1) {
        this.notMissing1 = notMissing1;
    }

    public void setMissing1(DelegateField missing1) {
        this.missing1 = missing1;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void onBefore(ListenerContext context) {
        Assert.assertNotNull(notMissing1);
        Assert.assertNotNull(notMissing2);
        Assert.assertNotNull(missing1);
        Assert.assertNotNull(missing2);

        Assert.assertNotNull(notMissing1.getValue());
        Assert.assertNotNull(notMissing2.getValue());
        Assert.assertNull(missing1.getValue());
        Assert.assertNull(missing2.getValue());
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        Assert.assertNotNull(notMissing1);
        Assert.assertNotNull(notMissing2);
        Assert.assertNotNull(missing1);
        Assert.assertNotNull(missing2);

        Assert.assertNotNull(notMissing1.getValue());
        Assert.assertNotNull(notMissing2.getValue());
        Assert.assertNull(missing1.getValue());
        Assert.assertNull(missing2.getValue());
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        Assert.assertNotNull(notMissing1);
        Assert.assertNotNull(notMissing2);
        Assert.assertNotNull(missing1);
        Assert.assertNotNull(missing2);

        Assert.assertNotNull(notMissing1.getValue());
        Assert.assertNotNull(notMissing2.getValue());
        Assert.assertNull(missing1.getValue());
        Assert.assertNull(missing2.getValue());
    }
}
