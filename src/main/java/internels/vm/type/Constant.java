/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type;

import internels.vm.type.core.KodeObject;

/**
 *
 * @author dell
 */
public final class Constant {

    public static final KodeObject None = new KodeObject() {

        @Override
        public String toString() {
            return "None";
        }
    };

    private Constant() {
    }

//    public static final KodeObject valueOf(String name) {
//        try {
//            Object obj = Constant.class.getField(name).get(null);
//            if (obj instanceof KodeObject) {
//                return (KodeObject) obj;
//            }
//        } catch (Exception ex) {
//        }
//        return null;
//    }
}
