/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.scopes;

import java.util.HashMap;
import internels.vm.type.Type;

/**
 *
 * @author dell
 */
public interface Scope {

    public static Scope buildDefaultScope() {
        return new ScopeImpl();
    }

    @Deprecated
    public default boolean hasVariable(String name) {
        return retriveVariable(name) != null;
    }

    public Type retriveVariable(String name);

    public void storeVariable(String name, Type value);

}

class ScopeImpl extends HashMap<String, Type> implements Scope {

    @Override
    public Type retriveVariable(String name) {
        return this.get(name);
    }

    @Override
    public void storeVariable(String name, Type value) {
        this.put(name, value);
    }

}
