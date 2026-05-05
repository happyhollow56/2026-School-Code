// ComputeLoan.java - Servlet to handle loan calculation
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ComputeLoan")
public class ComputeLoan extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve input parameters
        double loanAmount = Double.parseDouble(request.getParameter("loanAmount"));
        double annualInterestRate = Double.parseDouble(request.getParameter("annualInterestRate"));
        int numberOfYears = Integer.parseInt(request.getParameter("numberOfYears"));
        
        // Create Loan object and compute payments
        Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
        double monthlyPayment = loan.getMonthlyPayment();
        double totalPayment = loan.getTotalPayment();
        
        // Display results
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Compute Loan Payment</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("table { border-collapse: collapse; width: 400px; }");
        out.println("td { padding: 8px; border: 1px solid #ddd; }");
        out.println("td:first-child { font-weight: bold; background-color: #f2f2f2; width: 200px; }");
        out.println("h2 { color: #333; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Loan Payment Results</h2>");
        out.println("<table>");
        out.println("<tr><td>Loan Amount:</td><td>$" + String.format("%,.2f", loanAmount) + "</td></tr>");
        out.println("<tr><td>Annual Interest Rate:</td><td>" + annualInterestRate + "%</td></tr>");
        out.println("<tr><td>Number of Years:</td><td>" + numberOfYears + "</td></tr>");
        out.println("<tr><td>Monthly Payment:</td><td>$" + String.format("%,.2f", monthlyPayment) + "</td></tr>");
        out.println("<tr><td>Total Payment:</td><td>$" + String.format("%,.2f", totalPayment) + "</td></tr>");
        out.println("</table>");
        out.println("<br><a href='loanForm.html'>Compute Another Loan</a>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}