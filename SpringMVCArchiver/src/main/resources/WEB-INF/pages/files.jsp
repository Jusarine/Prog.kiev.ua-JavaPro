<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Archiver</title>
</head>
<body>
<div align="center">

    <form action="archive" method="POST">
        <table border="1">
            <tr>
                <th>select</th>
                <th>file_name</th>
            </tr>
            <c:forEach items="${file_names}" var="name">
                <tr>
                    <td><input type="checkbox" name="checked_files" value="${name}"></td>
                    <td><c:out value="${name}"/></td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <input type="text" name="archive_name" placeholder="archive name" pattern="[A-Za-z0-9]*" required> .zip
        <input type="submit" value="Archive"/>
    </form>

    <a href="/">starter page</a>
</div>
</body>
</html>