<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bank</title>
</head>
<body>
    <form action="login" method="POST">
        Name: <input type="text" name="name" required><br>
        Email: <input type="email" name="email"><br>
        PhoneNumber: <input type="text" name="phoneNumber" required><br>
        Password: <input type="password" name="password" required><br>
        <input type="submit" name="button" value="Sign up"><br>
        <br>
    </form>
    <form action="login" method="POST">
        Name: <input type="text" name="name" required><br>
        Password: <input type="password" name="password" required><br>
        <input type="submit" name="button" value="Sign in"><br>
        <br>
    </form>
</body>
</html>
