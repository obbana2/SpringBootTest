<html>

<body>
<#if message ??>
    ${message}
<#else></#if>
<div>Добавить товар:</div>
<div>
    <form method="post" enctype="multipart/form-data">
        <label>Категория:</label><br/>
        <select name="category" required>
        <#if categories ??>
            <#list categories as category>
                <option value="${category.id}">${category.title}</option>
            </#list>
        </#if>
        </select><br/><br/>
        <label>Название:</label><br/>
        <input type="text" name="title" placeholder="Введите название" required/><br/><br/>
        <label>Описание:</label><br/>
        <textarea name="description" placeholder="Введите описание"></textarea><br/><br/>
        <label>Количество:</label><br/>
        <input type="text" name="quantity" placeholder="Введите количество" min="0" pattern="^[0-9]+$" required><br/><br/>
        <label>Картинка:</label><br/>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="file" name="file" accept=".png,.jpg,.jpeg"/><br/><br/>
        <button type="submit">Добавить</button>
    </form>
</div>
</body>
</html>