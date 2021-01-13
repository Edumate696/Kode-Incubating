package debug;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dell
 */
public class Debugger {

    public static void enableDebug() {
        System.setProperty("kode.debug", "true");
    }

    public static void disableDebug() {
        System.setProperty("kode.debug", "false");
    }

}
