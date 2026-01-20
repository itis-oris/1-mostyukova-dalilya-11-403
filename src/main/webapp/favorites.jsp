<%@ page import="styleforevent.model.Outfit" %>
<%@ page import="styleforevent.model.OutfitImage" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Outfit> favorites = (List<Outfit>) request.getAttribute("favorites");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои избранные образы - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Мои избранные образы</h1>
    <div class="nav-buttons">
        <a href="<%= contextPath %>/home" class="btn btn-primary">Главная</a>
        <a href="<%= contextPath %>/outfits" class="btn btn-secondary">Мои образы</a>
        <a href="<%= contextPath %>/logout" class="btn btn-secondary">Выйти</a>
    </div>
</header>

<% if (favorites == null || favorites.isEmpty()) { %>
<div class="empty-state">
    <h2>У вас пока нет избранных образов</h2>
    <p>Добавляйте понравившиеся образы в избранное, чтобы вернуться к ним позже!</p>
    <a href="<%= contextPath %>/home" class="btn btn-primary">Перейти к каталогу</a>
</div>
<% } else { %>
<h2>Ваши избранные образы (<%= favorites.size() %>)</h2>
<div class="favorites-list">
    <% for (Outfit outfit : favorites) { %>
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
        <p><strong>Для:</strong> <%= "FEMALE".equals(outfit.getGender()) ? "Женский" : "Мужской" %></p>

        <div class="outfit-actions">
            <a href="<%= contextPath %>/view-outfit?id=<%= outfit.getId() %>"
               class="btn btn-look">Посмотреть</a>

            <a href="<%= contextPath %>/outfit-catalog?event_id=<%= outfit.getEventId() %>&gender=<%= outfit.getGender() %>"
               class="btn btn-same">Похожие</a>
            <button class="btn btn-danger" onclick="removeFromFavorites(<%= outfit.getId() %>, this)">
                Удалить
            </button>
        </div>
    </div>
    <% } %>
</div>
<% } %>

<script>
    function removeFromFavorites(outfitId, button) {
        if (!confirm('Удалить этот образ из избранного?')) {
            return;
        }

        fetch('<%= contextPath %>/favorites?action=remove&outfit_id=' + outfitId, {
            method: 'POST'
        })
            .then(response => response.text())
            .then(result => {
                console.log("Remove result:", result);
                if (result === 'SUCCESS') {
                    alert('Удалено из избранного');
                    button.closest('.outfit-card').remove();

                    const cards = document.querySelectorAll('.outfit-card');
                    if (cards.length === 0) {
                        location.reload();
                    }
                } else {
                    alert('Ошибка при удалении из избранного');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка при удалении из избранного');
            });
    }
</script>
</body>
</html>