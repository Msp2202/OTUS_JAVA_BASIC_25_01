package basic.io;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileEditor {
    private static final String ROOT_DIR = ".";
    private static final String TXT_EXTENSION = ".txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            /**
             * 1. Получаем список текстовых файлов
             */
            List<Path> textFiles = listTextFiles(ROOT_DIR);

            if (textFiles.isEmpty()) {
                System.out.println("В текущей директории нет .txt файлов.");
                return;
            }

            /**
             * 2. Выводим список файлов
             */
            System.out.println("Список текстовых файлов:");
            for (int i = 0; i < textFiles.size(); i++) {
                System.out.println((i + 1) + ". " + textFiles.get(i).getFileName());
            }

            /**
             * 3. Запрашиваем выбор файла
             */
            System.out.print("\nВведите номер файла для работы: ");
            int fileNumber = Integer.parseInt(scanner.nextLine()) - 1;

            if (fileNumber < 0 || fileNumber >= textFiles.size()) {
                System.out.println("Неверный номер файла!");
                return;
            }

            Path selectedFile = textFiles.get(fileNumber);

            /**
             * 4. Выводим содержимое файла
             */
            System.out.println("\nСодержимое файла " + selectedFile.getFileName() + ":");
            printFileContent(selectedFile);

            /**
             * 5. Режим записи в файл с отображением изменений
             */
            System.out.println("\nВведите текст для добавления в файл (для выхода введите 'exit'):");
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                appendToFile(selectedFile, input);
                System.out.println("Текст добавлен в файл.");

                /**
                 * Показываем обновленное содержимое
                 */
                System.out.println("\nТекущее содержимое файла:");
                printFileContent(selectedFile);
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static List<Path> listTextFiles(String dir) throws IOException {
        List<Path> textFiles = new ArrayList<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().toLowerCase().endsWith(TXT_EXTENSION)) {
                    textFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
        return textFiles;
    }

    private static void printFileContent(Path file) throws IOException {
        if (Files.exists(file)) {
            BufferedReader reader = Files.newBufferedReader(file);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } else {
            System.out.println("Файл пуст или не существует.");
        }
    }

    private static void appendToFile(Path file, String text) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(
                file, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        writer.write(text);
        writer.newLine();
        writer.close();
    }
}