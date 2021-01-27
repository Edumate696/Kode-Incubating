/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm;

import internels.compiler.Byte;
import internels.compiler.Compiler;
import internels.compiler.Chunk;
import internels.vm.scopes.Scope;
import io.Printer;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.LinkedList;
import internels.vm.type.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dell
 */
public class VM {

    public Type eval(String fn, String src) {
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

    private Type result = null;
    private final LinkedList<Type> arithmatic_stack = new LinkedList<>();
    private boolean debug;
    private final Scope global_table = Scope.buildDefaultScope();
    private final LinkedList<Scope> local_table = new LinkedList<>();
    private final Map<String, Boolean> custom_binding = new HashMap<>();

    private void evaluate(Chunk chunk) {
        for (Byte b : chunk) {
            switch (b.getOpcode()) {
                case OP_CONST:
                    push(Type.generate(b.getOperand()));
                    break;
                case OP_POP:
                    this.result = pop();
                    break;
                case OP_ADD:
                    binary_operation(Type::__add__);
                    break;
                case OP_SUB:
                    binary_operation(Type::__sub__);
                    break;
                case OP_MUL:
                    binary_operation(Type::__mul__);
                    break;
                case OP_DIV:
                    binary_operation(Type::__div__);
                    break;
                case OP_POS:
                    unary_operation(Type::__pos__);
                    break;
                case OP_NEG:
                    unary_operation(Type::__neg__);
                    break;
                case OP_LOAD:
                    push(retriveVariable(b.getOperand()));
                    break;
                case OP_STORE:
                    storeVariable(b.getOperand(), peek());
                    break;
                case OP_GLOBAL:
                    custom_binding.put(b.getOperand(), true);
                    break;
                case OP_NON_LOCAL:
                    custom_binding.put(b.getOperand(), false);
                    break;
                case OP_EXIT:
                    exitStmtCall(pop());
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

    private void binary_operation(BiFunction<Type, Type, Type> op) {
        Type right = pop();
        Type left = pop();
        push(op.apply(left, right));
    }

    private void unary_operation(Function<Type, Type> op) {
        push(op.apply(pop()));
    }

    private void push(Type constant) {
        arithmatic_stack.push(Objects.requireNonNull(constant, "Null value cannot be pushed."));
    }

    private Type pop() {
        return arithmatic_stack.pop();
    }

    private Type peek() {
        return arithmatic_stack.peek();
    }

    private Type retriveVariable(String name) {
        Type v;
        for (Scope s : local_table) {
            if ((v = s.retriveVariable(name)) != null) {
                return v;
            }
        }
        if ((v = global_table.retriveVariable(name)) != null) {
            return v;
        }
        throw new RuntimeException("Variable '" + name + "' not defind.");
    }

    private void storeVariable(String name, Type value) {
        if (local_table.isEmpty() || Objects.equals(custom_binding.get(name), Boolean.TRUE)) { // GLOBAL
            global_table.storeVariable(name, value);
        } else if (Objects.equals(custom_binding.get(name), Boolean.FALSE)) { // NON_LOCAL
            for (Scope s : local_table) {
                if (s.retriveVariable(name) != null) {
                    s.storeVariable(name, value);
                    return;
                }
            }
            if (global_table.retriveVariable(name) != null) {
                global_table.storeVariable(name, value);
            }
        } else { // LOCAL
            local_table.peek().storeVariable(name, value);
        }
    }

    public Scope getGlobalTable() {
        return global_table;
    }

    private void exitStmtCall(Type exitCode) {
        System.exit(Double.valueOf(exitCode.toString()).intValue());
    }

}
