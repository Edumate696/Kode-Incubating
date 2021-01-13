/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 * @author dell
 */
public enum TokenType{
    MINUS, PLUS, SLASH, STAR, LEFT_PAREN, RIGHT_PAREN,
    
    NUMBER, IDENTIFIER, EOF, SEMICOLON,
}
