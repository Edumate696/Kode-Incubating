/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.compiler;

import enums.OpcodeTable;

/**
 *
 * @author dell
 */
public class Byte {

    private final OpcodeTable opcode;
    private final Object operand;

    protected Byte(OpcodeTable opcode, Object operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    public OpcodeTable getOpcode() {
        return opcode;
    }

    public <T> T getOperand() {
        return (T) operand;
    }

    @Override
    public String toString() {
        if (this.operand == null) {
            return "" + this.opcode;
        }else{
            return "" + this.opcode + " " + this.operand;
        }
    }

}
