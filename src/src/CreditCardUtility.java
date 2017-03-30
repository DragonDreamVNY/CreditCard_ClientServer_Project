/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author DragonDream
 * Networking - credit card validation
 */
public class CreditCardUtility {
    
    private static boolean isValidNumber(String number) { 
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1)); if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1; 
                }
            }
                sum += n;
                alternate = !alternate;
        }
        return (sum % 10 == 0); 
    }//end is valid number method
    
    
    /* Visa (all valid visa cards must begin with the digit 4 and must have either a total of 13 or 16 digits) */
    public static boolean checkVisa(String cardNum) {

        return ( (cardNum.startsWith("4")) && (isValidNumber(cardNum)) && ( (cardNum.length() == 13) || (cardNum.length() == 16)) );

    } //end check VISA
    
    
    /* MasterCard (all valid MasterCards must begin with the digits 51 or 55, AND must have either a total of 13 digits) */
    public static boolean checkMastercard(String cardNum) {

        return ( (cardNum.startsWith("51")) || (cardNum.startsWith("53")) || (cardNum.startsWith("55")) ) && ( (isValidNumber(cardNum)) && cardNum.length() == 13 );

    } //end check MASTERCARD
    
    /* Laser (all valid laser cards must begin with the digits 6304 or 6706 or 6771 or 6709 AND is a Valid Number AND must have either a total of 16 or 19 digits) */
    public static boolean checkLaser(String cardNum) {
        
        return ( (cardNum.startsWith("6304")) || (cardNum.startsWith("6706")) || (cardNum.startsWith("6771")) ) &&  (isValidNumber(cardNum)) &&  ((cardNum.length() == 16) || (cardNum.length() == 19) ) ;

    } //end check MASTERCARD
    
}//end Credit Card utility
