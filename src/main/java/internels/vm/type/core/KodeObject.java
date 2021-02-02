/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type.core;

import internels.vm.type.Constant;
import java.util.HashMap;

/**
 *
 * @author dell
 */
public class KodeObject {
    
    private final KodeType objType;
    private final HashMap<String, KodeObject> fields = new HashMap<>();

    public KodeObject(){
        this.objType = KodeType.fromClass(getClass());
    }
    
    public KodeObject(KodeType objType){
        this.objType = objType;
    }
    
    protected KodeObject(boolean basetype){
        this.objType = (KodeType) this;
    }

    public final KodeType getType() {
        return objType;
    }
    
    public final KodeObject getAttribute(String name){
        return Constant.None;
    }

    @Override
    public String toString() {
        return this.getType().toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
