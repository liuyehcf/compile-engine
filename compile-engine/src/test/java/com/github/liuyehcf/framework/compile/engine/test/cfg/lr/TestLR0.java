package com.github.liuyehcf.framework.compile.engine.test.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.LR0;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LRCompiler;
import com.github.liuyehcf.framework.compile.engine.test.GrammarCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestLR0 {
    @Test
    public void testLR0Status1() {
        LRCompiler compiler = new LR0(GrammarCase.LR0_1.GRAMMAR, GrammarCase.LR0_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"productions\":[\"B → · a B | a · B | a B · | · b | b ·\",\"S → · B B | B · B | B B ·\",\"__S__ → · S "
                        + "| S ·\"]}",
                compiler.getGrammar().toString()
        );

        assertEquals(
                "{\"closures:\":[{\"id\":\"0\",\"coreItems\":{\"1\":\"__S__ → · S\"},\"equalItems\":{\"1\":\"B → · a B\","
                        + "\"2\":\"B → · b\",\"3\":\"S → · B B\"}}, {\"id\":\"1\",\"coreItems\":{\"1\":\"B → a · B\"},"
                        + "\"equalItems\":{\"1\":\"B → · a B\",\"2\":\"B → · b\"}}, {\"id\":\"2\",\"coreItems\":{\"1\":\"B → "
                        + "b ·\"},\"equalItems\":{}}, {\"id\":\"3\",\"coreItems\":{\"1\":\"S → B · B\"},"
                        + "\"equalItems\":{\"1\":\"B → · a B\",\"2\":\"B → · b\"}}, {\"id\":\"4\","
                        + "\"coreItems\":{\"1\":\"__S__ → S ·\"},\"equalItems\":{}}, {\"id\":\"5\",\"coreItems\":{\"1\":\"B →"
                        + " a B ·\"},\"equalItems\":{}}, {\"id\":\"6\",\"coreItems\":{\"1\":\"S → B B ·\"},"
                        + "\"equalItems\":{}}]}",
                compiler.getClosureJSONString()
        );

        assertEquals(
                "| 状态\\文法符号 | __$__ | a | b | B | S |\n" +
                        "|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | \\ | MOVE_IN \"1\" | MOVE_IN \"2\" | JUMP \"3\" | JUMP \"4\" |\n" +
                        "| 1 | \\ | MOVE_IN \"1\" | MOVE_IN \"2\" | JUMP \"5\" | \\ |\n" +
                        "| 2 | REDUCTION \"B → b\" | REDUCTION \"B → b\" | REDUCTION \"B → b\" | \\ | \\ |\n" +
                        "| 3 | \\ | MOVE_IN \"1\" | MOVE_IN \"2\" | JUMP \"6\" | \\ |\n" +
                        "| 4 | ACCEPT \"__S__ → S\" | \\ | \\ | \\ | \\ |\n" +
                        "| 5 | REDUCTION \"B → a B\" | REDUCTION \"B → a B\" | REDUCTION \"B → a B\" | \\ | \\ |\n" +
                        "| 6 | REDUCTION \"S → B B\" | REDUCTION \"S → B B\" | REDUCTION \"S → B B\" | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testSLRStatus1() {
        LRCompiler compiler = new LR0(GrammarCase.SLR_1.GRAMMAR, GrammarCase.SLR_1.LEXICAL_ANALYZER);

        assertFalse(compiler.isLegal());

        assertEquals(
                "| 状态\\文法符号 | ( | ) | * | + | __$__ | id | E | F | T |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | JUMP \"1\" | JUMP \"5\" | JUMP \"2\" |\n" +
                        "| 1 | \\ | \\ | \\ | MOVE_IN \"6\" | ACCEPT \"__S__ → E\" | \\ | \\ | \\ | \\ |\n" +
                        "| 2 | REDUCTION \"E → T\" | REDUCTION \"E → T\" | REDUCTION \"E → T\" / MOVE_IN \"7\" | REDUCTION "
                        + "\"E → T\" | REDUCTION \"E → T\" | REDUCTION \"E → T\" | \\ | \\ | \\ |\n"
                        +
                        "| 3 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | JUMP \"8\" | JUMP \"5\" | JUMP \"2\" |\n" +
                        "| 4 | REDUCTION \"F → id\" | REDUCTION \"F → id\" | REDUCTION \"F → id\" | REDUCTION \"F → id\" | "
                        + "REDUCTION \"F → id\" | REDUCTION \"F → id\" | \\ | \\ | \\ |\n"
                        +
                        "| 5 | REDUCTION \"T → F\" | REDUCTION \"T → F\" | REDUCTION \"T → F\" | REDUCTION \"T → F\" | "
                        + "REDUCTION \"T → F\" | REDUCTION \"T → F\" | \\ | \\ | \\ |\n"
                        +
                        "| 6 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | \\ | JUMP \"5\" | JUMP \"9\" |\n" +
                        "| 7 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | \\ | JUMP \"10\" | \\ |\n" +
                        "| 8 | \\ | MOVE_IN \"11\" | \\ | MOVE_IN \"6\" | \\ | \\ | \\ | \\ | \\ |\n" +
                        "| 9 | REDUCTION \"E → E + T\" | REDUCTION \"E → E + T\" | REDUCTION \"E → E + T\" / MOVE_IN \"7\" | "
                        + "REDUCTION \"E → E + T\" | REDUCTION \"E → E + T\" | REDUCTION \"E → E + T\" | \\ | \\ | \\ |\n"
                        +
                        "| 10 | REDUCTION \"T → T * F\" | REDUCTION \"T → T * F\" | REDUCTION \"T → T * F\" | REDUCTION \"T →"
                        + " T * F\" | REDUCTION \"T → T * F\" | REDUCTION \"T → T * F\" | \\ | \\ | \\ |\n"
                        +
                        "| 11 | REDUCTION \"F → ( E )\" | REDUCTION \"F → ( E )\" | REDUCTION \"F → ( E )\" | REDUCTION \"F →"
                        + " ( E )\" | REDUCTION \"F → ( E )\" | REDUCTION \"F → ( E )\" | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    /*
     * 已经验证LL1_1、LL1_2不可以用LR0分析法，LL1_3尚未验证
     * 如果存在 `A → aβ | ε `这样的产生式，那么必定存在移入规约冲突
     */

    @Test
    public void testLR0Case1() {

        LRCompiler compiler = new LR0(GrammarCase.LR0_1.GRAMMAR, GrammarCase.LR0_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LR0_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LR0_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }
}
