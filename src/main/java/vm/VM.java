/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vm;

import compiler.Compiler;
import compiler.Chunk;
import compiler.Opcode;
import io.Printer;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import value.Value;

/**
 *
 * @author dell
 */
public class VM {

    public Object eval(String fn, String src) {
        Chunk chunk = Compiler.parse(fn, src);
        debug = Boolean.getBoolean("kode.debug") || Boolean.getBoolean("kode.debug.vm");
        result = null;
        if (debug) {
            Printer.out.println();
            Printer.out.println("             RESULT");
            Printer.out.println("----------------------------------");
            try {
                evaluate(chunk);
            } catch (Throwable e) {
                Printer.out.println();
                Printer.out.println();
                throw e;
            }
            Printer.out.println();
            Printer.out.println();
        } else {
            evaluate(chunk);
        }
        return result;
    }

    private Object result = null;
    Stack<Value> arithmatic_stack = new Stack<>();
    private boolean debug;

    private void evaluate(Chunk chunk) {
        Iterator<Value> const_iterator = chunk.const_iterator();
        for (Opcode op : chunk) {
            Value operand = null;
            switch (op) {
                case OP_PUSH:
                    operand = const_iterator.next();
            }
            switch (op) {
                case OP_PUSH:
                    push(operand);
                    break;
                case OP_POP:
                    this.result = pop();
                    break;
                case OP_ADD:
                    binary_operation(Value::__add__);
                    break;
                case OP_SUB:
                    binary_operation(Value::__sub__);
                    break;
                case OP_MUL:
                    binary_operation(Value::__mul__);
                    break;
                case OP_DIV:
                    binary_operation(Value::__div__);
                    break;
                case OP_POS:
                    unary_operation(Value::__pos__);
                    break;
                case OP_NEG:
                    unary_operation(Value::__neg__);
                    break;
                default:
                    throw new RuntimeException("Unknown Opcode : " + op);
            }
            if (this.debug) {
                Printer.out.format("%-20s ", op);
                Printer.out.println(this.arithmatic_stack);
            }
        }
    }

    private void binary_operation(BiFunction<Value, Value, Value> op) {
        Value right = pop();
        Value left = pop();
        push(op.apply(left, right));
    }

    private void unary_operation(Function<Value, Value> op) {
        push(op.apply(pop()));
    }

    private void push(Value constant) {
        arithmatic_stack.push(Objects.<Value>requireNonNull(constant, "Null value cannot be pushed."));
    }

    private Value pop() {
        return arithmatic_stack.pop();
    }

}
