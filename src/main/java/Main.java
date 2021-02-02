
import io.Printer;
import io.Scanner;
import internels.vm.VM;
import internels.vm.type.core.KodeObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dell
 */
public final class Main {

    public static void main(String[] args) {
        Main.REPL();
    }

    private Main() {

    }

    private static void REPL() {
        Printer.out.println("Kode Incubating " + Main.getVersion() + " ["+System.getProperty("kode.home", "Kode Home Not Set")+"]");
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
                KodeObject result = vm.eval("<shell>", line);
                if (result != null) {
                    Printer.out.println(result);
                }
            } catch (Exception error) {
                String clsName = error.getClass().getSimpleName();
                String msg = error.getLocalizedMessage();
                Printer.err.println((msg != null) ? (clsName + ": " + msg) : clsName);
            }
        }
    }

    public static boolean checkUpdate() {
        return false;
    }

    public static String getVersion() {
        try {
            return com.install4j.api.launcher.Variables.getCompilerVariable("sys.version");
        } catch (Throwable ex) {
            return "dev-release";
        }
    }
}
