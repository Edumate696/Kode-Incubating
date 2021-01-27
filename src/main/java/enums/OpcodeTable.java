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
public enum OpcodeTable {
    OP_CONST, OP_POP, // Maybe replaced with RETURN in future
    OP_ADD, OP_SUB, OP_MUL, OP_DIV, // Binary Operations
    OP_POS, OP_NEG, // Unary Operations
    OP_LOAD, OP_STORE, // Variable

    OP_GLOBAL, OP_NON_LOCAL, // Scoping
    OP_EXIT,

}
