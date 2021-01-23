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
    CONST, POP, // Maybe replaced with RETURN in future
    ADD, SUB, MUL, DIV, // Binary Operations
    POS, NEG, // Unary Operations
    LOAD, STORE, // Variable
}
