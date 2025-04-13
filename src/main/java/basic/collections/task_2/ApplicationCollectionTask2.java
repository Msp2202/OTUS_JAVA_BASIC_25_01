package basic.collections.task_2;

public class ApplicationCollectionTask2 {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Иванов", "111-11-11");
        phoneBook.add("Петров", "222-22-22");
        phoneBook.add("Иванов", "333-33-33");

        phoneBook.printInfoAllContacts();
    }
}
