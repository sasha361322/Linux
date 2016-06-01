import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Linux {
    /**
     * Конструктор по умолчанию.
     * Инициализирует переменную path диском на которой находится программи
     */
    Linux(){
        path = new File("/Users/sasha/Desktop/");
    }

    /**
     * Конструктор с параметром.
     * @param directory - строка содержащая путь к директории.
     */
    Linux(File directory){
        if (directory.exists()){
            if (directory.isDirectory()){
                path = directory;
            }
            else{
                path = new File("/");
            }
        }
    }
    /**
     * cd - change directory
     * "cd/" "cd /" переход корень директории
     * "cd.." "cd .." переход на уровень выше
     * @param directory абсолютный или относительный путь. Если такая директория есть, то
     *                 "командная строка" зайдёт в эту папку.
     */
    void cd(String directory){
        if (directory.length()>=1){
            /**
             * длина команды не может быть меньше 1-го символа "cd/"
             * "cd.." cd a" и так далее
             */
            if (directory.startsWith(" "))
                directory = directory.substring(1);//обрезаем первый пробел в команде если он есть
            if (directory.startsWith("/")){//"cd /" or "cd /java"
                path = new File("/");// символ слэш в команде означает переход в корневой уровень
                cd(directory.substring(1));//рекурсивно получаем нужную директорию
            }else
            if (directory.startsWith("..")){// "cd.." "cd .."
                path = path.getParentFile();// две точки в команде значат переход в родительский каталог
                cd(directory.substring(2));//рекурсивно получаем нужную директорию
            }else
            /**
             * Если в директории есть ":/" то путь абсолютный иначе относительный
             */
            if(directory.contains(":\\")){
                File new_file = new File(directory);
                if (new_file.exists())
                    path = new_file;
            }
            else{
                /**
                 * Иначе формируем новую директорию конкатенацией старой и тем, что ввел пользователь.
                 * Например текущая директория "C://" Пользователь ввел "cd Java"
                 * Значит новая директория будет "C://Java/" (если такая директория есть)
                 */
                String new_path = path.getAbsolutePath()+"\\"+directory;
                File new_file = new File(new_path);
                if (new_file.exists())
                    path = new_file;
            }
        }
    }
    /**
     * Печатает на экране все файлы, которые находятся в текущей директории.
     */
    void ls(){
        File[] files = path.listFiles();
        for (File f : files){//цикл foreach по массиву файлов: для кажног файла печатаем его имя
            System.out.println(f.getName());
        }
    }
    /**
     * @param new_path -  имя директории
     * Печатает на экране все файлы, которые находятся в  директории new_path.
     * Например, если мы сейчас в C:/ то "ls Program Files" вызовет этот метод с параметром
     * new_path="Program files" и выведется список файлов в папке "program Files"(если она существует)
     */
    void ls(String new_path){
        if (new_path.startsWith(" ")){
            new_path = new_path.substring(1);//если в начале стоит пробел, удаляем его
        }
        Linux temp = new Linux();//создаем новый объяект класса Linux
        temp.cd(this.path+"\\"+new_path);//устанавливаем в нем нужную директорию
        temp.ls();//и выводим список файлов в ней. Текущая директория остаётся неизменой
    }
    /**
     *pwd - present working directory — текущий рабочий каталог; или print working directory — вывести рабочий каталог)
     * @return строка, содержащия в себе текущую рабочую директорию
     */
    String pwd(){
        return path.getAbsolutePath();//возвращает строку = полный путь к файлу
    }
    /**
     *@param directory имя папки, которую нужно создать
     */
    void mkdir(String directory){
        if (directory.startsWith(" "))//обрезаем первый пробел, если он есть.
            directory = directory.substring(1);
        File temp = new File(path.getAbsolutePath()+"\\"+directory);
        try {//пробуем создать директорию
            temp.mkdir();
        }
        catch (Exception e){//ошибка может возникнуть, если введено некорректное имя
        //если ошибка - ничего не делаем. Когда пользователь введет правильно, тогда папка и создастся
        }
    }
    /**
     *@param directory имя папки, которую нужно удалить
     *@return true - если папка удалилась, false в противоположном случае
     */
    boolean rmdir(String directory){
        Linux temp = new Linux(this.path);
        temp.cd(directory);
        if ((temp.path.exists())&&(temp.path.isDirectory())&&(temp.path.listFiles().length==0)) {//если файл существует и является папкой и не содержит в себе файлов
            temp.path.delete();
            return true;//если папка создаётся, то возвращается true
        }
        else
            return false;//иначе false
    }
    /**
     *@param directory имя файла, который нужно удалить
     */
    void rm(String directory){
        if (!rmdir(directory));{//Если не удалилось, значит либо не папка, либо ошибка
            if (directory.startsWith(" "))//обрезаем первый пробел, если он есть.
                directory = directory.substring(1);
            File temp = new File(path.getAbsolutePath()+"\\"+directory);
            if (temp.exists())
                temp.delete();
        }
    }
    /**
     *@param arg содержит информацию о поиске. Возможные варианты команды:
     * (grep 'word' filename) (grep "word1 word2" filename1, filename2) и т.д
     *  между именами файлов ОБЯЗАТЕЛЬНО должен присутствовать пробел!!!
     */
    void grep(String arg){
        arg = arg.trim();//обрезаю пробелы по бокам
        arg = arg.concat(",");//дополняю строкой запятой, чтобы, если указан 1 файл, сплит мог распарсить строку на файлы
        String[] searches = arg.split("[']+");/*парсим строку используя разделитель '. => в массиы строк searches попадают
        части строк, находящиеся между '. Если кавычек нет, массив содержит 1 пустую строку, если есть кавычка, то одну или две,
        если 2 кавычки, то 3 элемента*/
        if((searches.length>2)&&(searches[1].length()>0)){//если слово для поиска есть и имеет длину минимум 1 символ
            String search = searches[1];//в строке search будет второй элемент массива, то есть то, что между первой и второй '
            arg = arg.replaceAll("'"+search+"'", "");//удаляем искомое слово: "'слово'"->""
            String[]files = arg.split("[,]+");//парсим через запятую оставшуюся строку на поиск файлов.
            for (int i=0; i< files.length; i++){
                files[i] = files[i].trim();//удаляем крайние пробелы в имени файла
                File file = new File(path.getAbsolutePath()+"\\"+files[i]);// сздаем файл
                if(file.exists()&&file.canRead()){//если файл существует и доступен для чтения
                    try {
                        Scanner sc = new Scanner(file);//Scanner -  класс-обертка над потоком ввода. Данный сканер будет читать из файла
                        int number=0;//номер строки
                        while (sc.hasNextLine()){//пока файл не кончился
                            number++;//инкремируем номер строки
                            String line = sc.nextLine();//читаем строку в переменную line
                            if (line.contains(search))//если прочитанная строка содержит слово(подстроку) для поиска
                                System.out.println(file.getAbsolutePath()+" "+number+" "+line);//выводим имя файла, номер строки и саму строку
                        }
                    } catch (FileNotFoundException e) {
                        //ошибка может возникнуть, если проблемы с потоком ввода, например файл недоступен или несуществует
                    }
                }
            }
    }
    }
    /**
     * копирует файл в указанную директорию.
     * @param arg строка команды. "cp file1.txt direct/file2.txt" -> arg=" file1.txt direct/file2.txt"
     */
    void cp(String arg){
        arg = arg.trim();
        try {
            String [] targets = arg.split("[ ]+");
            String first_path = targets[0];
            File first_directory = new File(path.getAbsolutePath()+"\\"+first_path);
            String other_path = targets[1];
            File other_directory = new File(path.getAbsolutePath()+"\\"+other_path);
            if (other_directory.isDirectory())
                other_directory = new File(other_directory.getAbsolutePath()+"\\"+first_path);
            Files.copy(first_directory.toPath(), other_directory.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            //do nothing
        }
    }
    /**
     * mv myfile.txt destination-directory
     * mv myfile.txt newname.txt
     * @param arg
     */
    void mv(String arg){
        cp(arg);
        arg = arg.trim();
        String [] targets = arg.split("[ ]+");
        File file = new File(this.path.getAbsolutePath()+"\\"+targets[0]);
        if (file.isDirectory())
            rm(file.getAbsolutePath());
        else
            file.delete();
    }
    /**
     * touch NEW_TEST_FILE.txt - создаёт пустой файл, если он не существует
     * touch FILE.txt - установка времени доступа и модификации файла, равному текущему времени
     * @param arg
     */
    void touch(String arg){
        arg = arg.trim();
        File file = new File(path.getAbsolutePath()+"\\"+arg);
        if (file.exists())
            file.setLastModified((new Date()).getTime());
        else
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    /**
     * cat(concatenate) Объединяет файлы в поток.
     * cat mytext.txt выведет содержимое файла на экран
     * cat mytext.txt mytext2.txt выведет последовательно содержимое 2х файлов
     * cat mytext.txt > newfile.txt выведет содержимое первого файла во второй(будет создан/перезаписан, если есть/нет)
     * cat mytext.txt >> another-text-file.txt дозапишет содержимое второго файла содержимым первого
     * @param arg
     */
    void cat(String arg){
        arg = arg.trim();
        LinkedList scanners = new LinkedList<Scanner>();
        File file_to = null;
        boolean append = false;
        try {
            String[]files = arg.split("[>]+");
            String filesFrom = files[0].trim();
            String[] _filesFrom = filesFrom.split("[ ]+");
            for (String fileFrom : _filesFrom){
                File from = new File(path.getAbsolutePath()+"\\"+fileFrom.trim());
                if (from.canRead()){
                    scanners.add(new Scanner(from));
                }
            }
        if(arg.contains(">")){//если запись в файл
            files[1] = files[1].substring(1).trim();
            file_to = new File(path.getAbsolutePath()+"\\"+files[1].trim());
            append = (arg.contains(">>"));//если >> то дозапись
        }
        Print print = new Print(scanners, file_to, append);
        } catch (Exception e) {
            //do nothing
        }
    }
    /**
     * more +3 myfile.txt
     * more +/"hope" myfile.txt
     * more letter.txt
     * @param arg
     */
    void more(String arg){
        arg = arg.trim();
        try {
            if (!arg.startsWith("+")) {//если нет +, то просто вывод файла на экран
                File file1 = new File(path.getAbsolutePath() + "\\" + arg.trim());
                LinkedList scanners = new LinkedList<Scanner>();
                scanners.add(new Scanner(file1));
                new Print(scanners, null, false);
            }
            else{
                arg = arg.substring(1);
                if (arg.startsWith("/\"")){//
                    arg = arg.substring(2);
                    String[] param = arg.split("[\"]+");
                    String slovo = param[0];
                    String _file = param[1].trim();
                    File file = new File(path.getAbsolutePath()+"\\"+_file);
                    if (file.canRead()){
                        Scanner scanner = new Scanner(file);
                        boolean out = false;
                        while (scanner.hasNext()){
                            String line = scanner.nextLine();
                            if (!out)
                                out = line.contains(slovo);
                            if (out)
                                System.out.println(line);
                        }
                        scanner.close();
                    }
                }
                else{//more +3 myfile.txt
                    String[] params = arg.split("[ ]+");
                    int count = Integer.parseInt(params[0]);
                    File file = new File(path.getAbsolutePath()+"\\"+params[1].trim());
                    if (file.canRead()){
                        Scanner sc = new Scanner(file);
                        int i=1;
                        while (sc.hasNext()){
                            String line = sc.nextLine();
                            if (i>=count)
                                System.out.println(line);
                            i++;
                        }
                        sc.close();
                    }
                }
            }

        } catch (Exception e) {
            //
        }
    }
    /**
     * echo string
     * echo "string" > filename
     * echo "string" >> filename
     * @param arg строка
     */
    void echo(String arg){
        arg = arg.trim();
        if (!arg.contains(">"))
            System.out.println(arg);
        else{
            boolean append = arg.contains(">>");
            String string = arg.split("[\"]+")[1];
            String file_to;
            if (append)
                file_to = arg.split("[>>]+")[1];
            else
                file_to = arg.split("[>]+")[1];
            File file = new File(path.getAbsolutePath()+"\\"+file_to.trim());
            FilePrinter printer = new FilePrinter();
            LinkedList list = new LinkedList<String>();
            list.add(string);
            printer.Print(list, file, append);
        }
    }
 /**
 * Переменная типа File хранит в себе директорию в которой находится пользователь.
 */
    private File path;
}