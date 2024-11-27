import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageFinesGUI extends JPanel {
    private JPanel backgroundPanel;
    private Map<String, Double> userFines = new HashMap<>(); // Stores username -> fine amount
    private List<User> users = new ArrayList<>(); // Users loaded from the database

    private static final String FINES_FILE = "fines.txt"; // File to store fines data

    public ManageFinesGUI(MainGUI mainGUI, JPanel backgroundPanel) {
        this.backgroundPanel = backgroundPanel;
        setLayout(new BorderLayout());
        setBackground(Color.white);

        // Load users and fines
        loadUsersAndFines();

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.white);
        JLabel title = new JLabel("Manage Fines");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // Center panel for fine details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.white);
        populateFineDetails(centerPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.white);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainGUI.switchToMainPage());

        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH);
    }

    private void loadUsersAndFines() {
        // Load users from the database (users.txt)
        users = AddUser.loadUsers();

        // Load fines from fines.txt
        userFines = loadFines();

        // Ensure all users in the database have a fine entry
        for (User user : users) {
            String username = user.getFirstName() + " " + user.getLastName();
            userFines.putIfAbsent(username, 0.0); // Default fine is 0
        }

        // Save updated fines back to the fines file
        saveFines();
    }

    private void populateFineDetails(JPanel centerPanel) {
        centerPanel.removeAll(); // Clear previous components

        for (User user : users) {
            String username = user.getFirstName() + " " + user.getLastName();
            double fineAmount = userFines.getOrDefault(username, 0.0);

            JPanel finePanel = new JPanel(new BorderLayout());
            finePanel.setBackground(Color.white);
            finePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel userLabel = new JLabel(username + " - Fine: $" + fineAmount);
            userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            finePanel.add(userLabel, BorderLayout.CENTER);

            JButton clearFineButton = new JButton("Clear Fine");
            clearFineButton.setBackground(Color.BLACK);
            clearFineButton.setForeground(Color.WHITE);
            clearFineButton.setPreferredSize(new Dimension(100, 30));
            clearFineButton.addActionListener(e -> {
                userFines.put(username, 0.0); // Clear fine
                saveFines(); // Save updated fines to file
                JOptionPane.showMessageDialog(this, "Fine cleared for " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
                populateFineDetails(centerPanel); // Refresh fine details
                centerPanel.revalidate();
                centerPanel.repaint();
            });
            finePanel.add(clearFineButton, BorderLayout.EAST);

            centerPanel.add(finePanel);
        }
    }

    private Map<String, Double> loadFines() {
        Map<String, Double> fines = new HashMap<>();
        try (Scanner scanner = new Scanner(new java.io.File(FINES_FILE))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2) {
                    fines.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Fines file not found. Initializing fines to default values.");
        }
        return fines;
    }

    private void saveFines() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(FINES_FILE))) {
            for (Map.Entry<String, Double> entry : userFines.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToMainPanel() {
        CardLayout cl = (CardLayout) backgroundPanel.getLayout();
        cl.show(backgroundPanel, "MainPage");
    }
}