<html>

<body>
<div>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="submit" value="Выйти"/>
        <a href="/product">Перейти к товарам</a>
    </form>
</div>
<#if message ??>
    ${message}
<#else></#if>
<#if __isAdmin>
<a href="/addCategory" target="_blank" onClick="popupWin = window.open(this.href, 'contacts', 'location,width=400,height=300,top=0'); popupWin.focus(); return false;">Добавить категорию</a>
</#if>
<br/><br/>
<div>Список категорий:</div>
<br/>
<div>
    <form action="/categoryFilter" method="post">
        <input type="text" name="filter" placeholder="Введите фильтр..." />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="submit" value="Найти"/>
        <a href="/category">Показать все</a>
    </form>
</div>
<table border="1" bordercolor="#000000" cellspacing="0" width="100%">
    <thead bgcolor="#BBBBBB" align="center">
    <tr>
        <td>Номер</td>
        <td>Название</td>
        <td>Описание</td>
        <td>Кол. товаров</td>
        <td>Действия</td>
    </tr>
    </thead>

<#if categories ??>
    <#list categories as category>
        <#if change?? && change = category.id>
        <tr>
            <form method="post">
            <td align="center">${category.id}</td>
            <td><input style="width: 100%;" type="text" name="title" value="${category.title}"></td>
                <td><textarea style="width: 100%;" name="description">${category.description}</textarea></td>
            <td align="center">${category.cnt}</td>
            <td><input type="hidden" name="_csrf" value="${_csrf.token}" /><input type="submit" value="Сохранить"/> <a href="/category">Отменить</a></td>
            </form>
        </tr>
        <#else>
        <tr>
            <td align="center">${category.id}</td>
            <td>${category.title}</td>
                <td>
            <#if category.description?length lt 150>
                ${category.description}
            <#else>
                ${category.description?substring(0,150)} ...
            </#if>
                </td>
            <td align="center">${category.cnt}</td>
            <td>
                <#if __isAdmin>
                <a href="/categoryAction?act=change&id=${category.id}">Изменить</a> <a href="/categoryAction?act=delete&id=${category.id}" onclick="return confirm('Вы действительно хотите удалить категорию? Все товары данной категории также будут удалены')">Удалить</a>
                <#else></#if>
                </td>
        </tr>
        </#if>
    </#list>
<#else></#if>

    <tfoot bgcolor="#BBBBBB" align="center">
    <tr>
        <td>Номер</td>
        <td>Название</td>
        <td>Описание</td>
        <td>Кол. товаров</td>
        <td>Действия</td>
    </tr>
    </tfoot>
</table>
</body>
</html>