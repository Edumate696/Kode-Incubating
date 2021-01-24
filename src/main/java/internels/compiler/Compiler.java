/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.compiler;

import enums.OpcodeTable;
import static enums.OpcodeTable.*;
import io.Printer;
import java.util.List;
import internels.lexer.Lexer;
import internels.lexer.Token;
import enums.TokenType;
import static enums.TokenType.*;

/**
 *
 * @author dell
 */
public class Compiler {

    public static Chunk parse(String fn, String src) {
        return new Compiler(Lexer.toTokens(fn, src)).current_chunk;
    }

    private final List<Token> tokens;
    private int current = 0;
    private Chunk current_chunk = Chunk.buildChunk();

    private Compiler(List<Token> tokens) {
        this.tokens = tokens;
        while (!isAtEnd()) {
            statement();
        }

        // DEBUG
        if (Boolean.getBoolean("kode.debug") || Boolean.getBoolean("kode.debug.compiler")) {
            Printer.out.println();
            Printer.out.println("             BYTECODE");
            Printer.out.println("----------------------------------");
            for (Byte b : current_chunk) {
                Printer.out.println(b);
            }
            Printer.out.println();
        }
    }

    private void statement() {
        expressionStatement();
    }

    private void expressionStatement() {
        expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        emitByte(POP);
    }

    private void expression() {
        assignment();
    }

    private void assignment() {
        addition(); // Expr

        if (match(EQUAL)) {
//            Token equals = previous();
            Byte b = removeByte();
            assignment(); // Value
            
            if(b.getOpcode() == LOAD){
                emitByte(STORE, b.getOperand());
            }
        }
    }

    private void addition() {
        multiplication();
        while (match(MINUS, PLUS)) {
            Token operator = previous();
            multiplication();
            switch (operator.type) {
                case MINUS:
                    emitByte(SUB);
                    break;
                case PLUS:
                    emitByte(ADD);
                    break;
            }
        }
    }

    private void multiplication() {
        unary();
        while (match(SLASH, STAR)) {
            Token operator = previous();
            unary();
            switch (operator.type) {
                case SLASH:
                    emitByte(DIV);
                    break;
                case STAR:
                    emitByte(MUL);
                    break;
            }
        }
    }

    private void unary() {
        if (match(MINUS, PLUS)) {
            Token operator = previous();
            unary();
            switch (operator.type) {
                case MINUS:
                    emitByte(NEG);
                    break;
                case PLUS:
                    emitByte(POS);
                    break;
            }
        } else {
            primary();
        }
    }

    private void primary() {
        if (match(NUMBER)) {
            emitByte(CONST, previous().literal);
        } else if (match(LEFT_PAREN)) {
            expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
        } else if (match(IDENTIFIER)) {
            emitByte(LOAD, previous().lexeme);
        } else {
            throw error(peek(), "Expect expression.");
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void emitByte(OpcodeTable op, Object operand) {
        this.current_chunk.emitByte(previous(), op, operand);
    }

    private void emitByte(OpcodeTable op) {
        emitByte(op, null);
    }
    
    private Byte removeByte(){
        return this.current_chunk.removeByte();
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException(String.format("%s, line %d: %s", token.fn, token.line, message));
    }
}
