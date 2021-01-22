/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.util.HashMap;
import value.Value;

/**
 *
 * @author dell
 */
public class Table extends HashMap<String, Value>{

    private final Table enclosing;

    public Table(Table enclosing) {
        this.enclosing = enclosing;
    }

    public Table getEnclosing() {
        return enclosing;
    }
    
    
    
}
