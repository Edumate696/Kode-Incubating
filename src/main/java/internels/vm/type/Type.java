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
public interface Type {

    public static Type generate(Object obj) {
        if (obj instanceof Double) {
            return new NumberType((Double) obj);
        } else if (obj instanceof Type) {
            return (Type) obj;
        } else {
            throw new RuntimeException("Unidentified Value");
        }
    }

    public Type __add__(Type right);

    public Type __sub__(Type right);

    public Type __mul__(Type right);

    public Type __div__(Type right);

    public Type __pos__();

    public Type __neg__();

}
