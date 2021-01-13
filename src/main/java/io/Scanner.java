/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

/**
 *
 * @author dell
 */
public final class Scanner {
    
    /** Don't let anyone instantiate this class */
    private Scanner() {
    }

    private final static boolean CONSOLE_LESS = System.console() == null;
    private final static Console SYS_CONSOLE = System.console();
    private final static BufferedReader ALT_CONSOLE = new BufferedReader(new InputStreamReader(System.in));

    public final static int read() {
        try {
            return CONSOLE_LESS ? ALT_CONSOLE.read() : SYS_CONSOLE.reader().read();
        } catch (Throwable ex) {
            return -1; //Error
        }
    }
    
    public final static String readLine() {
        try {
            return CONSOLE_LESS ? ALT_CONSOLE.readLine() : SYS_CONSOLE.readLine();
        } catch (Throwable ex) {
            return null; //Error
        }
    }
    
    public final static char[] readPassword() {
        try {
            return CONSOLE_LESS ? ALT_CONSOLE.readLine().toCharArray() : SYS_CONSOLE.readPassword();
        } catch (Throwable ex) {
            return null; //Error
        }
    }

}
