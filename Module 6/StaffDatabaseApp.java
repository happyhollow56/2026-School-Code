import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class StaffDatabaseApp extends JFrame {
    // Text fields for each column
    private final JTextField jtfID = new JTextField(9);
    private final JTextField jtfLastName = new JTextField(15);
    private final JTextField jtfFirstName = new JTextField(15);
    private final JTextField jtfMI = new JTextField(1);
    private final JTextField jtfAddress = new JTextField(20);
    private final JTextField jtfCity = new JTextField(20);
    private final JTextField jtfState = new JTextField(2);
    private final JTextField jtfTelephone = new JTextField(10);
    private final JTextField jtfEmail = new JTextField(40);

    private final JButton jbtView = new JButton("View");
    private final JButton jbtInsert = new JButton("Insert");
    private final JButton jbtUpdate = new JButton("Update");
    private final JLabel jlblStatus = new JLabel(" ");

    // --- Database configuration ---
    // Update these values for your database setup
    private static final String DB_URL = "jdbc:mysql://localhost:3306/staffdb";
    private static final String DB_USER = "staffuser";
    private static final String DB_PASSWORD = "staffpassword";

    public StaffDatabaseApp() {
        setTitle("Staff Database Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(null);

        // Form panel using GridBagLayout for clean alignment
        JPanel jpForm = new JPanel(new GridBagLayout());
        jpForm.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Helper to add label + field rows
        addFormRow(jpForm, gbc, 0, "ID:", jtfID);
        addFormRow(jpForm, gbc, 1, "Last Name:", jtfLastName);
        addFormRow(jpForm, gbc, 2, "First Name:", jtfFirstName);
        addFormRow(jpForm, gbc, 3, "MI:", jtfMI);
        addFormRow(jpForm, gbc, 4, "Address:", jtfAddress);
        addFormRow(jpForm, gbc, 5, "City:", jtfCity);
        addFormRow(jpForm, gbc, 6, "State:", jtfState);
        addFormRow(jpForm, gbc, 7, "Telephone:", jtfTelephone);
        addFormRow(jpForm, gbc, 8, "Email:", jtfEmail);

        // Buttons panel
        JPanel jpButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        jpButtons.add(jbtView);
        jpButtons.add(jbtInsert);
        jpButtons.add(jbtUpdate);

        // Status label
        jlblStatus.setFont(new Font("Dialog", Font.BOLD, 12));
        jlblStatus.setForeground(Color.BLUE);
        JPanel jpStatus = new JPanel(new BorderLayout());
        jpStatus.setBorder(new EmptyBorder(0, 10, 5, 10));
        jpStatus.add(jlblStatus, BorderLayout.WEST);

        // Assemble frame
        setLayout(new BorderLayout());
        add(jpForm, BorderLayout.CENTER);
        add(jpButtons, BorderLayout.SOUTH);
        add(jpStatus, BorderLayout.NORTH);

        // Button listeners
        jbtView.addActionListener(e -> view());
        jbtInsert.addActionListener(e -> insert());
        jbtUpdate.addActionListener(e -> update());

        // Initialize database driver
        initializeDB();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JTextField textField) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField, gbc);
    }

    private void initializeDB() {
        try {
            // For MySQL: ensure mysql-connector-java is in your classpath
            Class.forName("com.mysql.cj.jdbc.Driver");
            jlblStatus.setText("Database driver loaded.");
        } catch (ClassNotFoundException ex) {
            jlblStatus.setText("Error: JDBC Driver not found.");
            jlblStatus.setForeground(Color.RED);
        }
    }

    /** Retrieves a record by ID and populates the form. */
    private void view() {
        String id = jtfID.getText().trim();
        if (id.isEmpty()) {
            jlblStatus.setText("Please enter an ID to view.");
            return;
        }

        String sql = "SELECT * FROM Staff WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    jtfLastName.setText(rs.getString("lastName"));
                    jtfFirstName.setText(rs.getString("firstName"));
                    jtfMI.setText(rs.getString("mi"));
                    jtfAddress.setText(rs.getString("address"));
                    jtfCity.setText(rs.getString("city"));
                    jtfState.setText(rs.getString("state"));
                    jtfTelephone.setText(rs.getString("telephone"));
                    jtfEmail.setText(rs.getString("email"));
                    jlblStatus.setText("Record found.");
                    jlblStatus.setForeground(Color.BLUE);
                } else {
                    jlblStatus.setText("Record not found for ID: " + id);
                    jlblStatus.setForeground(Color.RED);
                    clearFieldsExceptID();
                }
            }
        } catch (SQLException ex) {
            jlblStatus.setText("Error: " + ex.getMessage());
            jlblStatus.setForeground(Color.RED);
        }
    }

    /** Inserts a new record using the current form values. */
    private void insert() {
        String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setStatementValues(pstmt);
            pstmt.executeUpdate();
            jlblStatus.setText("Record inserted successfully.");
            jlblStatus.setForeground(Color.BLUE);

        } catch (SQLException ex) {
            jlblStatus.setText("Insert failed: " + ex.getMessage());
            jlblStatus.setForeground(Color.RED);
        }
    }

    /** Updates an existing record for the specified ID. */
    private void update() {
        String id = jtfID.getText().trim();
        if (id.isEmpty()) {
            jlblStatus.setText("Please enter an ID to update.");
            return;
        }

        String sql = "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, " +
                     "city=?, state=?, telephone=?, email=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jtfLastName.getText().trim());
            pstmt.setString(2, jtfFirstName.getText().trim());
            pstmt.setString(3, jtfMI.getText().trim());
            pstmt.setString(4, jtfAddress.getText().trim());
            pstmt.setString(5, jtfCity.getText().trim());
            pstmt.setString(6, jtfState.getText().trim());
            pstmt.setString(7, jtfTelephone.getText().trim());
            pstmt.setString(8, jtfEmail.getText().trim());
            pstmt.setString(9, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                jlblStatus.setText("Record updated successfully.");
                jlblStatus.setForeground(Color.BLUE);
            } else {
                jlblStatus.setText("No record found with ID: " + id);
                jlblStatus.setForeground(Color.RED);
            }

        } catch (SQLException ex) {
            jlblStatus.setText("Update failed: " + ex.getMessage());
            jlblStatus.setForeground(Color.RED);
        }
    }

    /** Helper to set all values for INSERT. */
    private void setStatementValues(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, jtfID.getText().trim());
        pstmt.setString(2, jtfLastName.getText().trim());
        pstmt.setString(3, jtfFirstName.getText().trim());
        pstmt.setString(4, jtfMI.getText().trim());
        pstmt.setString(5, jtfAddress.getText().trim());
        pstmt.setString(6, jtfCity.getText().trim());
        pstmt.setString(7, jtfState.getText().trim());
        pstmt.setString(8, jtfTelephone.getText().trim());
        pstmt.setString(9, jtfEmail.getText().trim());
    }

    private void clearFieldsExceptID() {
        jtfLastName.setText("");
        jtfFirstName.setText("");
        jtfMI.setText("");
        jtfAddress.setText("");
        jtfCity.setText("");
        jtfState.setText("");
        jtfTelephone.setText("");
        jtfEmail.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StaffDatabaseApp frame = new StaffDatabaseApp();
            frame.setVisible(true);
        });
    }
}