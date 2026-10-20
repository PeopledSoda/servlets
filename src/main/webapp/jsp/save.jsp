<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add users</title>
</head>
<body>
<h1>Add users</h1>
<form action="${pageContext.request.contextPath}/save" method="post">
    <c:forEach begin="0" end="${usersToAdd - 1}" var="i">
        <fieldset>
            <legend>User ${i + 1}</legend>
            <input name="name_${i}" placeholder="Name" required>
            <input name="surname_${i}" placeholder="Surname" required>
            <input name="age_${i}" type="number" min="0" placeholder="Age" required>
            <input name="email_${i}" type="email" placeholder="Email" required>
            <input name="city_${i}" placeholder="City" required>
            <input name="phone_${i}" placeholder="Phone" required>
        </fieldset>
    </c:forEach>
    <input type="submit" value="Save users">
</form>
</body>
</html>
