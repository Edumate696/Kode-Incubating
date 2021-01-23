/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.vm;

import engine.compiler.Byte;
import engine.compiler.Compiler;
import engine.compiler.Chunk;
import io.Printer;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import engine.vm.value.Value;
import java.util.HashMap;
import java.util.Map;

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
    private final Stack<Value> arithmatic_stack = new Stack<>();
    private boolean debug;
    private Map<String, Value> table = new HashMap<>();

    private void evaluate(Chunk chunk) {
        for (Byte b : chunk) {
            switch (b.getOpcode()) {
                case CONST:
                    push(Value.generate(b.getOperand()));
                    break;
                case POP:
                    this.result = pop();
                    break;
                case ADD:
                    binary_operation(Value::__add__);
                    break;
                case SUB:
                    binary_operation(Value::__sub__);
                    break;
                case MUL:
                    binary_operation(Value::__mul__);
                    break;
                case DIV:
                    binary_operation(Value::__div__);
                    break;
                case POS:
                    unary_operation(Value::__pos__);
                    break;
                case NEG:
                    unary_operation(Value::__neg__);
                    break;
                case LOAD:
                    push(retriveVariable(b.getOperand()));
                    break;
                case STORE:
                    storeVariable(b.getOperand(), peek());
                    break;
                default:
                    throw new RuntimeException("Unknown Instruction : " + b);
            }
            if (this.debug) {
                Printer.out.format("%-20s ", b);
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
        arithmatic_stack.push(Objects.requireNonNull(constant, "Null value cannot be pushed."));
    }

    private Value pop() {
        return arithmatic_stack.pop();
    }
    
    private Value peek() {
        return arithmatic_stack.peek();
    }

    private Value retriveVariable(String name) {
        if (table.containsKey(name)) {
            return table.get(name);
        }
        throw new RuntimeException("Variable '" + name + "' not defind.");
    }
    
    private void storeVariable(String name, Value value) {
        table.put(name, value);
    }

}
