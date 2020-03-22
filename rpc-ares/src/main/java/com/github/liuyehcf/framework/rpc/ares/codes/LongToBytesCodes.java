package com.github.liuyehcf.framework.rpc.ares.codes;

import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;

import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public class LongToBytesCodes extends ObjectToBytesCodes<Long> {

    @Override
    public boolean matchDecodeType(Type expectedPlainType) {
        if (long.class.equals(expectedPlainType)) {
            return true;
        }
        return super.matchDecodeType(expectedPlainType);
    }

    @Override
    public byte[] encode(Long obj) {
        return Long.toString(obj).getBytes();
    }

    @Override
    public Long decode(byte[] obj, Type expectedPlainType) {
        return Long.parseLong(new String(obj));
    }
}
