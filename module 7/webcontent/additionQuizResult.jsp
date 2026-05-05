<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Addition Quiz Result</title>
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
            width: 500px;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .correct {
            color: green;
            font-weight: bold;
        }
        .incorrect {
            color: red;
            font-weight: bold;
        }
        .score {
            margin-top: 20px;
            font-size: 20px;
            font-weight: bold;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h2>Addition Quiz Result</h2>
    <table>
        <tr>
            <th>#</th>
            <th>Question</th>
            <th>Your Answer</th>
            <th>Correct Answer</th>
            <th>Status</th>
        </tr>
        <%
            int correctCount = 0;
            int totalQuestions = 10;
            
            for (int i = 0; i < totalQuestions; i++) {
                // Retrieve question data
                int num1 = Integer.parseInt(request.getParameter("num1_" + i));
                int num2 = Integer.parseInt(request.getParameter("num2_" + i));
                int correctAnswer = Integer.parseInt(request.getParameter("correct" + i));
                
                // Retrieve user's answer
                String userAnswerStr = request.getParameter("answer" + i);
                int userAnswer = 0;
                boolean isValid = true;
                
                try {
                    userAnswer = Integer.parseInt(userAnswerStr);
                } catch (NumberFormatException e) {
                    isValid = false;
                }
                
                boolean isCorrect = isValid && (userAnswer == correctAnswer);
                if (isCorrect) {
                    correctCount++;
                }
        %>
        <tr>
            <td><%= i + 1 %></td>
            <td><%= num1 %> + <%= num2 %></td>
            <td><%= userAnswerStr %></td>
            <td><%= correctAnswer %></td>
            <td class="<%= isCorrect ? "correct" : "incorrect" %>">
                <%= isCorrect ? "Correct" : "Incorrect" %>
            </td>
        </tr>
        <%
            }
            
            double percentage = (correctCount * 100.0) / totalQuestions;
        %>
    </table>
    
    <div class="score">
        Total Correct: <%= correctCount %> out of <%= totalQuestions %><br>
        Score: <%= String.format("%.1f", percentage) %>%
    </div>
    
    <a href="additionQuiz.jsp">Take Another Quiz</a>
</body>
</html>
