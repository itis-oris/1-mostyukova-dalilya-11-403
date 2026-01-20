<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Просмотр образа - ${outfit.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="view-container">
    <a href="javascript:history.back()" class="back-btn">← Назад</a>

    <div class="outfit-header">
        <h1>${outfit.name}</h1>
        <div class="outfit-meta">
            <c:choose>
                <c:when test="${outfit.gender == 'FEMALE'}">Женский образ</c:when>
                <c:when test="${outfit.gender == 'MALE'}">Мужской образ</c:when>
            </c:choose>
        </div>
    </div>

    <c:if test="${not empty outfit.description}">
        <div class="outfit-description">
                ${outfit.description}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty outfit.images}">
            <div class="carousel">
                <div class="carousel-images" id="carouselImages">
                    <c:forEach var="image" items="${outfit.images}" varStatus="status">
                        <div class="carousel-image">
                            <img src="${image.imageUrl}"
                                 alt="${outfit.name} - изображение ${status.index + 1}"
                                 onerror="this.src='https://via.placeholder.com/600x400/007bff/ffffff?text=Изображение+не+загружено'">
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${outfit.images.size() > 1}">
                    <div class="carousel-nav" id="carouselNav">
                        <c:forEach var="image" items="${outfit.images}" varStatus="status">
                            <div class="carousel-dot ${status.first ? 'active' : ''}"
                                 onclick="showSlide(${status.index})"></div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-images">
                <p>Нет изображений для этого образа</p>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">К моим образам</a>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
    </div>
</div>

<script>
    function showSlide(index) {
        const carousel = document.getElementById('carouselImages');
        const dots = document.querySelectorAll('.carousel-dot');

        const slideWidth = carousel.children[0].offsetWidth;
        carousel.scrollLeft = slideWidth * index;

        dots.forEach((dot, i) => {
            dot.classList.toggle('active', i === index);
        });
    }

    let currentSlide = 0;
    const totalSlides = ${outfit.images.size()};

    if (totalSlides > 1) {
        setInterval(() => {
            currentSlide = (currentSlide + 1) % totalSlides;
            showSlide(currentSlide);
        }, 5000);
    }
</script>
</body>
</html>