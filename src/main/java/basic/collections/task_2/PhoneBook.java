package basic.collections.task_2;

import java.util.*;

public class PhoneBook {
    private final Map<String, Set<String>> contacts = new HashMap<>();

    public void add(String name, String phoneNumber) {
        if (contacts.containsKey(name)) {
            Set<String> numbers = contacts.get(name);
            numbers.add(phoneNumber);
        } else {
            Set<String> numbers = new HashSet<>();
        }
    }

    public Set<String> find(String name) {
        if (contacts.containsKey(name)) {
            return contacts.get(name);
        }
        return Collections.emptySet();
    }

    public boolean containsPhoneNumber(String phoneNumber) {
        for (Set<String> numbers : contacts.values()) {
            if (numbers.contains(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public void printInfoAllContacts() {
        System.out.println("Телефонная книга");
        for (Map.Entry<String, Set<String>> entry : contacts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
