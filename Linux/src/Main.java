import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while(!((input = scanner.nextLine()).equals("exit"))){//пока пользователь не ввёл exit
            /**если введённая строка начинается с подстроки "ls",
             * то вызываем у класса Linux метод ls.
             * Если строка = "ls",  то вызывается метод без параметров,
             * в противном случае в качестве параметра передаётся оставшеяся подстрока.
             * Например если пользователь ввёл "ls a" передаём в метод ls строку "a"
             * и будет выведено на экран содержимое директории "a" (если она существует)
             */
            if (input.startsWith(Commands.ls.toString())){
                if (input.equals(Commands.ls.toString()))
                    linux.ls();
                else
                    linux.ls(input.substring(2));
            }else
            if (input.startsWith(Commands.cd.toString())){
                linux.cd(input.substring(2));
            }else
            if (input.equals(Commands.pwd.toString())){
                System.out.println(linux.pwd());
            }else
            if (input.startsWith(Commands.mkdir.toString())){
                linux.mkdir(input.substring(5));
            }else
            if (input.startsWith(Commands.rmdir.toString())){
                linux.rmdir(input.substring(5));
            }else
            if (input.startsWith(Commands.rm.toString())){
                linux.rm(input.substring(2));
            }else
            if (input.startsWith(Commands.grep.toString())){
                linux.grep(input.substring(4));
            }else
            if (input.startsWith(Commands.cp.toString())){
                linux.cp(input.substring(2));
            }else
            if (input.startsWith(Commands.mv.toString())){
                linux.mv(input.substring(2));
            }else
            if (input.startsWith(Commands.touch.toString())){
                linux.touch(input.substring(5   ));
            }else
            if (input.startsWith(Commands.cat.toString())){
                linux.cat(input.substring(3));
            }else
            if (input.startsWith(Commands.more.toString())){
                linux.more(input.substring(4));
            }else
            if (input.startsWith(Commands.echo.toString())){
                linux.echo(input.substring(4));
            }
        }
    }

    //Создаём объект класса Linux(наш эмулятор командной строки)
    static Linux linux = new Linux();
    //Создаём объект класса Scanner, который используется для получения данных с потока ввода
    static Scanner scanner = new Scanner(System.in);
    //Создаём строку input в которую будем считывать введённую пользователем в консоль строку
    static String input;
    //Объект класса Перечисление. Элементы перечисления - наши команды. Таким образом оберегаем себя от возможных ошибок
    enum Commands {ls, cd, pwd, mkdir, rmdir, rm, grep, cp, mv, touch, cat, more, echo}
}
