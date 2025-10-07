package ru.vsu.cs.bazykin.viewer;

import ru.vsu.cs.bazykin.api.CreateArchiveRequest;
import ru.vsu.cs.bazykin.api.UpdateArchiveRequest;
import ru.vsu.cs.bazykin.model.Archive;
import ru.vsu.cs.bazykin.service.ArchiveService;
import ru.vsu.cs.bazykin.service.FileEditor;

import java.util.List;
import java.util.Scanner;

public class ArchiveViewer {
    private final Scanner scanner;
    private Archive currentArchive;

    private ArchiveService service;

    public ArchiveViewer(ArchiveService service) {
        this.scanner = new Scanner(System.in);
        this.service = service;
    }

    public void start() {
        System.out.println("=== Консольный просмотрщик архивов ===");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createArchive();
                    break;
                case "2":
                    openArchive();
                    break;
                case "3":
                    viewArchiveContents();
                    break;
                case "4":
                    addFileToArchive();
                    break;
                case "5":
                    removeFileFromArchive();
                    break;
                case "0":
                    System.out.println("Выход из программы");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("(введите число отвечающее за необходимую операцию)");
        System.out.println("1. Создать архив");
        System.out.println("2. Открыть архив");
        System.out.println("3. Просмотреть содержимое архива");
        System.out.println("4. Добавить файл в архив");
        System.out.println("5. Удалить файл из архива");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void createArchive(){
        System.out.print("Введите путь, где необходимо создать архив: ");
        String archivePath = scanner.nextLine();
        if (!FileEditor.isValidPath(archivePath)){
            System.out.print("Неверно указан путь");
            return;
        }
        System.out.print("Введите название архива: ");
        String archiveName = scanner.nextLine();
        String fullPath = archivePath + archiveName + ".arc";
        this.currentArchive = service.create(new CreateArchiveRequest(fullPath));
        System.out.println("Архив также был открыт");

    }

    private void openArchive() {
        System.out.print("Введите путь к архиву: ");
        String archivePath = scanner.nextLine();
        if (!FileEditor.isValidPath(archivePath)){
            System.out.print("Неверно указан путь");
            return;
        }
        List<String> archiveContent = FileEditor.readListFromFile(archivePath);
        if (archiveContent != null && !archiveContent.isEmpty()){
            this.currentArchive = service.getById(archiveContent.getFirst());
        }
        System.out.println("Архив открыт: " + archivePath);
    }

    private void viewArchiveContents() {
        if (currentArchive == null) {
            System.out.println("Сначала откройте архив!");
            return;
        }
        System.out.println("Содержимое архива :");
        System.out.println("Дата создания архива " + currentArchive.getContent().get(1));
        for (int i = 2; i < currentArchive.getContent().size(); i += 2){
            System.out.println(currentArchive.getContent().get(i));}
        System.out.println("Конец архива");
    }

    private void addFileToArchive() {
        if (currentArchive == null) {
            System.out.println("Сначала откройте архив!");
            return;
        }
        System.out.print("Введите путь к файлу для добавления: ");
        String filePath = scanner.nextLine();
        if (service.update(currentArchive.getId(), new UpdateArchiveRequest(filePath, "add")) != null){
            System.out.println("Файл успешно добавлен в архив");
        } else {
            System.out.println("Ошибка при добавлении файла");
        }
    }

    private void removeFileFromArchive() {
        if (currentArchive == null) {
            System.out.println("Сначала откройте архив!");
            return;
        }
        System.out.print("Введите имя файла для удаления: ");
        String fileName = scanner.nextLine();
        if (service.update(currentArchive.getId(), new UpdateArchiveRequest(fileName, "delete")) != null){
            System.out.println("Файл " + fileName + " удален из архива");
        } else System.out.println("Ошибка при удалении файла");
    }
}
