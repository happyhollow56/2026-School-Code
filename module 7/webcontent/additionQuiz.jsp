<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Addition Quiz</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h2 {
            color: #333;
        }
        table {
            border-collapse: collapse;
            margin-top: 20px;
        }
        td {
            padding: 10px;
            font-size: 18px;
        }
        .question {
            font-family: "Courier New", monospace;
            font-size: 20px;
        }
        input[type="text"] {
            width: 60px;
            padding: 5px;
            font-size: 16px;
            text-align: center;
        }
        input[type="submit"] {
            margin-top: 20px;
            padding: 10px 25px;
            font-size: 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h2>Addition Quiz</h2>
    <form action="additionQuizResult.jsp" method="post">
        <table>
            <%
                // Generate 10 random addition questions
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    int num1 = random.nextInt(100); // 0-99
                    int num2 = random.nextInt(100); // 0-99
                    int answer = num1 + num2;
            %>
            <tr>
                <td class="question">
                    <%= num1 %> + <%= num2 %> = 
                </td>
                <td>
                    <input type="text" name="answer<%= i %>" required>
                    <input type="hidden" name="num1_<%= i %>" value="<%= num1 %>">
                    <input type="hidden" name="num2_<%= i %>" value="<%= num2 %>">
                    <input type="hidden" name="correct<%= i %>" value="<%= answer %>">
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
