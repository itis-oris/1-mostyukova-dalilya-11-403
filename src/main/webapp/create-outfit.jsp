<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Создать образ - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="form-body">
<div class="form-container">
    <h1>Создать новый образ</h1>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <form action="<%= contextPath %>/outfits?action=create" method="post">
        <div class="form-group">
            <label for="name">Название образа:</label>
            <input type="text" id="name" name="name" required placeholder="Например: Вечернее платье для свадьбы">
        </div>

        <div class="form-group">
            <label for="description">Описание:</label>
            <textarea id="description" name="description" placeholder="Опишите ваш образ..."></textarea>
        </div>

        <div class="form-group">
            <label>Фотографии образа (можно добавить несколько):</label>
            <div id="image-urls-container">
                <div class="image-url-group">
                    <input type="url" name="image_urls" placeholder="https://example.com/image1.jpg"
                           class="image-url-input">
                    <button type="button" class="remove-image-btn" style="display: none;">Удалить</button>
                </div>
            </div>
            <button type="button" id="add-image-btn" class="btn btn-secondary">Добавить еще фото</button>
        </div>

        <div class="form-group">
            <label for="event_id">Мероприятие:</label>
            <select id="event_id" name="event_id" required>
                <option value="">Выберите мероприятие</option>
                <option value="1">Свадьба</option>
                <option value="2">Выпускной</option>
                <option value="3">Деловая встреча</option>
                <option value="4">Свидание</option>
                <option value="5">Вечеринка</option>
                <option value="6">Прогулка</option>
                <option value="7">Спорт</option>
            </select>
        </div>

        <div class="form-group">
            <label>Для кого:</label>
            <div>
                <input type="radio" id="gender_female" name="gender" value="FEMALE" required>
                <label for="gender_female" style="display: inline; margin-right: 20px;">Женский</label>

                <input type="radio" id="gender_male" name="gender" value="MALE" required>
                <label for="gender_male" style="display: inline;">Мужской</label>
            </div>
        </div>

        <div style="display: flex; gap: 10px; margin-top: 30px;">
            <button type="submit" class="btn btn-primary">Сохранить образ</button>
            <a href="<%= contextPath %>/outfits" class="btn btn-secondary">Назад к моим образам</a>
        </div>
    </form>
</div>

<script>
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