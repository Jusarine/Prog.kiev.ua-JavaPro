<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Archiver</title>
</head>
<body>
<div align="center">
    <table border="1">
        <tr>
            <th>archive_name</th>
            <th>download</th>
            <th>exact</th>
        </tr>
        <c:forEach items="${archive_names}" var="name">
            <tr>
                <td><c:out value="${name}"/></td>
                <td><input type="submit" value="Download" onclick="window.location='/download_archive/${name}';" /></td>
                <td><input type="submit" value="Exact" onclick="window.location='/extract/${name}';" /></td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <a href="/">starter page</a>
</div>
</body>
</html>