import java.io.File;
import java.util.LinkedList;

public abstract class Printer {
    abstract void Print(LinkedList<String> lines, File file, boolean append);
}
