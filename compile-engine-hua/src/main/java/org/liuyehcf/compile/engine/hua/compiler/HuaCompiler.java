package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.bytecode.*;
import org.liuyehcf.compile.engine.hua.production.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.*;

import java.util.ArrayList;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR {
    public HuaCompiler(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer);
    }

    @Override
    protected Engine createCompiler(String input) {
        return new HuaEngine(input);
    }

    private class HuaEngine extends Engine {

        /**
         * 地址偏移量，初始化为0
         */
        private int offset = 0;

        /**
         * 变量符号表
         */
        private VariableSymbolTable variableSymbolTable = new VariableSymbolTable();

        /**
         * 方法定义表
         */
        private MethodInfoTable methodInfoTable = new MethodInfoTable();

        HuaEngine(String input) {
            super(input);
        }

        @Override
        protected void before() {
            super.before();
        }

        @Override
        protected void after() {
            System.out.println(variableSymbolTable);
            System.out.println(methodInfoTable);
        }

        @Override
        protected void onReduction(PrimaryProduction ppReduction, FutureSyntaxNodeStack stack) {
            List<AbstractSemanticAction> semanticActions = ppReduction.getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (AbstractSemanticAction semanticAction : semanticActions) {
                if (semanticAction instanceof AddFutureSyntaxNode) {
                    processAddFutureSyntaxNode(stack, (AddFutureSyntaxNode) semanticAction);
                } else if (semanticAction instanceof AssignAttr) {
                    processAssignAttr(stack, (AssignAttr) semanticAction);
                } else if (semanticAction instanceof Assignment) {
                    processAssignment(stack);
                } else if (semanticAction instanceof BinaryOperator) {
                    processBinaryOperator((BinaryOperator) semanticAction);
                } else if (semanticAction instanceof CreateVariable) {
                    processCreateVariable(stack, (CreateVariable) semanticAction);
                } else if (semanticAction instanceof EnterMethod) {
                    processEnterMethod();
                } else if (semanticAction instanceof EnterNamespace) {
                    processEnterNamespace();
                } else if (semanticAction instanceof ExitMethod) {
                    processExitMethod();
                } else if (semanticAction instanceof ExitNamespace) {
                    processExitNamespace();
                } else if (semanticAction instanceof GetMethodNameFromIdentifier) {
                    processGetMethodNameFromIdentifier(stack, (GetMethodNameFromIdentifier) semanticAction);
                } else if (semanticAction instanceof GetVariableSymbolFromIdentifier) {
                    processGetVariableSymbolFromIdentifier(stack);
                } else if (semanticAction instanceof IncreaseArrayTypeDim) {
                    processIncreaseArrayTypeDim(stack, (IncreaseArrayTypeDim) semanticAction);
                } else if (semanticAction instanceof MethodInvocation) {
                    processMethodInvocation(stack);
                } else if (semanticAction instanceof PostDecrement) {
                    processPostDecrement(stack);
                } else if (semanticAction instanceof PostIncrement) {
                    processPostIncrement(stack);
                } else if (semanticAction instanceof SaveParamInfo) {
                    processSaveParamInfo(stack, (SaveParamInfo) semanticAction);
                } else if (semanticAction instanceof SetAssignOperator) {
                    processSetAssignOperator(stack, (SetAssignOperator) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromLexical) {
                    processSetSynAttrFromLexical(stack, (SetSynAttrFromLexical) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromSystem) {
                    processSetSynAttrFromSystem(stack, (SetSynAttrFromSystem) semanticAction);
                }
            }
        }

        private void processAddFutureSyntaxNode(FutureSyntaxNodeStack stack, AddFutureSyntaxNode semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            stack.addFutureSyntaxNode(stackOffset);
        }


        private void processAssignAttr(FutureSyntaxNodeStack stack, AssignAttr semanticAction) {
            int fromStackOffset = semanticAction.getFromStackOffset();
            int toStackOffset = semanticAction.getToStackOffset();
            String fromAttrName = semanticAction.getFromAttrName();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromStackOffset);
            SyntaxNode toNode = stack.get(toStackOffset);

            assertNotNull(fromNode.get(fromAttrName));
            toNode.put(toAttrName, fromNode.get(fromAttrName));
        }

        private void processAssignment(FutureSyntaxNodeStack stack) {
            SetAssignOperator.Operator operator = stack.get(Assignment.OPERATOR_STACK_OFFSET).get(AttrName.ASSIGN_OPERATOR.name());
            switch (operator) {
                case NORMAL_ASSIGN:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _istore());
                    break;
            }
        }

        private void processBinaryOperator(BinaryOperator semanticAction) {
            switch (semanticAction.getOperator()) {
                case LOGICAL_OR:
                    throw new UnsupportedOperationException();
                case LOGICAL_AND:
                    throw new UnsupportedOperationException();
                case BIT_OR:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _ior());
                    break;
                case BIT_XOR:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _ixor());
                    break;
                case BIT_AND:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _iand());
                    break;
                case EQUAL:
                    throw new UnsupportedOperationException();
                case NOT_EQUAL:
                    throw new UnsupportedOperationException();
                case LESS:
                    throw new UnsupportedOperationException();
                case LARGE:
                    throw new UnsupportedOperationException();
                case LESS_EQUAL:
                    throw new UnsupportedOperationException();
                case LARGE_EQUAL:
                    throw new UnsupportedOperationException();
                case SHIFT_LEFT:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _ishl());
                    break;
                case SHIFT_RIGHT:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _ishr());
                    break;
                case UNSIGNED_SHIFT_RIGHT:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _iushr());
                    break;
                case ADDITION:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _iadd());
                    break;
                case SUBTRACTION:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _isub());
                    break;
                case MULTIPLICATION:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _imul());
                    break;
                case DIVISION:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _idiv());
                    break;
                case REMAINDER:
                    methodInfoTable.getCurMethodInfo().addByteCode(new _irem());
                    break;
            }
        }

        private void processCreateVariable(FutureSyntaxNodeStack stack, CreateVariable semanticAction) {
            int stackOffset = semanticAction.getStackOffset();

            SyntaxNode node = stack.get(stackOffset);

            String name = node.getValue();
            String type = node.get(AttrName.TYPE.name());
            int width = node.get(AttrName.WIDTH.name());

            if (variableSymbolTable.enter(this.offset, name, type, width) == null) {
                throw new RuntimeException("标志符 " + name + " 已存在，请勿重复定义");
            }
            this.offset += width;
        }

        private void processEnterMethod() {
            methodInfoTable.enterMethod();
        }

        private void processEnterNamespace() {
            enterNamespace();
        }

        private void processExitMethod() {
            methodInfoTable.exitMethod();
        }

        private void processExitNamespace() {
            exitNamespace();
        }

        private void processGetMethodNameFromIdentifier(FutureSyntaxNodeStack stack, GetMethodNameFromIdentifier semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            String methodName = stack.get(stackOffset).getValue();
            methodInfoTable.setMethodNameOfCurrentMethod(methodName);
        }

        private void processGetVariableSymbolFromIdentifier(FutureSyntaxNodeStack stack) {
            String identifierName = stack.get(0).getValue();
            VariableSymbol variableSymbol = variableSymbolTable.getVariableSymbolByName(identifierName);
            if (variableSymbol == null) {
                throw new RuntimeException("标志符 " + identifierName + " 尚未定义");
            }

            switch (variableSymbol.getType()) {
                case "int":
                    methodInfoTable.getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                    break;
                case "boolean":
                    methodInfoTable.getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                    break;
            }
        }

        private void processIncreaseArrayTypeDim(FutureSyntaxNodeStack stack, IncreaseArrayTypeDim increaseArrayTypeDim) {
            int stackOffset = increaseArrayTypeDim.getStackOffset();
            String originType = stack.get(stackOffset).get(AttrName.TYPE.name());
            originType = originType + "[]";
            stack.get(stackOffset).put(AttrName.TYPE.name(), originType);
        }

        private void processMethodInvocation(FutureSyntaxNodeStack stack) {

        }

        @SuppressWarnings("unchecked")
        private void processPostDecrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.name()) == null) {
                stack.get(0).put(AttrName.CODES.name(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.name())).add(new _iinc(1));
        }

        @SuppressWarnings("unchecked")
        private void processPostIncrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.name()) == null) {
                stack.get(0).put(AttrName.CODES.name(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.name())).add(new _iinc(1));
        }

        private void processSaveParamInfo(FutureSyntaxNodeStack stack, SaveParamInfo semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            String type = stack.get(stackOffset).get(AttrName.TYPE.name());
            int width = stack.get(stackOffset).get(AttrName.WIDTH.name());
            methodInfoTable.addParamInfoToCurrentMethod(new ParamInfo(type, width));
        }

        private void processSetAssignOperator(FutureSyntaxNodeStack stack, SetAssignOperator semanticAction) {
            SetAssignOperator.Operator operator = semanticAction.getOperator();

            stack.get(0).put(AttrName.ASSIGN_OPERATOR.name(), operator);
        }

        private void processSetSynAttrFromLexical(FutureSyntaxNodeStack stack, SetSynAttrFromLexical semanticAction) {
            int fromStackOffset = semanticAction.getFromStackOffset();
            int toStackOffset = semanticAction.getToStackOffset();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromStackOffset);
            SyntaxNode toNode = stack.get(toStackOffset);

            assertNotNull(fromNode.getValue());
            toNode.put(toAttrName, fromNode.getValue());
        }

        private void processSetSynAttrFromSystem(FutureSyntaxNodeStack stack, SetSynAttrFromSystem semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            String attrName = semanticAction.getAttrName();
            Object attrValue = semanticAction.getAttrValue();

            stack.get(stackOffset).put(attrName, attrValue);
        }

        private void enterNamespace() {
            variableSymbolTable.enterNamespace();
        }

        private void exitNamespace() {
            variableSymbolTable.exitNamespace();
        }

    }
}
