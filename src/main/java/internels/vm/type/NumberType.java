/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type;

/**
 *
 * @author dell
 */
class NumberType implements Type {

    private final Double value;

    protected NumberType(Double value) {
        this.value = value;
    }

    @Override
    public Type __add__(Type right) {
        return Type.generate(this.value + ((NumberType) right).value);
    }

    @Override
    public Type __sub__(Type right) {
        return Type.generate(this.value - ((NumberType) right).value);
    }

    @Override
    public Type __mul__(Type right) {
        return Type.generate(this.value * ((NumberType) right).value);
    }

    @Override
    public Type __div__(Type right) {
        return Type.generate(this.value / ((NumberType) right).value);
    }

    @Override
    public Type __pos__() {
        return Type.generate(this.value);
    }

    @Override
    public Type __neg__() {
        return Type.generate(-this.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
