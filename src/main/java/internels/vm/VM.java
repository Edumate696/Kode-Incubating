/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm;

import internels.vm.type.KodeNumber;
import internels.compiler.Byte;
import internels.compiler.Compiler;
import internels.compiler.Chunk;
import internels.vm.scopes.Scope;
import io.Printer;
import java.util.Objects;
import java.util.LinkedList;
import internels.vm.type.core.KodeObject;
import internels.vm.type.core.KodeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dell
 */
public class VM {

    public KodeObject eval(String fn, String src) {
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

    private KodeObject result = null;
    private final LinkedList<KodeObject> arithmatic_stack = new LinkedList<>();
    private final LinkedList<KodeObject> argument_queue = new LinkedList<>();
    private boolean debug;
    private final Scope global_table = Scope.buildDefaultScope();
    private final LinkedList<Scope> local_table = new LinkedList<>();
    private final Map<String, Boolean> custom_binding = new HashMap<>();

    private void evaluate(Chunk chunk) {
        for (Byte b : chunk) {
            switch (b.getOpcode()) {
                case OP_CONST:
                    pushConstant(b.getOperand());
                    break;
                case OP_POP:
                    this.result = pop();
                    break;
                case OP_ADD:
                    binary_operation("__add__", "__radd__");
                    break;
                case OP_SUB:
                    binary_operation("__sub__", "__rsub__");
                    break;
                case OP_MUL:
                    binary_operation("__mul__", "__rmul__");
                    break;
                case OP_DIV:
                    binary_operation("__div__", "__rdiv__");
                    break;
                case OP_POS:
                    unary_operation("__pos__");
                    break;
                case OP_NEG:
                    unary_operation("__neg__");
                    break;
                case OP_LOAD:
                    retriveVariable(b.getOperand());
                    break;
                case OP_STORE:
                    storeVariable(b.getOperand());
                    break;
                case OP_GLOBAL:
                    custom_binding.put(b.getOperand(), true);
                    break;
                case OP_NON_LOCAL:
                    custom_binding.put(b.getOperand(), false);
                    break;
                case OP_PRINT:
                    printStmtCall();
                    break;
                case OP_ARG_RESET:
                    argument_queue.clear();
                    break;
                case OP_ARG_PUSH:
                    argument_queue.add(pop());
                    break;
                case OP_CALL:
                    call_operation();
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

    private void push(KodeObject constant) {
        arithmatic_stack.push(Objects.requireNonNull(constant, "Null value cannot be pushed."));
    }

    private KodeObject pop() {
        return arithmatic_stack.pop();
    }

    private KodeObject peek() {
        return arithmatic_stack.peek();
    }

    private void binary_operation(String op, String rop) {
        KodeObject right = pop();
        KodeObject left = pop();
        push(left);
    }

    private void unary_operation(String op) {
        push(pop());
    }

    private void call_operation() {
        KodeObject callee = pop();
        List<KodeObject> args = new LinkedList<>(argument_queue); // Clone argument_queue
        KodeObject retVal = callee.getType();
        push(retVal);
    }

    private void retriveVariable(String name) {
        KodeObject v;
        for (Scope s : local_table) {
            if ((v = s.retriveVariable(name)) != null) {
                push(v);
                return;
            }
        }
        if ((v = global_table.retriveVariable(name)) != null) {
            push(v);
            return;
        }
        throw new RuntimeException("Variable '" + name + "' not defind.");
    }

    private void storeVariable(String name) {
        KodeObject value = peek();
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

    public void biginScope(Scope newScope) {
        this.local_table.push(newScope);
    }

    public void biginScope() {
        biginScope(Scope.buildDefaultScope());
    }

    public void endScope() {
        this.local_table.pop();
    }

    private void printStmtCall() {
        Printer.out.println(pop());
    }

    private void pushConstant(Object operand) {
        KodeObject constant;
        if (operand instanceof Double) {
            constant = new KodeNumber((Double) operand);
        } else {
            throw new RuntimeException("Unsupported Value.");
        }
        push(constant);
    }

}
