import java.io.File;
import java.util.LinkedList;

public class ConsolePrinter extends Printer {
    @Override
    void Print(LinkedList<String> lines, File file, boolean append) {
        for (String line : lines){
            System.out.println(line);
        }
    }
}
