/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import io.Printer;
import java.util.Iterator;
import value.Value;

/**
 *
 * @author dell
 */
public final class Chunk implements Iterable<Opcode> {

    public static Chunk buildChunk() {
        return new Chunk();
    }

    transient Node<Opcode> code_first;
    transient Node<Opcode> code_last;

    transient Node<Value> const_first;
    transient Node<Value> const_last;

    private Chunk() {
        this.code_last = null;
        this.code_first = null;
        this.const_first = null;
        this.const_last = null;
    }

    public void emitByte(Opcode op) {
        final Node<Opcode> newNode = new Node<>(op);
        if (this.code_last == null) {
            this.code_first = newNode;
        } else {
            this.code_last.next = newNode;
        }
        this.code_last = newNode;
    }

    public void emitConstant(Object operand) {
        final Node<Value> newNode = new Node<>(Value.generate(operand));
        if (this.const_last == null) {
            this.const_first = newNode;
        } else {
            this.const_last.next = newNode;
        }
        this.const_last = newNode;
    }

    @Override
    public Iterator<Opcode> iterator() {
        return new CustomIter<>(this.code_first);
    }

    private CustomIter<Value> _constIter() {
        return new CustomIter<>(this.const_first);
    }
    
    public Iterator<Value> const_iterator() {
        return this._constIter();
    }

    public void printDebug() {
        CustomIter<Value> temp_const_iter = this._constIter();
        for (Opcode opcode : this) {
            switch (opcode) {
                case OP_PUSH:
                    Printer.out.println(opcode + " " + temp_const_iter.debugNext("<empty>"));
                    break;
                default:
                    Printer.out.println(opcode);
            }
        }
    }

    private static class Node<E> {

        E item;
        Node<E> next = null;

        Node(E element) {
            this.item = element;
        }
    }

    private static class CustomIter<E> implements Iterator<E> {

        private Node<E> next_node;

        CustomIter(Node<E> first_node) {
            this.next_node = first_node;
        }

        @Override
        public boolean hasNext() {
            return this.next_node != null;
        }

        @Override
        public E next() {
            final E res = this.next_node.item;
            this.next_node = this.next_node.next;
            return res;
        }

        public String debugNext(String msg) {
            if (this.next_node != null) {
                final E res = this.next_node.item;
                this.next_node = this.next_node.next;
                return res.toString();
            }else{
                return msg;
            }
        }

    }

}
