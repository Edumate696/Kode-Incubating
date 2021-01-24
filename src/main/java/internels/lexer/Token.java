/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.lexer;

import enums.TokenType;

/**
 *
 * @author dell
 */
public class Token {

    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final String line_text;
    public final int line;
    public final String fn;

    protected Token(TokenType type, String lexeme, Object literal, int line, String line_text, String fn) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.line_text = line_text;
        this.fn = fn;
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
