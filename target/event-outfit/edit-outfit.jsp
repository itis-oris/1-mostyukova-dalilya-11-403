<%@ page import="styleforevent.model.Outfit" %>
<%@ page import="styleforevent.model.OutfitImage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Outfit outfit = (Outfit) request.getAttribute("outfit");
    String error = (String) request.getAttribute("error");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Редактировать образ - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="form-body">
<div class="form-container">
    <h1>Редактировать образ</h1>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <form action="<%= contextPath %>/outfits?action=edit&id=<%= outfit.getId() %>" method="post">
        <div class="form-group">
            <label for="name">Название образа:</label>
            <input type="text" id="name" name="name" value="<%= outfit.getName() %>" required>
        </div>

        <div class="form-group">
            <label for="description">Описание:</label>
            <textarea id="description" name="description"><%= outfit.getDescription() != null ? outfit.getDescription() : "" %></textarea>
        </div>

        <div class="form-group">
            <label>Текущие фотографии:</label>
            <div class="current-images">
                <% if (outfit.getImages() != null && !outfit.getImages().isEmpty()) { %>
                <% for (OutfitImage image : outfit.getImages()) { %>
                <div class="image-preview">
                    <img src="<%= image.getImageUrl() %>" alt="Текущее фото">
                    <button type="button" class="remove-image"
                            onclick="removeImage(<%= image.getId() %>, this)">×</button>
                </div>
                <% } %>
                <% } else { %>
                <p style="color: #666;">Нет фотографий</p>
                <% } %>
            </div>
        </div>

        <div class="form-group">
            <label>Добавить новые фотографии:</label>
            <div id="image-urls-container">
                <div class="image-url-group">
                    <input type="url" name="image_urls" placeholder="https://example.com/new-image.jpg"
                           class="image-url-input">
                    <button type="button" class="remove-image-btn" style="display: none;">Удалить</button>
                </div>
            </div>
            <button type="button" id="add-image-btn" class="btn btn-secondary">Добавить еще фото</button>
        </div>

        <div class="form-group">
            <label for="event_id">Мероприятие:</label>
            <select id="event_id" name="event_id" required>
                <option value="1" <%= outfit.getEventId() == 1 ? "selected" : "" %>>Свадьба</option>
                <option value="2" <%= outfit.getEventId() == 2 ? "selected" : "" %>>Выпускной</option>
                <option value="3" <%= outfit.getEventId() == 3 ? "selected" : "" %>>Деловая встреча</option>
                <option value="4" <%= outfit.getEventId() == 4 ? "selected" : "" %>>Свидание</option>
                <option value="5" <%= outfit.getEventId() == 5 ? "selected" : "" %>>Вечеринка</option>
                <option value="6" <%= outfit.getEventId() == 6 ? "selected" : "" %>>Прогулка</option>
                <option value="7" <%= outfit.getEventId() == 7 ? "selected" : "" %>>Спорт</option>
            </select>
        </div>

        <div class="form-group">
            <label>Для кого:</label>
            <div>
                <input type="radio" id="gender_female" name="gender" value="FEMALE"
                    <%= "FEMALE".equals(outfit.getGender()) ? "checked" : "" %> required>
                <label for="gender_female" style="display: inline; margin-right: 20px;">Женский</label>

                <input type="radio" id="gender_male" name="gender" value="MALE"
                    <%= "MALE".equals(outfit.getGender()) ? "checked" : "" %> required>
                <label for="gender_male" style="display: inline;">Мужской</label>
            </div>
        </div>

        <div style="display: flex; gap: 10px; margin-top: 30px;">
            <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            <a href="<%= contextPath %>/outfits" class="btn btn-secondary">Отмена</a>
            <a href="<%= contextPath %>/outfits?action=delete&id=<%= outfit.getId() %>"
               class="btn btn-danger" onclick="return confirm('Удалить этот образ?')">Удалить образ</a>
        </div>
    </form>
</div>

<script>
    function removeImage(imageId, button) {
        if (!confirm('Удалить эту фотографию?')) {
            return;
        }

        fetch('<%= contextPath %>/outfits?action=deleteImage&image_id=' + imageId, {
            method: 'POST'
        })
            .then(response => response.text())
            .then(result => {
                if (result === 'SUCCESS') {
                    alert('Фотография удалена');
                    button.closest('.image-preview').remove();
                } else {
                    alert('Ошибка при удалении фотографии');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка при удалении фотографии');
            });
    }

    document.getElementById('add-image-btn').addEventListener('click', function() {
        const container = document.getElementById('image-urls-container');
        const newGroup = document.createElement('div');
        newGroup.className = 'image-url-group';
        newGroup.innerHTML = `
                <input type="url" name="image_urls" placeholder="https://example.com/image.jpg"
                       class="image-url-input">
                <button type="button" class="remove-image-btn">Удалить</button>
            `;
        container.appendChild(newGroup);

        updateRemoveButtons();
    });

    function updateRemoveButtons() {
        const groups = document.querySelectorAll('.image-url-group');
        groups.forEach((group, index) => {
            const removeBtn = group.querySelector('.remove-image-btn');
            if (groups.length > 1) {
                removeBtn.style.display = 'inline-block';
            } else {
                removeBtn.style.display = 'none';
            }

            removeBtn.onclick = function() {
                if (groups.length > 1) {
                    group.remove();
                    updateRemoveButtons();
                }
            };
        });
    }

    updateRemoveButtons();
</script>
</body>
</html>