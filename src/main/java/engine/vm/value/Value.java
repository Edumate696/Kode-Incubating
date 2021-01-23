/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.vm.value;

/**
 *
 * @author dell
 */
public class Value {

    public static Value generate(Object num) {
        if (num instanceof Double) {
            return new Value((Double) num);
        }else{
            throw new RuntimeException("Unidentified Value");
        }
    }

    private final Double value;

    private Value(Double num) {
        this.value = num;
    }

    public Value __add__(Value right) {
        return Value.generate(this.value + right.value);
    }

    public Value __sub__(Value right) {
        return Value.generate(this.value - right.value);
    }

    public Value __mul__(Value right) {
        return Value.generate(this.value * right.value);
    }

    public Value __div__(Value right) {
        return Value.generate(this.value / right.value);
    }

    public Value __pos__() {
        return Value.generate(this.value);
    }

    public Value __neg__() {
        return Value.generate(-this.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
