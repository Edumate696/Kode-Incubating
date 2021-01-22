/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

/**
 *
 * @author dell
 */
public class Byte {

    private final Opcode opcode;
    private final Object operand;

    protected Byte(Opcode opcode, Object operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public Opcode getOpcode() {
        return opcode;
    }

    public <T> T getOperand() {
        return (T) operand;
    }
    
}
