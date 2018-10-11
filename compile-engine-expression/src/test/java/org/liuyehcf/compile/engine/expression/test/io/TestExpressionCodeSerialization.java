package org.liuyehcf.compile.engine.expression.test.io;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.Option;
import org.liuyehcf.compile.engine.expression.compile.ExpressionCompiler;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.core.io.ExpressionInputStream;
import org.liuyehcf.compile.engine.expression.core.io.ExpressionOutputStream;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class TestExpressionCodeSerialization extends TestBase {
    @Test
    public void caseCode() throws IOException {
        ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);

        ExpressionCode expressionCode = ExpressionEngine.compile("(-1+2*3/4%5-6&7|8^9<<10>>11>>>12) == 13 && 14 > 15 && 16 >=17&&18<19&&20<=21&&22==23&&24!=25&&date.timestamp() > 26 && collection.include([null,'s'],27.0) || !(a[28])");
        System.out.println(expressionCode.toReadableString());

        List<Class<?>> distinctClasses = expressionCode.getByteCodes().stream().map(Object::getClass).distinct().collect(Collectors.toList());
        assertEquals(30, distinctClasses.size());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExpressionOutputStream outputStream = new ExpressionOutputStream(byteArrayOutputStream);
        outputStream.writeExpressionCode(expressionCode);

        ExpressionInputStream inputStream = new ExpressionInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ExpressionCode loadedExpressionCode = inputStream.readExpressionCode();

        assertEquals(expressionCode.getByteCodes().size(), loadedExpressionCode.getByteCodes().size());
        for (int i = 0; i < expressionCode.getByteCodes().size(); i++) {
            assertEquals(expressionCode.getByteCodes().get(i).toString(), loadedExpressionCode.getByteCodes().get(i).toString());
        }

        ExpressionEngine.setOption(Option.OPTIMIZE_CODE, true);
    }

    @Test
    public void caseCompiler() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);

        outputStream.writeObject(ExpressionCompiler.getInstance());

        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ExpressionCompiler compiler = (ExpressionCompiler) inputStream.readObject();
    }
}
