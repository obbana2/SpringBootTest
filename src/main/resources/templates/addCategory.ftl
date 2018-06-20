<html>

<body>
<#if message ??>
    ${message}
<#else></#if>
<div>Добавить категорию:</div>
<div>
    <form method="post">
        <label>Название:</label><br/>
        <input type="text" name="title" placeholder="Введите название" required/><br/><br/>
        <label>Описание:</label><br/>
        <textarea name="description" placeholder="Введите описание" required></textarea><br/><br/>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit">Добавить</button>
    </form>
</div>
</body>
</html>