<html>

<body>
<#if message ??>
    ${message}
<#else></#if>
<div>Изменить товар:</div>
<div>
    <img src="/uploads/${product.image}" title="Нет изображения"><br/>
    <form method="post" enctype="multipart/form-data">
        <label>Категория:</label><br/>
        <select name="category" required>
        <#if categories ??>
            <#list categories as category>
                <option value="${category.id}" <#if category.id = product.category.id >selected</#if>>${category.title}</option>
            </#list>
        </#if>
        </select><br/><br/>
        <label>Название:</label><br/>
        <input type="text" name="title" placeholder="Введите название" value="${product.title}" required/><br/><br/>
        <label>Описание:</label><br/>
        <textarea name="description" placeholder="Введите описание">${product.description}</textarea><br/><br/>
        <label>Количество:</label><br/>
        <input type="text" name="quantity" placeholder="Введите количество" value="${product.quantity}" min="0" pattern="^[0-9]+$" required><br/><br/>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <label>Картинка (не загружайте, если не хотите изменить):</label><br/>
        <input type="file" name="file" accept=".png,.jpg,.jpeg" /><br/><br/>
        <button type="submit">Сохранить</button>
    </form>
</div>
</body>
</html>