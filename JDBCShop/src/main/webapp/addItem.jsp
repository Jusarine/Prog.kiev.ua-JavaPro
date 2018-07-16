<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online store</title>
</head>
<body>
    <% String username = (String) session.getAttribute("username");
        if (username != null){%>
    <%="<b>Welcome, " + session.getAttribute("username") + "</b>"%>
    <%}%>
    <form action="add" method="GET">
        <br>
        Name: <input type="text" name="name" required><br>
        Description: <input type="text" name="description"><br>
        Price $: <input type="number" min="0" step="0.1" name="price" required><br>
        <input type="submit" name="add_button" value="Add item">
    </form>
    <a href=# onclick=history.back();>Back</a><br>
    <a href="">Starter page</a>
</body>
</html>
