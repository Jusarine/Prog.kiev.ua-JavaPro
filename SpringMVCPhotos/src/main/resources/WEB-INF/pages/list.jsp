<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
<div align="center">

    <form action="delete_checked_photos" method="POST">
        <table border="1">
            <tr>
                <th>select</th>
                <th>id</th>
                <th>image</th>
            </tr>
            <c:forEach items="${ids}" var="id">
                <tr>
                    <td>
                        <input type="checkbox" name="ids" value="${id}">
                    </td>
                    <td>
                        <a href="/photo/${id}">${id}</a>
                    </td>
                    <td>
                        <img src="/photo/${id}" height="150"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br><input type="submit" value="Delete"/>
    </form>
</div>
</body>
</html>