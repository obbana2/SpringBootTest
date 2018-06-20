<#if products ??>
    <#if pages ??>
    Страницы:
        <#list 1..pages as page>
            <#if currentPage = page>
                ${page}
            <#else>
            <a href="#${page}" onClick="page=${page};reloadTable();return false;">${page}</a>
            </#if>
        </#list>
    <#else></#if>
</#if>
<table border="1" bordercolor="#000000" cellspacing="0" width="100%">
    <thead bgcolor="#BBBBBB" align="center">
    <tr>
        <td onclick="sorting('id');">Номер</td>
        <td>Изображение</td>
        <td onclick="sorting('category');">Категория</td>
        <td onclick="sorting('title');">Название</td>
        <td onclick="sorting('description');">Описание</td>
        <td onclick="sorting('quantity');">Кол. товаров</td>
        <td>Действия</td>
    </tr>
    </thead>

<#if products ??>
    <#list products as product>
        <tr>
            <td align="center">${product.id}</td>
            <td align="center"><img src="/uploads/${product.image}" title="Нет изображения"></td>
            <td align="center">${product.category.title}</td>
            <td>${product.title}</td>
            <td>
            <#if product.description?length lt 150>
                ${product.description}
            <#else>
                ${product.description?substring(0,150)} ...
            </#if>
            </td>
            <td align="center">${product.quantity}</td>
            <td>
                <#if __isAdmin>
                    <a href="/editProduct?id=${product.id}" target="_blank" onClick="createWindow(this.href); return false;">Изменить</a>
                    <a href="/deleteProduct?id=${product.id}" onclick="deleteProduct(${product.id}); return false;">Удалить</a>
                <#else></#if>
            </td>
        </tr>
    </#list>
<#else>
    <tr>
        <td align="center" colspan="7">Товары не найдены</td>
    </tr>
</#if>

    <tfoot bgcolor="#BBBBBB" align="center">
    <tr>
        <td onclick="sorting('id');">Номер</td>
        <td>Изображение</td>
        <td onclick="sorting('category');">Категория</td>
        <td onclick="sorting('title');">Название</td>
        <td onclick="sorting('description');">Описание</td>
        <td onclick="sorting('quantity');">Кол. товаров</td>
        <td>Действия</td>
    </tr>
    </tfoot>
</table>
<#if products ??>
<div>
    Показано ${viewElements} из ${totalElements} товаров.
</div>
</#if>