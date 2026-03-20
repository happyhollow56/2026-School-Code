
public class FeetMetersConversion {
        //Conversion FOR FEET TO METERS
    public static double footToMeter(double foot) {
        return 0.305 * foot;
    }
    //Conversion FOR METERS TO FEET
    public static double meterToFoot(double meter) {
        return 3.279 * meter;
    }
    
    public static void main(String[] args) {
        // display table headers
        System.out.println("Feet      Meters    |    Meters     Feet");
        System.out.println("-----------------------------------------");
        /// convert and display the tables
        double feet = 1.0;
        double meters = 20.0;
        
        for (int i = 0; i < 10; i++) {
            // feet to meters conversion (for left column)
            double metersConverted = footToMeter(feet);
            // meters to feet conversion (for right column)
            double feetConverted = meterToFoot(meters);
            // print the conversion results in formatted columns
            System.out.printf("%-9.1f %-9.3f |    %-9.1f  %.3f%n", 
                feet, metersConverted, meters, feetConverted);
            
            feet++;
            meters += 5;
        }
    }
}