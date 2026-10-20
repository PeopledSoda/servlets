<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User query</title>
</head>
<body>
<h1>Поиск пользователя</h1>
<form action="${pageContext.request.contextPath}/user-query" method="get">
    <label for="field">Фильтр:</label>
    <select id="field" name="field">
        <option value="email">Email</option>
        <option value="city">Город</option>
        <option value="phone">Телефон</option>
    </select>
    <label for="value">Значение:</label>
    <input id="value" name="value" required>
    <button type="submit">Выполнить запрос</button>
</form>
</body>
</html>
