<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Questionnaire</title>
  </head>
  <body>
    <form action="/db" method="POST">
        FirstName: <input type="text" name="firstName"><br>
        LastName: <input type="text" name="lastName"><br>
        How old are you?<br>
            <input type="radio" name="age" value="16-20">16-20<br>
            <input type="radio" name="age" value="21-25">21-25<br>
            <input type="radio" name="age" value="26+">26+<br>
        What is your position?<br>
            <input type="radio" name="position" value="Junior">Junior<br>
            <input type="radio" name="position" value="Middle">Middle<br>
            <input type="radio" name="position" value="Senior">Senior<br>
        <input type="submit" value="Send">
    </form>
  </body>
</html>
