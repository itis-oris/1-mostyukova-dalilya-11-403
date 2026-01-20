<%@ page import="styleforevent.model.Event" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Event> events = (List<Event>) request.getAttribute("events");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Event Outfit - Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="home-body">
<header class="header">
    <h1>Event Outfit</h1>
    <div class="nav-buttons">
        <a href="<%= contextPath %>/favorites" class="btn btn-primary">️Избранное</a>
        <a href="<%= contextPath %>/outfits" class="btn btn-primary">Мои образы</a>
        <a href="<%= contextPath %>/logout" class="btn btn-primary">Выйти</a>
    </div>
</header>
<div class="event">
    <div class="title">
        <h2>Выберите мероприятие</h2>
        <p>Подберите идеальный образ для любого события!</p>
    </div>

    <div class="event-list">
        <% for (Event event : events) { %>
        <div class="event-card">
            <h3><%= event.getName() %></h3>
            <p><%= event.getDescription() != null ? event.getDescription() : "" %></p>
            <div style="margin: 15px 0;">
                <a href="<%= contextPath %>/outfit-catalog?event_id=<%= event.getId() %>&gender=FEMALE" class="btn btn-look">Женские</a>
                <a href="<%= contextPath %>/outfit-catalog?event_id=<%= event.getId() %>&gender=MALE" class="btn btn-look">Мужские</a>
            </div>
        </div>
        <% } %>
    </div>
</div>

</body>
</html>