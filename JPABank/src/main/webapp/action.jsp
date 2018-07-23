<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bank</title>
</head>
<body>
    <%String username = (String) session.getAttribute("username");
        if (username != null){%>
        <%="<b>Welcome, " + session.getAttribute("username") + " </b>"%>
    <%}%>
    <form action="get" method="GET">
        <br>
        <button name="button" value="accounts">View accounts</button>
        <button name="button" value="transactions">View transactions</button>
        <button name="button" value="money">View all money</button>
    </form>

    <form action="add" method="GET">
        Currency:
        <br>
        <input type="radio" name="currency" value="USD" checked>USD<br>
        <input type="radio" name="currency" value="EUR">EUR<br>
        <input type="radio" name="currency" value="UAH">UAH<br>
        <input type="submit" name="button" value="Add account">
    </form>
    <form action="add" method="GET">
        Account id: <input type="number" min="1" name="account_id" required><br>
        Currency:
        <br>
        <input type="radio" name="currency" value="USD" checked>USD<br>
        <input type="radio" name="currency" value="EUR">EUR<br>
        <input type="radio" name="currency" value="UAH">UAH<br>
        <input type="submit" name="button" value="Change account currency">
    </form>
    <form action="add" method="GET">
        Account id: <input type="number" min="1" name="account_id" required><br>
        Money: <input type="number" min="0" step="0.1" name="money" required><br>
        Currency:
        <br>
        <input type="radio" name="currency" value="USD" checked>USD<br>
        <input type="radio" name="currency" value="EUR">EUR<br>
        <input type="radio" name="currency" value="UAH">UAH<br>
        <input type="submit" name="button" value="Replenish account">
    </form>
    <form action="add" method="GET">
        Sender id: <input type="number" min="1" name="sender_id" required><br>
        Receiver id: <input type="number" min="1" name="receiver_id" required><br>
        Money: <input type="number" min="0" step="0.1" name="money" required><br>
        Currency:
        <br>
        <input type="radio" name="currency" value="USD" checked>USD<br>
        <input type="radio" name="currency" value="EUR">EUR<br>
        <input type="radio" name="currency" value="UAH">UAH<br>
        <input type="submit" name="button" value="Perform transaction">
    </form>


    <br>
    <a href=# onclick=history.back();>Back</a><br>
    <a href="/">Starter page</a>
</body>
</html>
