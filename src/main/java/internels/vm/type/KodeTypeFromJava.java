/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type;

import internels.vm.type.core.KodeType;
import internels.vm.type.core.KodeObject;


public class KodeTypeFromJava extends KodeType {

    public KodeTypeFromJava(Class<? extends KodeObject> c) {
        super(c.getName());
    }
    
}
