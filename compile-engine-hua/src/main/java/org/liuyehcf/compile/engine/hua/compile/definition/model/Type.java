package org.liuyehcf.compile.engine.hua.compile.definition.model;

import java.io.Serializable;
import java.util.Objects;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 变量类型信息
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class Type implements Serializable {

    private final static int NORMAL_TYPE_DIM = 0;
    public final static Type TYPE_INT = createNormalType(NORMAL_INT, 4);
    public final static Type TYPE_BOOLEAN = createNormalType(NORMAL_BOOLEAN, 1);
    public final static Type TYPE_VOID = createNormalType(NORMAL_VOID, 0);
    public final static int ARRAY_TYPE_WIDTH = 8;
    
    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型长度
     */
    private final int typeWidth;

    /**
     * 维度，非数组类型该字段值为0
     */
    private final int dim;

    public Type(String typeName, int typeWidth, int dim) {
        this.typeName = typeName;
        this.typeWidth = typeWidth;
        this.dim = dim;
    }

    public static Type createNormalType(String typeName, int typeWidth) {
        return new Type(typeName, typeWidth, NORMAL_TYPE_DIM);
    }

    public static Type createArrayType(String typeName, int dim) {
        return new Type(typeName, ARRAY_TYPE_WIDTH, dim);
    }

    public static Type createType(String typeName, int typeWidth, int dim) {
        return new Type(typeName, typeWidth, dim);
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeWidth() {
        return typeWidth;
    }

    public int getDim() {
        return dim;
    }

    public boolean isArrayType() {
        return dim != NORMAL_TYPE_DIM;
    }

    public Type toDimDecreasedType() {
        if (!isArrayType()) {
            throw new RuntimeException("非数组类型无法获取降维类型");
        }

        if (this.dim == 1) {
            switch (this.typeName) {
                case NORMAL_INT:
                    return TYPE_INT;
                case NORMAL_BOOLEAN:
                    return TYPE_BOOLEAN;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return new Type(this.typeName, this.typeWidth, this.dim - 1);
    }

    public Type toDimIncreasedType() {
        if (this.dim == 0) {
            return createArrayType(this.typeName, this.dim + 1);
        }
        return new Type(this.typeName, this.typeWidth, this.dim + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeWidth == type.typeWidth &&
                dim == type.dim &&
                Objects.equals(typeName, type.typeName);
    }

    public String toTypeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName);
        for (int i = 0; i < dim; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeName='" + typeName + '\'' +
                ", typeWidth=" + typeWidth +
                ", dim=" + dim +
                '}';
    }
}