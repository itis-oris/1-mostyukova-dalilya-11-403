<%@ page import="styleforevent.model.Outfit" %>
<%@ page import="styleforevent.model.OutfitImage" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Outfit> outfits = (List<Outfit>) request.getAttribute("outfits");
    String eventId = (String) request.getAttribute("eventId");
    String gender = (String) request.getAttribute("gender");
    String genderDisplay = (String) request.getAttribute("genderDisplay");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Каталог образов - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Каталог образов</h1>
    <div>
        <a href="<%= contextPath %>/favorites" class="btn btn-primary">Избранное</a>
        <a href="<%= contextPath %>/home" class="btn btn-primary">Назад к мероприятиям</a>
        <a href="<%= contextPath %>/outfits" class="btn btn-primary">Мои образы</a>
        <a href="<%= contextPath %>/logout" class="btn btn-primary">Выйти</a>
    </div>
</header>

<h2><%= genderDisplay %> образы</h2>

<% if (outfits == null || outfits.isEmpty()) { %>
<div class="empty-state">
    <h2>Пока нет образов для этого мероприятия</h2>
    <p>Будьте первым, кто создаст образ для этого события!</p>
    <a href="<%= contextPath %>/outfits?action=create" class="btn btn-primary">Создать образ</a>
</div>
<% } else { %>
<p>Найдено <%= outfits.size() %> образов</p>
<div class="outfit-list">
    <% for (Outfit outfit : outfits) { %>
    <div class="outfit-card">
        <div class="outfit-images">
            <% if (outfit.getImages() != null && !outfit.getImages().isEmpty()) { %>
            <img src="<%= outfit.getImages().get(0).getImageUrl() %>" alt="<%= outfit.getName() %>"
                 class="outfit-image"
                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
            <div class="image-placeholder" style="display: none;">
                Не удалось загрузить изображение
            </div>
            <% } else { %>
            <div class="image-placeholder">
                Нет фотографий
            </div>
            <% } %>
        </div>

        <h3><%= outfit.getName() %></h3>
        <p><%= outfit.getDescription() != null ? outfit.getDescription() : "" %></p>

        <div class="outfit-actions">
            <a href="<%= contextPath %>/view-outfit?id=<%= outfit.getId() %>"
               class="btn btn-success">Посмотреть</a>

            <button class="btn btn-primary" onclick="addToFavorites(<%= outfit.getId() %>, this)">В избранное</button>        </div>
    </div>
    <% } %>
</div>
<% } %>

<script>
    function addToFavorites(outfitId, buttonElement) {
        console.log("=== ADD TO FAVORITES DEBUG ===");
        console.log("Outfit ID:", outfitId);

        const button = buttonElement || event.target;
        console.log("Button element:", button);

        const url = '<%= contextPath %>/favorites?action=add&outfit_id=' + outfitId;
        console.log("URL:", url);

        fetch(url, {
            method: 'POST'
        })
            .then(response => {
                console.log("Response status:", response.status);
                console.log("Response ok:", response.ok);
                return response.text();
            })
            .then(result => {
                console.log("Raw result:", result);
                console.log("Result length:", result.length);

                const cleanResult = result.trim();
                console.log("Clean result:", cleanResult);

                if (cleanResult === 'SUCCESS') {
                    console.log("SUCCESS - added to favorites");
                    alert('Успешно добавлено в избранное!');
                    button.textContent = 'В избранном';
                    button.disabled = true;
                    button.style.background = '#28a745';
                } else if (cleanResult === 'ALREADY_FAVORITE') {
                    console.log("ALREADY IN FAVORITES");
                    alert('Этот образ уже в избранном!');
                    button.textContent = 'В избранном';
                    button.disabled = true;
                    button.style.background = '#28a745';
                } else if (cleanResult === 'NOT_LOGGED_IN') {
                    console.log("USER NOT LOGGED IN");
                    alert('Необходимо войти в систему');
                    window.location.href = '<%= contextPath %>/auth/login';
                } else {
                    console.log("UNKNOWN RESPONSE:", cleanResult);
                    alert('Неизвестный ответ от сервера: ' + cleanResult);
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                alert('Ошибка сети: ' + error.message);
            });
    }
</script>
</body>
</html>