import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminApp extends JFrame {
    private JTextField idField, nameField, emailField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private AdminCRUD AdminCRUD;

    public AdminApp() {
    	AdminCRUD = new AdminCRUD();

        setTitle("CRUD Application");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("ID:"));
        idField = new JTextField(20);
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField);

        addButton = new JButton("Add");
        addButton.addActionListener(new AddAction());
        add(addButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(new UpdateAction());
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteAction());
        add(deleteButton);

        viewButton = new JButton("View");
        viewButton.addActionListener(new ViewAction());
        add(viewButton);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class AddAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AdminCRUD.addUser(nameField.getText(), emailField.getText());
            JOptionPane.showMessageDialog(null, "User added!");
        }
    }

    private class UpdateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(idField.getText());
            AdminCRUD.updateUser(id, nameField.getText(), emailField.getText());
            JOptionPane.showMessageDialog(null, "User updated!");
        }
    }

    private class DeleteAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int id = Integer.parseInt(idField.getText());
            AdminCRUD.deleteUser(id);
            JOptionPane.showMessageDialog(null, "User deleted!");
        }
    }

    private class ViewAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<mainUser> users = AdminCRUD.getAllUsers();
            for (mainUser user : users) {
                System.out.println(user);
            }
        }
    }

    public static void main(String[] args) {
        new AdminApp();
    }
}
