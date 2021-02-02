/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type.core;

import internels.vm.type.KodeTypeFromJava;
import java.util.HashMap;

public abstract class KodeType extends KodeObject {

    private static final HashMap<Class, KodeTypeFromJava> REGISTRY = new HashMap<>();
    private static final KodeType BASE_TYPE = new KodeType(true) {
    }; // BaseType

    public static final KodeType fromClass(Class<? extends KodeObject> c) {
        if (KodeType.class.isAssignableFrom(c)) {
            return BASE_TYPE;
        }
        KodeTypeFromJava type = REGISTRY.get(c);
        if (type == null) {
            type = new KodeTypeFromJava(c);
            REGISTRY.put(c, type);
        }
        return type;
    }

    private final String name;

    protected KodeType(String name) {
        this.name = name;
    }

    private KodeType(boolean basetype) {
        super(true);
        this.name = "type";
    }

    @Override
    public String toString() {
        return "<class '%s' at %x>".formatted(this.name, this.hashCode());
    }

}
