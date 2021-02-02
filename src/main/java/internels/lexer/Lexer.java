/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.lexer;

import enums.TokenType;
import dnl.utils.text.table.TextTable;
import io.Printer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import static enums.TokenType.*;

/**
 *
 * @author dell
 */
public class Lexer {

    public static List<Token> toTokens(String fn, String src) {
        return new Lexer(fn, src).tokens;
    }

    private static final Map<String, TokenType> KEYWORDS;

    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("global", TT_GLOBAL);
        KEYWORDS.put("nonlocal", TT_NON_LOCAL);
        KEYWORDS.put("print", TT_PRINT);
    }

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = start;
    private int line = 1;
    private final String fn;

    private Lexer(String fn, String source) {
        this.fn = fn;
        this.source = source;
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
//            scanToken();
            scanToken();
        }
        emitToken(TT_EOF);

        // DEBUG
        if (Boolean.getBoolean("kode.debug") || Boolean.getBoolean("kode.debug.lexer")) {
            Printer.out.println();
            Printer.out.println("             TOKENS");
            DefaultTableModel m = new DefaultTableModel();
            m.setColumnIdentifiers(new Object[]{"Line No.", "Token Type", "Lexeme", "Literal"});
            tokens.forEach(tok -> {
                m.addRow(new Object[]{tok.line, tok.type, tok.lexeme, tok.literal});
            });
            TextTable tt = new TextTable(m);
            tt.printTable(Printer.out, 0);
            Printer.out.println();
        }
    }

    private void emitToken(TokenType type) {
        emitToken(type, null);
    }

    private void emitToken(TokenType type, Object literal) {
        String line_text = source.split("\n", -1)[line - 1];
        tokens.add(new Token(type, type == TT_EOF ? "" : source.substring(start, current), literal, line, line_text, fn));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // Single Char Lexemes
            case '(':
                emitToken(TT_LEFT_PAREN);
                break;
            case ')':
                emitToken(TT_RIGHT_PAREN);
                break;
            case '-':
                emitToken(TT_MINUS);
                break;
            case '+':
                emitToken(TT_PLUS);
                break;
            case '*':
                emitToken(TT_STAR);
                break;
            case '/':
                emitToken(TT_SLASH);
                break;
            case ';':
                emitToken(TT_SEMICOLON);
                break;
            case ',':
                emitToken(TT_COMMA);
                break;
            case '=':
                emitToken(TT_EQUAL);
                break;
            // White Space
            case ' ':
            case '\r':
            case '\t':
                break;
            // New Line
            case '\n':
                line++;
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    // Error Handling
                    error(fn, line, "Unexpected character '" + c + "'.");
                }
                break;
        }
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) {
                advance();
            }
        }

        // Look for a exponential part.
        if ((peek() == 'e' || peek() == 'E') && (isDigit(peekNext()) || peekNext() == '+' || peekNext() == '-')) {
            // Consume the "e"
            advance();
            if (peek() == '+' || peek() == '-') {
                advance();
            }

            while (isDigit(peek())) {
                advance();
            }
        }
        emitToken(TT_NUMBER, Double.valueOf(source.substring(start, current)));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        // See if the identifier is a reserved word.
        String text = source.substring(start, current);

        TokenType type = KEYWORDS.get(text);
        if (type == null) {
            type = TT_IDENTIFIER;
        }
        emitToken(type);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    void error(String fn, int line, String message) {
        throw new RuntimeException(String.format("%s, line %d: %s", fn, line, message));
    }
}
