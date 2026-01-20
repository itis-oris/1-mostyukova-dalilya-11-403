<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-register-body">
<div class="login-register-form">
    <h1>Event Outfit</h1>
    <h2>Регистрация</h2>

    <% if (request.getParameter("error") != null) { %>
    <div class="error"><%= request.getParameter("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/auth/register" method="post">        <label for="username">Имя пользователя:</label>
        <input type="text" id="username" name="username" required>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <p style="text-align: center; margin-top: 20px;">
        Уже есть аккаунт? <a href="/auth/login">Войдите</a>
    </p>
</div>
</body>
</html>