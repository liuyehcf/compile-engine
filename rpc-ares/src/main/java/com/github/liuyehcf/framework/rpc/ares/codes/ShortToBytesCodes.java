package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class ShortToBytesCodes extends ObjectToBytesCodes<Short> {

    @Override
    public boolean matchPlainType(Type type) {
        if (short.class.equals(type)) {
            return true;
        }
        return super.matchPlainType(type);
    }

    @Override
    public byte[] encode(Short obj) {
        return Short.toString(obj).getBytes();
    }

    @Override
    public Short decode(byte[] obj, Type expectedPlainType) {
        return Short.parseShort(new String(obj));
    }
}
