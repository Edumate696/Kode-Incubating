/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internels.vm.type;

import internels.vm.type.core.KodeObject;


public class KodeNumber extends KodeObject {

    private final Double value;

    public KodeNumber(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public Double internDouble() {
        return value;
    }
}
