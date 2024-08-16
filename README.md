Создать серверное приложение на основе Spring Boot (версия spring boot любая, версия jdk 17+).
Проект должен содержать внешний файл настроек, в котором можно будет изменить: подключение к БД, порт сервера.
Для работы с БД использовать JPA.
БД должна быть реляционного типа (любая на ваше усмотрение).

Реализовать следующие запросы (общение JSON сообщениями):
- создание пользователя
- удаление пользователя
- редактирование пользователя
- вход в систему под созданным пользователем
- выход из системы
- список пользователей

где User имеет вид
{
	“id” : long
	“login” : string (size = 50)
	“password” : string (size = 20, должен обязательно содержать спец символ и 3 числа, минимальное кол-во символом 7)
	“fullName” : string, (size = 256)
	“gender” : Gender
}

Gender имеет вид
{
	“id”: int
	“name” : string
}

1 -  мужской, 2 - женский, 3 - не задано

Авторизация должна происходить с использованием JWT. 
Запросы на удаление/редактирование/список пользователей/выход должны работать только для авторизованных пользователей.
Добавить проверку на валидные значения при добавлении/редактировании.

Реализовать WebSocket для STOMP обмена сообщений (JSON): когда какой-то пользователь вызывает запрос список пользователей, делать рассылку в топик с сообщением
{
	“user” : User
	“action” : “use request GET /users”
}

Реализовать Scheduled. Добавить в файл настроек переменную, которая будет принимать значение даты и времени (формат любой). Каждые 10 минут необходимо проверять значение данной переменной. Если текущее время превышает значение переменной, то серверное приложение должно перестать работать (закрыться).

Реализовать метод (попробовать себя в роли клиента), который будет обращаться к себе же через localhost:port с запросом на создание пользователя. Для реализации использовать любую библиотеку HTTP протокола (например: Okhttp, HttpClient и др.).

Если Вы уверены в своих знания SQL: реализовать функцию, которая будет удалять пользователей. На вход подается диапазон идентификаторов (например, [2,5]). Пользователи, у которых идентификаторы попадают в этот диапазон, должны быть удалены. Создать запрос, который будет вызывать данную функцию (обращаться к функции БД через @Query)
