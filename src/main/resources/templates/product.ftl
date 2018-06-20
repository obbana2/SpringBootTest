<html>

<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
    var page = 1;
    var elOnPage = 10;
    var filter = "";
    var category = -1;
    var sort = "id";
    var sortType = "ASK";
    var _csrf = "${_csrf.token}";
    function reloadTable() {
        $.ajax({
            url: "/productTable",
            type: "POST",
            data: {
                page: page,
                elOnPage: elOnPage,
                filter: filter,
                category: category,
                sort: sort,
                sortType: sortType,
                _csrf: _csrf
            },
            success: function(data) {
                $('#content').html(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }

    function deleteProduct(productId) {
        if(confirm('Вы точно хотите удалить продукт №' + productId + '?')) {
            $.ajax({
                url: "/deleteProduct",
                type: "POST",
                data: {
                    id: productId,
                    _csrf: _csrf
                },
                success: function (data) {
                    reloadTable();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        }
    }

    function createWindow(url) {
        popupWin = window.open(url, 'contacts', 'location,width=500,height=500,top=0');
        popupWin.focus();
        setTimeout(function() {
            if (popupWin.closed)
                reloadTable();
            else
                setTimeout(arguments.callee, 100);
        }, 100);
    }

    function sorting(column) {
        if(column === sort) {
            sortType = (sortType === 'ASK') ? 'DESC' : 'ASK';
        } else {
            sort = column;
            sortType = 'ASK';
        }
        page = 1;
        reloadTable();
    }

    $(function() {
        reloadTable();
        // Обработчик категории
        $('#category').change(function() {
            category = $("#category option:selected")[0].value;
            page = 1;
            reloadTable();
        });
        $('#filter').submit(function(event) {
            filter = $('#filter input')[0].value;
            page = 1;
            reloadTable();
            event.preventDefault();
        });
        $('#elOnPage').change(function() {
            elOnPage = $("#elOnPage option:selected")[0].value;
            page = 1;
            reloadTable();
        });
    });



</script>
<div>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="submit" value="Выйти"/>
        <a href="/category">Перейти к категориям</a>
    </form>
</div>
<#if message ??>
    ${message}
<#else></#if>
<#if __isAdmin>
<a href="/addProduct" target="_blank" onClick="createWindow(this.href);return false;">Добавить товар</a>
</#if>
<br/><br/>
<div>Список товаров:</div>
<div>
    <form>
        <select id="category" name="category">
            <option value="-1" selected>Все</option>
            <#list categories as category>
                <option value="${category.id}">${category.title}</option>
            </#list>
        </select>
    </form>
</div>
<div>
    <form>
        Отображать на странице:
        <select id="elOnPage" name="elOnPage">
            <option value="5">5</option>
            <option value="10" selected>10</option>
            <option value="20">20</option>
            <option value="30">30</option>
            <option value="40">40</option>
            <option value="50">50</option>
            <option value="100">100</option>
        </select>
         товаров
    </form>
</div>
<div>Фильтр</div>
<div>
    <form id="filter">
    <input type="text" name="filter" />
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button type="submit">Найти</button>
    </form>
</div>
<div id="content"></div>
</body>
</html>