<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Questionnaire</title>
</head>
<body>
    <form action="/signup" method="POST">
        FirstName: <input type="text" name="firstName"><br>
        LastName: <input type="text" name="lastName"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" name="button" value="Sign up"><br>
        <br>
    </form>
    <form action="/signin" method="POST">
        FirstName: <input type="text" name="firstName"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" name="button" value="Sign in"><br>
        <br>
    </form>
    <form action="/signup" method="POST">
        <input type="submit" name="button" value="Send anonymous questionnaire">
    </form>
    <a href="/statistic">Statistic</a>
</body>
</html>
