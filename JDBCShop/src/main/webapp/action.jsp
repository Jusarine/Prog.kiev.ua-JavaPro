<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Online store</title>
</head>
<body>
    <% String username = (String) session.getAttribute("username");
        if (username != null){%>
        <%="<b>Welcome, " + session.getAttribute("username") + " </b>"%>
    <%}%>
    <form action="get" method="GET">
        <br>
        <button name="get_button" value="items">View items</button>
        <button name="get_button" value="orders">View orders</button>
        <button name="get_button" value="users">View users</button>
    </form>
    <form>
        <button formaction="addItem.jsp">Add item</button>
        <button formaction="makeOrder.jsp">Make an order</button>
    </form>
    <br>
    <a href=# onclick=history.back();>Back</a><br>
    <a href="/">Starter page</a>
</body>
</html>
