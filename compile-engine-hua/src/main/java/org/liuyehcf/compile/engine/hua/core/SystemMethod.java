package org.liuyehcf.compile.engine.hua.core;

import org.liuyehcf.compile.engine.core.utils.ListUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.runtime.HeapMemoryManagement;
import org.liuyehcf.compile.engine.hua.runtime.Reference;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * @author hechenfeng
 * @date 2018/6/27
 */
public class SystemMethod {

    static final Map<MethodSignature, Pair<MethodInfo, ProxyInvoke>> SYSTEM_METHOD_POOL;

    private static final MethodSignature PRINT_INT = new MethodSignature("print", new String[]{NORMAL_INT});
    private static final MethodSignature PRINT_CHAR = new MethodSignature("print", new String[]{NORMAL_CHAR});
    private static final MethodSignature PRINT_BOOLEAN = new MethodSignature("print", new String[]{NORMAL_BOOLEAN});

    private static final MethodSignature PRINTLN_INT = new MethodSignature("println", new String[]{NORMAL_INT});
    private static final MethodSignature PRINTLN_CHAR = new MethodSignature("println", new String[]{NORMAL_CHAR});
    private static final MethodSignature PRINTLN_BOOLEAN = new MethodSignature("println", new String[]{NORMAL_BOOLEAN});

    private static final MethodSignature PRINTLN_INT_ARRAY = new MethodSignature("println", new String[]{Type.TYPE_INT_ARRAY.toTypeDescription()});
    private static final MethodSignature PRINTLN_CHAR_ARRAY = new MethodSignature("println", new String[]{Type.TYPE_CHAR_ARRAY.toTypeDescription()});
    private static final MethodSignature PRINTLN_BOOLEAN_ARRAY = new MethodSignature("println", new String[]{Type.TYPE_BOOLEAN_ARRAY.toTypeDescription()});

    private static final MethodSignature RANDOM_NEXT = new MethodSignature("nextInt", new String[]{NORMAL_INT, NORMAL_INT});
    private static final MethodSignature RANDOM_NEXT_INT = new MethodSignature("nextInt", new String[]{});
    private static final MethodSignature RANDOM_NEXT_BOOLEAN = new MethodSignature("nextBoolean", new String[]{});

    private static final Random random = new Random();

    static {
        SYSTEM_METHOD_POOL = new LinkedHashMap<>();

        /*
         * print
         */
        SYSTEM_METHOD_POOL.put(PRINT_INT,
                new Pair<>(
                        createFakeMethodInfo(PRINT_INT, Type.TYPE_VOID),
                        (args) -> {
                            int intArg = (int) args[0];
                            System.out.print(intArg);
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINT_CHAR,
                new Pair<>(
                        createFakeMethodInfo(PRINT_CHAR, Type.TYPE_VOID),
                        (args) -> {
                            char charArg = (char) ((int) args[0]);
                            System.out.print(charArg);
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINT_BOOLEAN,
                new Pair<>(
                        createFakeMethodInfo(PRINT_BOOLEAN, Type.TYPE_VOID),
                        (args) -> {
                            boolean booleanArg = (int) args[0] == 1;
                            System.out.print(booleanArg);
                            return null;
                        }
                ));


        /*
         * println
         */
        SYSTEM_METHOD_POOL.put(PRINTLN_INT,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_INT, Type.TYPE_VOID),
                        (args) -> {
                            int intArg = (int) args[0];
                            System.out.println(intArg);
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINTLN_CHAR,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_CHAR, Type.TYPE_VOID),
                        (args) -> {
                            char charArg = (char) ((int) args[0]);
                            System.out.println(charArg);
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINTLN_BOOLEAN,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_BOOLEAN, Type.TYPE_VOID),
                        (args) -> {
                            boolean booleanArg = (int) args[0] == 1;
                            System.out.println(booleanArg);
                            return null;
                        }
                ));


        /*
         * println array
         */
        SYSTEM_METHOD_POOL.put(PRINTLN_INT_ARRAY,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_INT_ARRAY, Type.TYPE_VOID),
                        (args) -> {
                            Reference reference = (Reference) args[0];
                            int[] intArray = new int[reference.getSize()];
                            for (int i = 0; i < reference.getSize(); i++) {
                                intArray[i] = HeapMemoryManagement.loadInt(reference.getAddress() + Type.INT_TYPE_WIDTH * i);
                            }
                            System.out.println(Arrays.toString(intArray));
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINTLN_CHAR_ARRAY,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_CHAR_ARRAY, Type.TYPE_VOID),
                        (args) -> {
                            Reference reference = (Reference) args[0];
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < reference.getSize(); i++) {
                                sb.append((char) HeapMemoryManagement.loadChar(reference.getAddress() + Type.CHAR_TYPE_WIDTH * i));
                            }
                            System.out.println(sb.toString());
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINTLN_BOOLEAN_ARRAY,
                new Pair<>(
                        createFakeMethodInfo(PRINTLN_BOOLEAN_ARRAY, Type.TYPE_VOID),
                        (args) -> {
                            Reference reference = (Reference) args[0];
                            boolean[] booleanArray = new boolean[reference.getSize()];
                            for (int i = 0; i < reference.getSize(); i++) {
                                booleanArray[i] = HeapMemoryManagement.loadBoolean(reference.getAddress() + Type.BOOLEAN_TYPE_WIDTH * i) == 1;
                            }
                            System.out.println(Arrays.toString(booleanArray));
                            return null;
                        }
                ));


        SYSTEM_METHOD_POOL.put(RANDOM_NEXT,
                new Pair<>(
                        createFakeMethodInfo(RANDOM_NEXT, Type.TYPE_INT),
                        (args) -> {
                            int start = (int) args[0];
                            int end = (int) args[1];
                            return random.nextInt(end - start) + start;
                        }
                ));
        SYSTEM_METHOD_POOL.put(RANDOM_NEXT_INT,
                new Pair<>(
                        createFakeMethodInfo(RANDOM_NEXT_INT, Type.TYPE_INT),
                        (args) -> random.nextInt()
                ));
        SYSTEM_METHOD_POOL.put(RANDOM_NEXT_BOOLEAN,
                new Pair<>(
                        createFakeMethodInfo(RANDOM_NEXT_BOOLEAN, Type.TYPE_BOOLEAN),
                        (args) -> random.nextInt(2)
                ));
    }

    private static MethodInfo createFakeMethodInfo(MethodSignature methodSignature, Type resultType) {
        String[] typeDescriptions = methodSignature.getTypeDescriptions();

        Type[] paramTypes = new Type[typeDescriptions.length];

        for (int i = 0; i < typeDescriptions.length; i++) {
            paramTypes[i] = Type.parse(typeDescriptions[i]);
        }

        MethodInfo methodInfo = new MethodInfo();

        methodInfo.setMethodName(methodSignature.getMethodName());
        methodInfo.setParamTypeList(ListUtils.of(paramTypes));
        methodInfo.setResultType(resultType);

        return methodInfo;
    }

    public static Object invoke(MethodSignature methodSignature, Object[] args) {
        assertTrue(SYSTEM_METHOD_POOL.containsKey(methodSignature));
        return SYSTEM_METHOD_POOL.get(methodSignature).getSecond().invoke(args);
    }

    private interface ProxyInvoke {
        Object invoke(Object[] args);
    }
}
