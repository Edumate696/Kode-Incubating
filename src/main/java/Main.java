
import io.Printer;
import io.Scanner;
import internels.vm.VM;

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

    private static void REPL() {
        Printer.out.println("Kode Incubating");
        Printer.out.println("Running on " + System.getProperty("os.name", "Unknown Operating System")
                + " (" + System.getProperty("user.name", "default") + ")");
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
                Printer.err.println("Error : " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Main.REPL();
    }
}
