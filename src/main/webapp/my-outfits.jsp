<%@ page import="styleforevent.model.Outfit" %>
<%@ page import="styleforevent.model.OutfitImage" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Outfit> outfits = (List<Outfit>) request.getAttribute("outfits");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Мои образы - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Мои образы</h1>
    <div class="nav-buttons">
        <a href="<%= contextPath %>/outfits?action=create" class="btn btn-primary">Создать образ</a>
        <a href="<%= contextPath %>/home" class="btn btn-secondary">Главная</a>
        <a href="<%= contextPath %>/favorites" class="btn btn-secondary">Избранное</a>
        <a href="<%= contextPath %>/logout" class="btn btn-secondary">Выйти</a>
    </div>
</header>

<% if (outfits == null || outfits.isEmpty()) { %>
<div class="empty-state">
    <h2>У вас пока нет образов</h2>
    <p>Создайте свой первый образ для мероприятия!</p>
    <a href="<%= contextPath %>/outfits?action=create" class="btn btn-primary" style="font-size: 16px; padding: 12px 24px;">
        Создать первый образ
    </a>
</div>
<% } else { %>
<h2>Ваши образы (<%= outfits.size() %>)</h2>
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

        <p style="color: #666; margin-bottom: 15px;">
            <strong>Для:</strong> <%= "FEMALE".equals(outfit.getGender()) ? "Женский" : "Мужской" %>
        </p>

        <div class="outfit-actions">
            <a href="<%= contextPath %>/view-outfit?id=<%= outfit.getId() %>"
               class="btn btn-look">Посмотреть</a>

            <a href="<%= contextPath %>/outfits?action=edit&id=<%= outfit.getId() %>"
               class="btn btn-edit">Редактировать</a>
            <a href="<%= contextPath %>/outfits?action=delete&id=<%= outfit.getId() %>"
               class="btn btn-danger"
               onclick="return confirm('Удалить образ \"<%= outfit.getName() %>\"?')">Удалить</a>
        </div>
    </div>
    <% } %>
</div>
<% } %>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const images = document.querySelectorAll('.outfit-image');
        images.forEach(img => {
            img.onerror = function() {
                this.style.display = 'none';
                const placeholder = this.nextElementSibling;
                if (placeholder && placeholder.classList.contains('image-placeholder')) {
                    placeholder.style.display = 'flex';
                }
            };
        });
    });
</script>
</body>
</html>