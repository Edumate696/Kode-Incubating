/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.compiler;

import enums.OpcodeTable;
import java.util.LinkedList;
import internels.lexer.Token;

/**
 *
 * @author dell
 */
public final class Chunk extends LinkedList<Byte> {

    public static Chunk buildChunk() {
        return new Chunk();
    }

    private Chunk() {
    }

    protected void emitByte(Token tok, OpcodeTable op, Object operand) {
        this.addLast(new Byte(op, operand));
    }

    protected Byte removeByte() {
        return this.removeLast();
    }

}
