import javax.swing.*;
import java.awt.*;

public class UpdateUserDialog extends JDialog {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField contactField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private User user;

    public UpdateUserDialog(JFrame parent, User user) {
        super(parent, "Update User", true);
        this.user = user;

        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(user.getFirstName());
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(user.getLastName());
        add(lastNameField);

        add(new JLabel("Contact Number:"));
        contactField = new JTextField(user.getContactNumber());
        add(contactField);

        add(new JLabel("Username:"));
        usernameField = new JTextField(user.getUsername());
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (validateFields()) {
                user.setFirstName(firstNameField.getText());
                user.setLastName(lastNameField.getText());
                user.setContactNumber(contactField.getText());
                user.setUsername(usernameField.getText());
                user.setPassword(new String(passwordField.getPassword()));

                boolean success = UserManager.updateUser(user); // Save updated user
                if (success) {
                    JOptionPane.showMessageDialog(this, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    private boolean validateFields() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                contactField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getPassword().length == 0 || confirmPasswordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
