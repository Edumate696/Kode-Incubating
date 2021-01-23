
import io.Printer;
import io.Scanner;
import engine.vm.VM;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dell
 */
public class Main {

    public static void main(String[] args) {
        Printer.out.println("Kode Incubating");
        Printer.out.println("Running on " + System.getProperty("os.name", "Unknown Operating System")
                + " (" + System.getProperty("user.name", "default") + ")");
        System.setProperty("kode.debug", "true");
        VM vm = new VM();
        for (;;) {
            try {
                Printer.out.print(">> ");
                String line = Scanner.readLine();
                if (line.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }
                Printer.out.println(vm.eval("<shell>", line));
            } catch (Exception e) {
                Printer.err.println(e.getMessage());
            }
        }
    }
}
