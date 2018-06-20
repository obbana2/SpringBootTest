<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Регистрация</title>
</head>
<body>
Регистрация
<#if message ??>
    ${message}
<#else></#if>
<form action="/registration" method="post">
    <div><label> Логин : <input type="text" name="username"/> </label></div>
    <div><label> Пароль: <input type="password" name="password"/> </label></div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div><input type="submit" value="Регистрация"/></div>
</form>
<a href="/login">Войти</a>
</body>
</html>