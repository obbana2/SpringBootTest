<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Страница входа</title>
</head>
<body>
Страница входа
<form action="/login" method="post">
    <div><label> Логин : <input type="text" name="username"/> </label></div>
    <div><label> Пароль: <input type="password" name="password"/> </label></div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div><input type="submit" value="Войти"/></div>
</form>
<a href="/registration">Регистрация</a>
</body>
</html>