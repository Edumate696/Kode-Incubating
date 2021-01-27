/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 *
 * @author dell
 */
public enum TokenType {
    TT_MINUS, TT_PLUS, TT_SLASH, TT_STAR, // Arithmatic Operators
    TT_EQUAL, // Assignment and Comparison
    TT_LEFT_PAREN, TT_RIGHT_PAREN, // Paren
    TT_NUMBER, TT_IDENTIFIER, //
    TT_COMMA, TT_SEMICOLON, TT_EOF, //Symbols
    // Keywords
    TT_GLOBAL, TT_NON_LOCAL, TT_EXIT,
}
