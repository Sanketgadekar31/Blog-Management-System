import java.sql.*;
import java.util.Scanner;


public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/blog_db";
    private static final String username = "root";
    private static final String password = "Sanket@3107";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true){
                System.out.println();
                System.out.println("BLOG MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Add a new Blog");
                System.out.println("2. View blog details");
                System.out.println("3. Update blog details");
                System.out.println("4. Delete a blog");
                System.out.println("5. Add comment on blog");
                System.out.println("6. View comment");
                System.out.println("7. Update comment");
                System.out.println("8. Delete a comment");
                System.out.println("0. EXIT");
                System.out.println("Choose an option: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        newBlog(connection, scanner);
                        break;
                    case 2:
                        viewBlog(connection);
                        break;
                    case 3:
                        updateBlog(connection, scanner);
                        break;
                    case 4:
                        deleteBlog(connection, scanner);
                        break;
                    case 5:
                        newComment(connection, scanner);
                        break;
                    case 6:
                        viewComment(connection);
                        break;
                    case 7:
                        updateComment(connection, scanner);
                        break;
                    case 8:
                        deleteComment(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice. Try again!");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    /*Blog Section Code*/

    // Create new blog
    private static void newBlog(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter blog title:");
            String blogTitle = scanner.next();
            scanner.nextLine();
            System.out.println("Enter blog description:");
            String blogDescription = scanner.next();
            scanner.nextLine();
            System.out.println("Enter blog author name:");
            String blogAuthor = scanner.next();

            String sql = "INSERT INTO blog (title, description, author)" +
                    "VALUES ('" + blogTitle + "', '" + blogDescription + "', '" + blogAuthor + "')";

            try (Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0){
                    System.out.println("Blog Added Successfully!");
                } else {
                    System.out.println("Blog Failed to get upload! Try Again");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // View Blog
    private static void viewBlog(Connection connection) throws SQLException{
        String sql = "SELECT blog_id, title, description, author, creation_date FROM blog";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("List of Blogs:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Blog ID | Title           | Description   | Author      | Creation Date         ");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int blogId = resultSet.getInt("blog_id");
                String blogTitle = resultSet.getString("title");
                String blogDescription = resultSet.getString("description");
                String blogAuthor = resultSet.getString("author");
                String creationDate = resultSet.getTimestamp("creation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-7d | %-15s | %-13s | %-11s | %-19s   \n",
                        blogId, blogTitle, blogDescription, blogAuthor, creationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }

    // Update Blog
    private static void updateBlog(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter blog ID to update: ");
            int blogId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Check if the blog exists
            if (!blogExists(connection, blogId)) {
                System.out.println("Blog not found for the given ID.");
                return;
            }

            System.out.print("Enter new title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new description: ");
            String newDescription = scanner.nextLine();
            System.out.print("Change Author Name: ");
            String newAuthor = scanner.nextLine();

            // Use PreparedStatement to prevent SQL injection and handle strings correctly
            String sql = "UPDATE blog SET title = ?, description = ?, author = ? WHERE blog_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newTitle);
                preparedStatement.setString(2, newDescription);
                preparedStatement.setString(3, newAuthor);
                preparedStatement.setInt(4, blogId);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Blog updated successfully!");
                } else {
                    System.out.println("Blog update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete blog
    private static void deleteBlog(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter blog ID to delete: ");
            int blogId = scanner.nextInt();

            if (!blogExists(connection, blogId)) {
                System.out.println("Blog not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM blog WHERE blog_id = " + blogId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Blog deleted successfully!");
                } else {
                    System.out.println("Blog deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // other functions
    private static boolean blogExists(Connection connection, int blogId) {
        try {
            String sql = "SELECT blog_id FROM blog WHERE blog_id = " + blogId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }



    /*Comment Section Code*/

    // Post new Comment
    private static void newComment(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter Blog ID to comment:");
            int blogId = scanner.nextInt();
            System.out.println("Enter comment:");
            String comment = scanner.next();

            String sql = "INSERT INTO comment (content, blog_id)" +
                    "VALUES ('" + comment + "', '" + blogId + "')";

            try (Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0){
                    System.out.println("Comment Added Successfully!");
                } else {
                    System.out.println("Failed! Try Again");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // View Blog
    private static void viewComment(Connection connection) throws SQLException{
        String sql = "SELECT blog.blog_id, blog.title, blog.description, comment.comment_id, comment.content FROM Blog LEFT JOIN comment ON blog.blog_id = comment.blog_id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("List of Blogs:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Blog ID | Title           | Description     | Comment ID | Comment    ");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int blogId = resultSet.getInt("blog_id");
                String blogTitle = resultSet.getString("title");
                String blogDescription = resultSet.getString("description");
                int commentId = resultSet.getInt("comment_id");
                String comment = resultSet.getString("content");

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-7d | %-15s | %-15s | %-7d | %-10s   \n",
                        blogId, blogTitle, blogDescription, commentId, comment);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }

    // Update Blog
    private static void updateComment(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter comment ID to update: ");
            int commentId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            // Check if the blog exists
            if (!commentExists(connection, commentId)) {
                System.out.println("Comment not found for the given ID.");
                return;
            }

            System.out.println("Enter new content for the comment: ");
            String newContent = scanner.nextLine();

            // Use PreparedStatement to prevent SQL injection and handle strings correctly
            String sql = "UPDATE comment SET content = ? WHERE comment_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newContent);
                preparedStatement.setInt(2, commentId);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Comment updated successfully!");
                } else {
                    System.out.println("Comment update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete blog
    private static void deleteComment(Connection connection, Scanner scanner) {
        try {
            System.out.print("Comment blog ID to delete: ");
            int commentId = scanner.nextInt();

            if (!commentExists(connection, commentId)) {
                System.out.println("Comment not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM comment WHERE comment_id = " + commentId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Comment deleted successfully!");
                } else {
                    System.out.println("Comment deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // other functions
    private static boolean commentExists(Connection connection, int commentId) {
        try {
            String sql = "SELECT comment_id FROM comment WHERE comment_id = " + commentId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Blog Management System!!!");
    }
}