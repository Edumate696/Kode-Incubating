/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.PrintStream;
import org.fusesource.jansi.AnsiConsole;

/**
 *
 * @author dell
 */
public final class Printer {
    
    /** Don't let anyone instantiate this class */
    private Printer() {
    }

    public static PrintStream out = AnsiConsole.out;
    public static PrintStream err = AnsiConsole.err;
}
