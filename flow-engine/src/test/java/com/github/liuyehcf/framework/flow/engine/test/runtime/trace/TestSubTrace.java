package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.listener.DefaultListener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
@SuppressWarnings("all")
public class TestSubTrace extends TestTraceBase {

    @Test
    public void testSubSingleWithSingleAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubSingleWithCascadeAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"){\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubSingleWithParallelAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        printAction(content=\"actionB\"),\n" +
                "        printAction(content=\"actionC\")\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubSingleWithIf() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i < 6; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[AB]");
                } else {
                    assertPrintCondition(trace, "condition[AB]", null);
                }
            }

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubSingleWithJoin() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        join {\n" +
                "            printAction(content=\"actionC\")&,\n" +
                "            printAction(content=\"actionD\")&\n" +
                "        } then {\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i < 9; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[ABCDE]");
                } else {
                    assertJoinGateway(trace);
                }
            }

            trace = executionLink.getTraces().get(9);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubSingleWithSelect() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubSingleWithSub() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        sub{\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertFlow(trace);

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubWithListeners() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")] ){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")],\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                }[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")] then {\n" +
                "                    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerG\"), printListener(event=\"success\", content=\"listenerH\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerI\"), printListener(event=\"success\", content=\"listenerJ\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerM\"), printListener(event=\"success\", content=\"listenerN\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerM", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerN", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerI", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(7);
            assertPrintCondition(trace, "conditionD", false);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerJ", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 19);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerI", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionLink.getTraces().get(12);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            trace = executionLink.getTraces().get(14);
            assertPrintListener(trace, "listenerG", ListenerEvent.before);

            trace = executionLink.getTraces().get(15);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(16);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);

            trace = executionLink.getTraces().get(17);
            assertPrintListener(trace, "listenerJ", ListenerEvent.success);

            trace = executionLink.getTraces().get(18);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInAction() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        sub{\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenTrue() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenFalse() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInIfThenElseTrue() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenElseFalse() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSubNestedInSelect() {
        Flow flow = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            sub {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInJoin() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

        });
    }

    @Test
    public void testSubNestedInJoinReverse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInJoinThen() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
                "    } then {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(7);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubNestedInJoinThenReverse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~&\n" +
                "    } then {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubWithTrueConditionOnly() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubWithFalseConditionOnly() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false))\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testParallelSub() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    },\n" +
                "    sub{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[AB]");

                trace = executionLink.getTraces().get(3);
                assertFlow(trace);
            }
        });
    }

    @Test
    public void testSubReachableWithNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(7);
            assertFlow(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSubUnReachableWithNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSubUnKnownException() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        throwExceptionAction()\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testSubLinkException() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        throwLinkTerminateAction()\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubLinkExceptionWithPropertyUpdate1() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        setPropertyAction(name=\"a\", value=1){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        },\n" +
                "        setPropertyAction(name=\"b\", value=2)\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            Assert.assertEquals(1, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);

            Assert.assertEquals(1, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
        });
    }

    @Test
    public void testSubLinkExceptionWithPropertyUpdate2() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        setPropertyAction(name=\"a\", value=1){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        },\n" +
                "        setPropertyAction(name=\"b\", value=2){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);

            Assert.assertEquals(2, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));
        });
    }
}
