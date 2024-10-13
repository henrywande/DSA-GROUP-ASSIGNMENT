 package phonebookapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

// Contact class to represent each contact in the phonebook
class Contact {
    String name;
    String phoneNumber;
    Contact next;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.next = null;
    }
}

// Phonebook class that manages contacts using a linked list
class Phonebook {
    Contact head;

    public Phonebook() {
        head = null;
    }

    public void insertContact(String name, String phoneNumber) {
        Contact newContact = new Contact(name, phoneNumber);
        newContact.next = head;
        head = newContact;
    }

    public Contact searchContact(String name) {
        Contact current = head;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public void deleteContact(String name) {
        Contact current = head;
        Contact prev = null;
        while (current != null && !current.name.equalsIgnoreCase(name)) {
            prev = current;
            current = current.next;
        }

        if (current == null) {
            JOptionPane.showMessageDialog(null, "Contact not found.");
            return;
        }

        if (prev == null) {
            head = current.next;
        } else {
            prev.next = current.next;
        }
        JOptionPane.showMessageDialog(null, "Contact deleted successfully.");
    }

    public void updateContact(String name, String newPhoneNumber) {
        Contact current = searchContact(name);
        if (current != null) {
            current.phoneNumber = newPhoneNumber;
            JOptionPane.showMessageDialog(null, "Contact updated successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Contact not found.");
        }
    }

    // Extract contacts, sort them by name, and return the sorted list as a string
    public String displaySortedContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        Contact current = head;

        // Extract all contacts into an ArrayList
        while (current != null) {
            contactList.add(current);
            current = current.next;
        }

        // Sort contacts by name using a Comparator
        contactList.sort(Comparator.comparing(contact -> contact.name.toLowerCase()));

        // Build a string of sorted contacts
        StringBuilder sortedContactsList = new StringBuilder();
        for (Contact contact : contactList) {
            sortedContactsList.append(contact.name).append(": ").append(contact.phoneNumber).append("\n");
        }

        if (sortedContactsList.length() == 0) {
            return "No contacts found.";
        }
        return sortedContactsList.toString();
    }

    // Normal display without sorting
    public String displayContacts() {
        Contact current = head;
        StringBuilder contactsList = new StringBuilder();
        while (current != null) {
            contactsList.append(current.name).append(": ").append(current.phoneNumber).append("\n");
            current = current.next;
        }
        if (contactsList.length() == 0) {
            return "No contacts found.";
        }
        return contactsList.toString();
    }
}

// GUI class
public class PhonebookApplication extends JFrame {

    private Phonebook phonebook;
    private JTextField nameField, phoneField, searchField, deleteField, updateField, updatePhoneField;
    private JTextArea displayArea;

    public PhonebookApplication() {
        phonebook = new Phonebook();

        // Setup JFrame
        setTitle("Phonebook Application");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Create a panel for the insert contact section
        JPanel insertPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        insertPanel.setBorder(BorderFactory.createTitledBorder("Insert Contact"));
        
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        JButton insertButton = new JButton("Insert Contact");

        insertPanel.add(nameLabel);
        insertPanel.add(nameField);
        insertPanel.add(phoneLabel);
        insertPanel.add(phoneField);
        insertPanel.add(new JLabel()); // Empty cell for spacing
        insertPanel.add(insertButton);

        // Create a panel for the search section
        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Contact"));
        
        JLabel searchLabel = new JLabel("Search Name:");
        searchField = new JTextField();
        JButton searchButton = new JButton("Search Contact");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(new JLabel()); // Empty cell for spacing
        searchPanel.add(searchButton);

        // Create a panel for the delete section
        JPanel deletePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Contact"));
        
        JLabel deleteLabel = new JLabel("Delete Name:");
        deleteField = new JTextField();
        JButton deleteButton = new JButton("Delete Contact");

        deletePanel.add(deleteLabel);
        deletePanel.add(deleteField);
        deletePanel.add(new JLabel()); // Empty cell for spacing
        deletePanel.add(deleteButton);

        // Create a panel for the update section
        JPanel updatePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Update Contact"));

        JLabel updateLabel = new JLabel("Update Name:");
        updateField = new JTextField();
        JLabel updatePhoneLabel = new JLabel("New Phone Number:");
        updatePhoneField = new JTextField();
        JButton updateButton = new JButton("Update Contact");

        updatePanel.add(updateLabel);
        updatePanel.add(updateField);
        updatePanel.add(updatePhoneLabel);
        updatePanel.add(updatePhoneField);
        updatePanel.add(new JLabel()); // Empty cell for spacing
        updatePanel.add(updateButton);

        // Create the display buttons and area
        JPanel displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton displayButton = new JButton("Display All Contacts");
        JButton sortButton = new JButton("Display Sorted Contacts (A to Z)");
        JButton exitButton = new JButton("Exit");
        displayPanel.add(displayButton);
        displayPanel.add(sortButton);
        displayPanel.add(exitButton);

        displayArea = new JTextArea(15, 50);
        displayArea.setBorder(BorderFactory.createTitledBorder("Contacts"));
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add action listeners for buttons
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                if (!name.isEmpty() && !phone.isEmpty()) {
                    phonebook.insertContact(name, phone);
                    JOptionPane.showMessageDialog(null, "Contact added.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter both name and phone number.");
                }
                nameField.setText("");
                phoneField.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = searchField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a name to search.");
                    return;
                }
                Contact contact = phonebook.searchContact(name);
                if (contact != null) {
                    JOptionPane.showMessageDialog(null, "Found: " + contact.name + " - " + contact.phoneNumber);
                } else {
                    JOptionPane.showMessageDialog(null, "Contact not found.");
                }
                searchField.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = deleteField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a name to delete.");
                    return;
                }
                phonebook.deleteContact(name);
                deleteField.setText("");
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = updateField.getText().trim();
                String newPhone = updatePhoneField.getText().trim();
                if (!name.isEmpty() && !newPhone.isEmpty()) {
                    phonebook.updateContact(name, newPhone);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter both name and new phone number.");
                }
                updateField.setText("");
                updatePhoneField.setText("");
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(phonebook.displayContacts());
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(phonebook.displaySortedContacts());
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Add all panels to the main panel
        mainPanel.add(insertPanel);
        mainPanel.add(searchPanel);
        mainPanel.add(deletePanel);
        mainPanel.add(updatePanel);
        mainPanel.add(displayPanel);

        // Add the main panel and display area to the frame
        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the Phonebook application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PhonebookApplication(); // Create and show the GUI
            }
        });
    }
}
