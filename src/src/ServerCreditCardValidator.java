package src;

import com.sun.rowset.JdbcRowSetImpl;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author k00223361 VINCENT LEE - DragonDream Credit Card SERVER receive
 * ObjectStream from CLIENT
 */
/*=========
//Part One
//=========
// receive card object from Client
// run validation
// returns Boolean
// sends Boolean to Client
// Client receives True or False
// True: displays new Credit Card Details
// False: displays that Details are incorrect. 
// further validation on Client side for textField entries needed: 
// 'expiry date: future dates only', 'Card Holder name is in '
// ref: https://www.paypalobjects.com/en_US/vhelp/paypalmanager_help/credit_card_numbers.htm

//=========
// Part Two
//=========
// server has sent response to Client,
// before this, it writes Credit Card details to MySQL database 
// db: 'creditcard_java'
// table : 'creditcards'
// host: 'localhost'
// pw: 'root'
// ***NB*** include MySQLJDBC library driver in Project Settings
// ref: https://www.mkyong.com/jdbc/jdbc-callablestatement-stored-procedure-out-parameter-example/
// ref: JDBC exercise DBLab05 solutions
 */
public class ServerCreditCardValidator {

    public static void main(String[] args) {
        
        System.out.println("Server started and is awaiting connections");
        try {
            //create a ServerSocket on port 8111
            ServerSocket serverSocket = new ServerSocket(8111);

            int clientNo = 1; // The number of a client
           
            while (true) { //keep listening for objects from Clients
                
                //listen to requests on the ServerSocket and accept a connection when one arrives
                Socket connectToClient = serverSocket.accept();

                // Print the new connect number on the console
                System.out.println("Start thread for client " + clientNo);

                // Find the client's host name, and IP address. Not functionally important in exam situation..
                InetAddress clientInetAddress = connectToClient.getInetAddress();
                String clientIP = clientInetAddress.getHostAddress();
                String clientHostName = clientInetAddress.getHostName();

                System.out.println("Client " + clientNo + "'s host name is " + clientHostName);
                System.out.println("Client " + clientNo + "'s IP Address is " + clientIP);

                // Create a new thread for the connection
                HandleAClient thread = new HandleAClient(connectToClient);

                thread.start();// Start the new thread
                clientNo++;// Increment clientNo    
            }//end while         
        } //end Try
        catch (IOException ex) {
            System.out.println("An error has occured: " + ex);
            System.exit(0);
        }//end catch

    }// end main

    public ServerCreditCardValidator() {
    }
}// end Server CreditCardValidator Class

// Define the thread class for handling a new connection
class HandleAClient extends Thread {

    private static String cardNumber;
    private static String expiryDate;
    private static String cardType;
    private static String cardHolderName;
    private static String cvvString;
    private static int cvv;

    private static boolean isValid;

    
    //this stream will be used to WRITE a card object to a FILE
    //private static ObjectOutputStream fileObjectOutputStream;

    private Socket connectToClient; // A connected socket

    /*===================
     * Construct a thread
     ===================*/
    public HandleAClient(Socket socket) {   
        connectToClient = socket;
    }

    /**--------------------------------------
     * Implement the run() method for the thread
     --------------------------------------*/
    public void run() {
        try {
            // Create data input and output streams
            //DataInputStream isFromClient = new DataInputStream(connectToClient.getInputStream());
            //DataOutputStream osToClient = new DataOutputStream(connectToClient.getOutputStream());

            //create a stream to receive Object from Client
            ObjectInputStream clientCardObjectInputStream = new ObjectInputStream(connectToClient.getInputStream());
            
            // stream to write Boolean response to Client from Server
            DataOutputStream validCardOutputStream = new DataOutputStream(connectToClient.getOutputStream());

            // Continuously serve the client
            while (true) {  //Main section that will change in different applications. Rest of server is boilerplate code
                // Receive credit card object from the client
                CreditCard card = (CreditCard) clientCardObjectInputStream.readObject();
                cardNumber = card.getCardNumber(); //read the cardNumber from the Card object received
                cardType = card.getCardType();
                cardHolderName = card.getCardHolder();
                cvv = card.getCvv();

                // Display radius on console
                System.out.println("Card details received from client: "
                        + "\n cardNum: " + cardNumber
                        + "\n type: " + cardType
                        + "\n Name: " + cardHolderName
                        + "\n cvv: " + cvv);

                // VISA?
                if (cardType.equalsIgnoreCase("visa")) {
                    isValid = CreditCardUtility.checkVisa(cardNumber);
                    System.out.println("VISA card");
                } // MASTER CARD?
                else if (cardType.equalsIgnoreCase("mastercard")) {
                    isValid = CreditCardUtility.checkMastercard(cardNumber);
                    System.out.println("MASTER card");
                } else if (cardType.equalsIgnoreCase("laser")) {
                    isValid = CreditCardUtility.checkLaser(cardNumber);
                    System.out.println("LASER card");
                } else {
                    isValid = false;
                    System.out.println("Credit Card Type isn't VISA / MASTERCARD / LASER for : " + cardNumber + "\n is invalid");
                }

                validCardOutputStream.writeBoolean(isValid);//tell the Client is CardNumber Valid?
             
            } // end try
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HandleAClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    } //end run method for thread

} //end HandleAClient Class


