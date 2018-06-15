package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * @author chenlu
 * @date 2018/6/3
 */
public class VariableSymbolTable {

    /**
     * 命名空间映射
     * id -> Namespace
     */
    private final Map<Integer, Namespace> namespaceMap;
    /**
     * 符号表
     * 标志符名字 -> 符号详细信息
     */
    private final Map<Namespace, Map<String, VariableSymbol>> table;
    /**
     * 符号表
     * 标志符偏移量 -> 符号详细信息
     */
    private final Map<Namespace, Map<Integer, VariableSymbol>> offsetMap;
    /**
     * 命名空间计数值
     */
    private int namespaceCnt;
    /**
     * 当前命名空间，初始化为最大的全局命名空间
     */
    private Namespace currentNamespace;

    public VariableSymbolTable() {
        namespaceCnt = 0;
        currentNamespace = new Namespace(namespaceCnt++, Namespace.NO_PARENT_NAMESPACE);
        namespaceMap = new LinkedHashMap<>(16);
        table = new LinkedHashMap<>(16);
        offsetMap = new LinkedHashMap<>(16);

        assertFalse(namespaceMap.containsKey(currentNamespace.getId()));
        namespaceMap.put(currentNamespace.getId(), currentNamespace);
        table.put(currentNamespace, new LinkedHashMap<>());
        offsetMap.put(currentNamespace, new LinkedHashMap<>());
    }

    public void enterNamespace() {
        currentNamespace = new Namespace(namespaceCnt++, currentNamespace.getId());

        assertFalse(table.containsKey(currentNamespace));
        assertFalse(offsetMap.containsKey(currentNamespace));
        table.put(currentNamespace, new LinkedHashMap<>(16));
        offsetMap.put(currentNamespace, new LinkedHashMap<>(16));

        assertFalse(namespaceMap.containsKey(currentNamespace.getId()));
        namespaceMap.put(currentNamespace.getId(), currentNamespace);
    }

    public void exitNamespace() {
        int pid = currentNamespace.getPid();

        assertTrue(pid != Namespace.NO_PARENT_NAMESPACE);
        assertTrue(namespaceMap.containsKey(pid));

        currentNamespace = namespaceMap.get(pid);
    }

    /**
     * 沿着命名空间的路径往上查找指定标志符
     *
     * @param name 标志符名字
     * @return 是否存在
     */
    private boolean exists(String name) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (table.get(namespace).containsKey(name)) {
                return true;
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return false;
    }

    public VariableSymbol enter(int offset, String name, Type type) {
        if (exists(name)) {
            return null;
        }

        VariableSymbol newVariableSymbol = new VariableSymbol(offset, currentNamespace, name, type);
        table.get(currentNamespace).put(name, newVariableSymbol);
        offsetMap.get(currentNamespace).put(offset, newVariableSymbol);

        return newVariableSymbol;
    }

    public VariableSymbol getVariableSymbolByName(String name) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (table.get(namespace).containsKey(name)) {
                return table.get(namespace).get(name);
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return null;
    }

    public VariableSymbol getVariableSymbolByOffset(int offset) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (offsetMap.get(namespace).containsKey(offset)) {
                return offsetMap.get(namespace).get(offset);
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return null;
    }

    public Map<String, Map<String, VariableSymbol>> getJSONTable() {
        Map<String, Map<String, VariableSymbol>> tableJSONMap = new LinkedHashMap<>(16);
        table.forEach((key, value) -> tableJSONMap.put("[" + key.getId() + ", " + key.getPid() + "]", value));
        return tableJSONMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
