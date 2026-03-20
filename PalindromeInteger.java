/// I couldnt find the 6.31 you had listed so i am just assuming you meant 6.3 here 
/// 
import java.util.Scanner;

public class PalindromeInteger {
    public static int reverse(int number) {
        int result = 0;
        int n = Math.abs(number);
        while (n > 0) {
            result = result * 10 + n % 10;
            n /= 10;
        }
        return number < 0 ? -result : result;
    }

    public static boolean isPalindrome(int number) {
        return number == reverse(number);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int number = scanner.nextInt();
        if (isPalindrome(number)) {
            System.out.println(number + " is a palindrome");
        } else {
            System.out.println(number + " is not a palindrome");
        }
        scanner.close();
    }
}
