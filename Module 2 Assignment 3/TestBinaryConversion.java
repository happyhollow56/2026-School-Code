import java.util.Scanner;

public class TestBinaryConversion {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Enter a binary string: ");
        String binaryString = input.nextLine();
        
        try {
            int decimalValue = BinaryConverter.bin2Dec(binaryString);
            System.out.println("The decimal value for binary \"" + binaryString + "\" is: " + decimalValue);
        } catch (BinaryFormatException ex) {
            System.out.println("BinaryFormatException: " + ex.getMessage());
        }
        
        input.close();
    }
}