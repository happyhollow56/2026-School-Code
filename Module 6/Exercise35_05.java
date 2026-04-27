import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;

/**
 * DBConnectionPanel - Panel for database connection parameters
 */
class DBConnectionPanel extends JPanel {
    private JComboBox<String> jcboDriver;
    private JComboBox<String> jcboURL;
    private JTextField jtfUsername;
    private JPasswordField jtfPassword;
    private JButton jbtConnect;
    
    private Connection connection;
    
    public DBConnectionPanel() {
        // Initialize driver options
        String[] drivers = {
            "com.mysql.cj.jdbc.Driver",
            "oracle.jdbc.driver.OracleDriver",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "org.apache.derby.jdbc.EmbeddedDriver",
            "org.sqlite.JDBC"
        };
        
        String[] urls = {
            "jdbc:mysql://localhost/javabook",
            "jdbc:oracle:thin:@localhost:1521:orcl",
            "jdbc:sqlserver://localhost:1433;databaseName=javabook",
            "jdbc:derby:javabook",
            "jdbc:sqlite:javabook.db"
        };
        
        jcboDriver = new JComboBox<>(drivers);
        jcboURL = new JComboBox<>(urls);
        jcboURL.setEditable(true);
        jcboDriver.setEditable(true);
        
        jtfUsername = new JTextField(15);
        jtfPassword = new JPasswordField(15);
        jbtConnect = new JButton("Connect to Database");
        
        // Layout
        setLayout(new GridLayout(5, 2, 5, 5));
        setBorder(new TitledBorder("Enter database information"));
        
        add(new JLabel("JDBC Driver"));
        add(jcboDriver);
        add(new JLabel("Database URL"));
        add(jcboURL);
        add(new JLabel("Username"));
        add(jtfUsername);
        add(new JLabel("Password"));
        add(jtfPassword);
        add(new JLabel(""));
        add(jbtConnect);
        
        jbtConnect.addActionListener(e -> connect());
    }
    
    private void connect() {
        try {
            Class.forName(jcboDriver.getSelectedItem().toString().trim());
            String url = jcboURL.getSelectedItem().toString().trim();
            String username = jtfUsername.getText().trim();
            String password = new String(jtfPassword.getPassword());
            
            connection = DriverManager.getConnection(url, username, password);
            JOptionPane.showMessageDialog(this, "Connected to database");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            connection = null;
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public JButton getConnectButton() {
        return jbtConnect;
    }
}

/**
 * Compares batch vs non-batch insert performance
 */
public class Exercise35_05 extends JFrame {
    private Connection connection;
    private DBConnectionPanel connectionPanel;
    private JTextArea jtaResult;
    private JButton jbtConnectDialog;
    private JButton jbtNonBatch;
    private JButton jbtBatch;
    private static final int RECORD_COUNT = 1000;
    
    public Exercise35_05() {
        setTitle("Batch Update Performance Comparison");
        setLayout(new BorderLayout(5, 5));
        
        // Top panel with connection button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jbtConnectDialog = new JButton("Connect to Database");
        topPanel.add(jbtConnectDialog);
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with results
        jtaResult = new JTextArea(15, 50);
        jtaResult.setEditable(false);
        jtaResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(jtaResult), BorderLayout.CENTER);
        
        // Bottom panel with test buttons
        JPanel bottomPanel = new JPanel();
        jbtNonBatch = new JButton("Non-Batch Insert");
        jbtBatch = new JButton("Batch Insert");
        jbtNonBatch.setEnabled(false);
        jbtBatch.setEnabled(false);
        bottomPanel.add(jbtNonBatch);
        bottomPanel.add(jbtBatch);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event handlers
        jbtConnectDialog.addActionListener(e -> showConnectionDialog());
        jbtNonBatch.addActionListener(e -> runNonBatchTest());
        jbtBatch.addActionListener(e -> runBatchTest());
        
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void showConnectionDialog() {
        JDialog dialog = new JDialog(this, "Database Connection", true);
        connectionPanel = new DBConnectionPanel();
        
        // Override connect button to also close dialog on success
        connectionPanel.getConnectButton().addActionListener(e -> {
            if (connectionPanel.getConnection() != null) {
                connection = connectionPanel.getConnection();
                jbtNonBatch.setEnabled(true);
                jbtBatch.setEnabled(true);
                jtaResult.append("Database connected successfully.\n\n");
                dialog.dispose();
            }
        });
        
        dialog.add(connectionPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            stmt.executeUpdate("DROP TABLE Temp");
        } catch (SQLException ex) {
            // Table might not exist, ignore
        }
        stmt.executeUpdate("CREATE TABLE Temp (num1 DOUBLE, num2 DOUBLE, num3 DOUBLE)");
        stmt.close();
    }
    
    private void runNonBatchTest() {
        try {
            createTable();
            jtaResult.append("Non-Batch Insert Test (" + RECORD_COUNT + " records):\n");
            
            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < RECORD_COUNT; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.executeUpdate();
            }
            
            long endTime = System.currentTimeMillis();
            pstmt.close();
            
            jtaResult.append("  Time taken: " + (endTime - startTime) + " ms\n");
            jtaResult.append("  Records inserted: " + RECORD_COUNT + "\n\n");
            
        } catch (SQLException ex) {
            jtaResult.append("Error: " + ex.getMessage() + "\n\n");
        }
    }
    
    private void runBatchTest() {
        try {
            createTable();
            jtaResult.append("Batch Insert Test (" + RECORD_COUNT + " records):\n");
            
            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < RECORD_COUNT; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            
            long endTime = System.currentTimeMillis();
            pstmt.close();
            
            jtaResult.append("  Time taken: " + (endTime - startTime) + " ms\n");
            jtaResult.append("  Records inserted: " + RECORD_COUNT + "\n\n");
            
        } catch (SQLException ex) {
            jtaResult.append("Error: " + ex.getMessage() + "\n\n");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Exercise35_05().setVisible(true);
        });
    }
}