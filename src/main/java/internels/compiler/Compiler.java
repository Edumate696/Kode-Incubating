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
        if (match(TT_GLOBAL, TT_NON_LOCAL)) {
            scopeDeclarationStatement();
        } else if (match(TT_EXIT)) {
            exitStatement();
        } else {
            expressionStatement();
        }
    }

    private void exitStatement() {
        if (!match(TT_SEMICOLON)) {
            expression();
            consume(TT_SEMICOLON, "Expect ';' after exit statement.");
        } else {
            emitByte(OP_CONST, 0.0);
        }
        emitByte(OP_EXIT);
    }

    private void scopeDeclarationStatement() {
        OpcodeTable opcode = previous().type == TT_GLOBAL ? OP_GLOBAL : OP_NON_LOCAL;
        do {
            Token name = consume(TT_IDENTIFIER, "Expect variable name.");
            emitByte(opcode, name.lexeme);
        } while (match(TT_COMMA));
        consume(TT_SEMICOLON, "Expect ';' after variable name.");
    }

    private void expressionStatement() {
        if (!match(TT_SEMICOLON)) {
            expression();
            consume(TT_SEMICOLON, "Expect ';' after expression.");
            emitByte(OP_POP);
        }
    }

    private void expression() {
        assignment();
    }

    private void assignment() {
        addition(); // Expr

        if (match(TT_EQUAL)) {
//            Token equals = previous();
            Byte b = removeByte();
            assignment(); // Value

            if (b.getOpcode() == OP_LOAD) {
                emitByte(OP_STORE, b.getOperand());
            }
        }
    }

    private void addition() {
        multiplication();
        while (match(TT_MINUS, TT_PLUS)) {
            Token operator = previous();
            multiplication();
            switch (operator.type) {
                case TT_MINUS:
                    emitByte(OP_SUB);
                    break;
                case TT_PLUS:
                    emitByte(OP_ADD);
                    break;
            }
        }
    }

    private void multiplication() {
        unary();
        while (match(TT_SLASH, TT_STAR)) {
            Token operator = previous();
            unary();
            switch (operator.type) {
                case TT_SLASH:
                    emitByte(OP_DIV);
                    break;
                case TT_STAR:
                    emitByte(OP_MUL);
                    break;
            }
        }
    }

    private void unary() {
        if (match(TT_MINUS, TT_PLUS)) {
            Token operator = previous();
            unary();
            switch (operator.type) {
                case TT_MINUS:
                    emitByte(OP_NEG);
                    break;
                case TT_PLUS:
                    emitByte(OP_POS);
                    break;
            }
        } else {
            primary();
        }
    }

    private void primary() {
        if (match(TT_NUMBER)) {
            emitByte(OP_CONST, previous().literal);
        } else if (match(TT_LEFT_PAREN)) {
            expression();
            consume(TT_RIGHT_PAREN, "Expect ')' after expression.");
        } else if (match(TT_IDENTIFIER)) {
            emitByte(OP_LOAD, previous().lexeme);
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
        return peek().type == TT_EOF;
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

    private Byte removeByte() {
        return this.current_chunk.removeByte();
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException(String.format("%s, line %d: %s", token.fn, token.line, message));
    }
}
