import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String FILE_NAME = "users.txt";

    // Read all users from the file
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Remove a user from the file
    public static boolean removeUser(User userToRemove) {
        List<User> users = getAllUsers();
        boolean removed = users.removeIf(user -> user.toString().equals(userToRemove.toString()));
        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (User user : users) {
                    writer.write(user.toString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean updateUser(User updatedUser) {
        // Load all users from the file
        List<User> users = AddUser.loadUsers();

        // Replace the user with matching username
        boolean userFound = false;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser); // Update user in the list
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            return false; // User not found
        }

        // Save the updated list back to the file
        return AddUser.updateUserFile(users);
    }
}
