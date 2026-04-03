/* +---------------------------+
|  BinaryFormatException    |
|     <<exception>>         |
+---------------------------+
| + BinaryFormatException() |
| + BinaryFormatException(message: String) |
+---------------------------+
           △
           | extends
+---------------------------+
|      Exception            |
+---------------------------+

+---------------------------+
|    BinaryConverter        |
+---------------------------+
| + bin2Dec(binary: String): int |
|   throws BinaryFormatException   |
+---------------------------+ */
public class BinaryFormatException extends Exception {
    
    /** Default constructor */
    public BinaryFormatException() {
        super("Invalid binary string - must contain only 0s and 1s");
    }
    
    /** Constructor with custom message */
    public BinaryFormatException(String message) {
        super(message);
    }
}