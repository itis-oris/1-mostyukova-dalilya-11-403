<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-register-body">
<div class="login-register-form">
    <h1>Event Outfit</h1>
    <h2>Вход в систему</h2>

    <% if (request.getParameter("error") != null) { %>
    <div class="error"><%= request.getParameter("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/auth/login" method="post">        <label for="username">Имя пользователя:</label>
        <input type="text" id="username" name="username" required>


<div class="form-group">
    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required>
</div>

<button type="submit">Войти</button>
</form>

<p style="text-align: center; margin-top: 20px;">
    Нет аккаунта? <a href="/auth/register">Зарегистрируйтесь</a>
</p>
</div>
</body>
</html>