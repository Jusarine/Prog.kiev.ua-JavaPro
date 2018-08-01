<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Delete Group</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <form role="form" class="form-horizontal" action="/group/delete" method="post">
                <h3>Delete Group</h3>
                <select class="selectpicker form-control form-group" name="group">
                    <c:forEach items="${groups}" var="group">
                        <option value="${group.id}">${group.name}</option>
                    </c:forEach>
                </select>
                <input type="submit" class="btn btn-primary" value="Delete">
            </form>
        </div>

        <script>
            $('.selectpicker').selectpicker();
        </script>
    </body>
</html>