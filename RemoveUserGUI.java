import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class RemoveUserGUI extends JPanel {
    private MainGUI mainGUI;
    private JPanel backgroundPanel;
    private JPanel centerPanel;

    public RemoveUserGUI(MainGUI mainGUI, JPanel backgroundPanel) {
        this.mainGUI = mainGUI;
        this.backgroundPanel = backgroundPanel;

        setLayout(new BorderLayout());
        setBackground(Color.white);

        // Top panel for title and search bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.white);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Title
        JLabel title = new JLabel("Remove Member");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(title, BorderLayout.NORTH);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.white);
        JTextField searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterUsers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterUsers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterUsers();
            }

            private void filterUsers() {
                String query = searchField.getText().toLowerCase();
                centerPanel.removeAll();

                List<User> users = UserManager.getAllUsers();
                for (User user : users) {
                    String fullName = user.getFirstName().toLowerCase() + " " + user.getLastName().toLowerCase();
                    if (fullName.contains(query)) {
                        JPanel userPanel = new JPanel(new BorderLayout());
                        userPanel.setBackground(Color.white);
                        userPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.BLACK, 1),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));

                        JLabel nameLabel = new JLabel(user.getFirstName() + " " + user.getLastName());
                        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        nameLabel.setForeground(Color.BLACK);
                        userPanel.add(nameLabel, BorderLayout.CENTER);

                        JButton removeButton = new JButton("Remove");
                        removeButton.setBackground(Color.BLACK);
                        removeButton.setForeground(Color.WHITE);
                        removeButton.setPreferredSize(new Dimension(100, 30));
                        removeButton.addActionListener(e -> {
                            boolean success = UserManager.removeUser(user);
                            if (success) {
                                JOptionPane.showMessageDialog(RemoveUserGUI.this, "User removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                refreshUserList();
                            } else {
                                JOptionPane.showMessageDialog(RemoveUserGUI.this, "Failed to remove user.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        userPanel.add(removeButton, BorderLayout.EAST);

                        centerPanel.add(userPanel);
                    }
                }

                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });


        add(topPanel, BorderLayout.NORTH);

        // Center panel to display members
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.white);

        // Add scroll pane with padding
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        JPanel scrollPanelWithPadding = new JPanel(new BorderLayout());
        scrollPanelWithPadding.setBackground(Color.white);
        scrollPanelWithPadding.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPanelWithPadding.add(scrollPane, BorderLayout.CENTER);

        add(scrollPanelWithPadding, BorderLayout.CENTER);

        // Back Button Panel
        JPanel backButtonPanel = new JPanel(null);
        backButtonPanel.setBackground(Color.white);
        backButtonPanel.setPreferredSize(new Dimension(400, 50));

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> switchToManageUserPage());

        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH);

        // Load users into the GUI
        refreshUserList();
    }

    // Method to dynamically load users into the GUI
    public void refreshUserList() {
        centerPanel.removeAll(); // Clear the panel before reloading

        AddUser.loadUsers();

        List<User> users = UserManager.getAllUsers();
        for (User user : users) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setBackground(Color.white);
            userPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel nameLabel = new JLabel(user.getFirstName() + " " + user.getLastName());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            nameLabel.setForeground(Color.BLACK);
            userPanel.add(nameLabel, BorderLayout.CENTER);

            JButton removeButton = new JButton("Remove");
            removeButton.setBackground(Color.BLACK);
            removeButton.setForeground(Color.WHITE);
            removeButton.setPreferredSize(new Dimension(100, 30));
            removeButton.addActionListener(e -> {
                boolean success = UserManager.removeUser(user);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserList(); // Refresh the list after removing a user
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            userPanel.add(removeButton, BorderLayout.EAST);

            centerPanel.add(userPanel);
        }

        centerPanel.revalidate(); // Revalidate the panel
        centerPanel.repaint();   // Repaint the panel to reflect changes
    }

    // This method switches to the ManageUserPage using CardLayout
    private void switchToManageUserPage() {
        CardLayout cl = (CardLayout) backgroundPanel.getLayout();
        cl.show(backgroundPanel, "ManageUserPage");
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }
}
