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
        Item id: <input type="number" min="1" name="item_id" required><br>
        Amount: <input type="number" min="1" name="amount" required><br>
        Delivery: <br>
        <input type="radio" name="delivery" value="MailDelivery" checked>MailDelivery<br>
        <input type="radio" name="delivery" value="CourierDelivery">CourierDelivery<br>
        <input type="radio" name="delivery" value="Pickup">Pickup<br>
        Payment: <br>
        <input type="radio" name="payment" value="CreditCardPayment" checked>CreditCardPayment<br>
        <input type="radio" name="payment" value="CashlessPayment">CashlessPayment<br>
        <input type="radio" name="payment" value="CashOnDelivery">CashOnDelivery<br>
        <input type="submit" name="add_button" value="Make an order">
    </form>
    <a href=# onclick=history.back();>Back</a><br>
    <a href="">Starter page</a>
</body>
</html>
