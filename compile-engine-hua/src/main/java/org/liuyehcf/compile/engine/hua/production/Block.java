package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.production.Expression.ASSIGNMENT;
import static org.liuyehcf.compile.engine.hua.production.Expression.EXPRESSION;
import static org.liuyehcf.compile.engine.hua.production.Program.VARIABLE_DECLARATORS;
import static org.liuyehcf.compile.engine.hua.production.Type.TYPE;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Block {
    public static final String BLOCK = "<block>"; // 139
    public static final String BLOCK_STATEMENTS = "<block statements>"; // 140
    public static final String BLOCK_STATEMENT = "<block statement>"; // 142
    public static final String LOCAL_VARIABLE_DECLARATION_STATEMENT = "<local variable declaration statement>"; // 144
    public static final String LOCAL_VARIABLE_DECLARATION = "<local variable declaration>"; // 146
    public static final String STATEMENT = "<statement>"; // 148
    public static final String STATEMENT_NO_SHORT_IF = "<statement no short if>"; // 150
    public static final String STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT = "<statement without trailing substatement>"; // 152
    public static final String EMPTY_STATEMENT = "<empty statement>"; // 154
    public static final String EXPRESSION_STATEMENT = "<expression statement>"; // 160
    public static final String STATEMENT_EXPRESSION = "<statement expression>"; // 162
    public static final String IF_THEN_STATEMENT = "<if then statement>"; // 164
    public static final String IF_THEN_ELSE_STATEMENT = "<if then else statement>"; // 166
    public static final String IF_THEN_ELSE_STATEMENT_NO_SHORT_IF = "<if then else statement no short if>"; // 168
    public static final String WHILE_STATEMENT = "<while statement>"; // 182
    public static final String FOR_STATEMENT = "<for statement>"; // 188
    public static final String EPSILON_OR_FOR_INIT = "<epsilon or for init>"; // new
    public static final String FOR_INIT = "<for init>"; // 192
    public static final String EPSILON_OR_EXPRESSION = "<epsilon or expression>"; // new
    public static final String EPSILON_OR_FOR_UPDATE = "<epsilon or for update>"; // new
    public static final String FOR_UPDATE = "<for update>"; // 194
    public static final String STATEMENT_EXPRESSION_LIST = "<statement expression list>"; // 196

    public static final String NORMAL_IF = "if";
    public static final String NORMAL_ELSE = "else";
    public static final String NORMAL_WHILE = "while";
    public static final String NORMAL_FOR = "for";

    public static final Production[] PRODUCTIONS = {

            /*
             * <block> 139
             * SAME
             */
            Production.create(
                    /*
                     * <block> → { }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    ),
                    /*
                     * <block> → { <block statements> }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),


            /*
             * <block statements> 140
             * SAME
             */
            Production.create(
                    /*
                     * <block statements> → <block statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <block statements> → <block statements> <block statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <block statement> 142
             * SAME
             */
            Production.create(
                    /*
                     * <block statement> → <local variable declaration statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <block statement> → <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <local variable declaration statement> 144
             * SAME
             */
            Production.create(
                    /*
                     * <local variable declaration statement> → <local variable declaration> ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),


            /*
             * <local variable declaration> 146
             * SAME
             */
            Production.create(
                    /*
                     * <local variable declaration> → <type> <variable declarators>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS)
                            )
                            , null
                    )
            ),


            /*
             * <statement> 148
             */
            Production.create(
                    /*
                     * <statement> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <if then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <while statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(WHILE_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <for statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_STATEMENT)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <statement no short if> 150
             */
            Production.create(
                    /*
                     * <statement no short if> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement no short if> → <if then else statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <statement without trailing substatement> 152
             */
            Production.create(
                    /*
                     * <statement without trailing substatement> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            )
                            , null
                    ),
                    /*
                     * <statement without trailing substatement> → <empty statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EMPTY_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement without trailing substatement> → <expression statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_STATEMENT)
                            )
                            , null
                    )
//                    /*
//                     * <statement without trailing substatement> → <return statement>
//                     */
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(RETURN_STATEMENT)
//                            )
//                            , null
//                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <empty statement> 154
             * SAME
             */
            Production.create(
                    /*
                     * <empty statement> → ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EMPTY_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),


            /*
             * <expression statement> 160
             * SAME
             */
            Production.create(
                    /*
                     * <expression statement> → <statement expression> ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),


            /*
             * <statement expression> 162
             */
            Production.create(
                    /*
                     * <statement expression> → <assignment>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <if then statement> 164
             * SAME
             */
            Production.create(
                    /*
                     * <if then statement> → if ( <expression> ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <if then else statement> 166
             * SAME
             */
            Production.create(
                    /*
                     * <if then else statement> → if ( <expression> ) <statement no short if> else <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <if then else statement no short if> 168
             * SAME
             */
            Production.create(
                    /*
                     * <if then else statement no short if> → if ( <expression> ) <statement no short if> else <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            )
                            , null
                    )
            ),


            /*
             * <while statement> 182
             * SAME
             */
            Production.create(
                    /*
                     * <while statement> → while ( <expression> ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(WHILE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <for statement> 188
             * SAME
             */
            Production.create(
                    /*
                     * <for statement> → for ( <for init>? ; <expression>? ; <for update>? ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FOR),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <epsilon or for init>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or for init> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or for init> → <for init>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_INIT)
                            )
                            , null
                    )
            ),


            /*
             * <epsilon or expression>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or expression> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or expression> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <epsilon or for update>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or for update> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or for update> → <for update>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_UPDATE)
                            )
                            , null
                    )
            ),


            /*
             * <for init> 192
             * SAME
             */
            Production.create(
                    /*
                     * <for init> → <statement expression list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST)
                            )
                            , null
                    ),
                    /*
                     * <for init> → <local variable declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION)
                            )
                            , null
                    )
            ),


            /*
             * <for update> 194
             * SAME
             */
            Production.create(
                    /*
                     * <for update> → <statement expression list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST)
                            )
                            , null
                    )
            ),


            /*
             * <statement expression list> 196
             * SAME
             */
            Production.create(
                    /*
                     * <statement expression list> → <statement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <statement expression list> → <statement expression list> , <statement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION)
                            )
                            , null
                    )
            ),
    };
}
