import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

class FilePrinter extends Printer{
    @Override
    public void Print(LinkedList<String> lines, File file, boolean append) {
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file, append);
            String separator = System.getProperty("line.separator");
            if (append)
                for (String line : lines)
                    writer.append(line.concat(separator));
            else {
                for (String line : lines)
                    writer.write(line.concat(separator));
            }
            writer.close();
        } catch (Exception e) {
            //do nothing
        }
    }
}