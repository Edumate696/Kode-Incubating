/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.LinkedList;
import lexer.Token;

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

    void emitByte(Token tok, Opcode op, Object operand) {
        this.addLast(new Byte(op, operand));
    }

}
