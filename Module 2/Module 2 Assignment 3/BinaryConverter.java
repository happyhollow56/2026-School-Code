/** Utility class for binary to decimal conversion */
public class BinaryConverter {
    
    /**
     * Converts a binary string to decimal integer
     * @param binary String containing binary digits (0s and 1s)
     * @return decimal integer value
     * @throws BinaryFormatException if string contains non-binary characters
     */
    public static int bin2Dec(String binary) throws BinaryFormatException {
        // Check if string is null or empty
        if (binary == null || binary.isEmpty()) {
            throw new BinaryFormatException("Binary string cannot be null or empty");
        }
        
        // Validate binary format
        for (int i = 0; i < binary.length(); i++) {
            char c = binary.charAt(i);
            if (c != '0' && c != '1') {
                throw new BinaryFormatException(
                    "Invalid character '" + c + "' at index " + i + ". Binary string must contain only 0s and 1s."
                );
            }
        }
        
        // Convert binary to decimal
        int decimal = 0;
        for (int i = 0; i < binary.length(); i++) {
            char bit = binary.charAt(i);
            decimal = decimal * 2 + (bit - '0');
        }
        
        return decimal;
    }
}