
package src;

import com.sun.rowset.JdbcRowSetImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author DragonDream
 */
public class DBConnection {
    // JDBC API objects
    static Connection connection;  //the database connection object
    static ResultSet resultSet;
    static Statement statement; //the statement object
    
    static JdbcRowSetImpl rowSet;
    
    
    public static void doConnection() {

        rowSet = new JdbcRowSetImpl();

        try {

            rowSet.setUrl("jdbc:mysql://localhost:3306/creditcard_java");
            rowSet.setUsername("root");
            rowSet.setPassword("root");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Creating Connection " + ex);
            System.exit(0);
        }

    } // end doConnection

    public static void closeConnection() {
        try {
            rowSet.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error Closing Connection " + ex);
            System.exit(0);
        }
    } // end closeConnection
    
    public static JdbcRowSetImpl getAllRecords() {

        try {
            rowSet.setCommand("SELECT * FROM cards");
            rowSet.execute();
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error Creating Connection " + ex);
            System.exit(0);
        }
        
        return rowSet;
        
    } // end getAllRecords
}
