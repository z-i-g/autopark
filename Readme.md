Autopark - это проект на Spring Boot для мониторинга и управления автомобилями на предприятиях. 
Архитектура проекта по паттерну MVC. Деплой приложения можно сделать с помощью файла deploy.bat, заменив переменные с сервером и учетной записью на свои. Сам делал деплой на VkCloud.

Java 17

Spring Boot 3.2.1

Возможности:
- учетные записи менеджеров и обычных пользователей;
- просмотр списка предприятий, автомобилей, водителей с детальной информацией по каждому пункту;

![img_2.png](img_2.png)![img_3.png](img_3.png)
- просмотр маршрутов + отрисовка на картах;

![img_5.png](img_5.png)
- назначение водителей, автомобилей;
- добавление/удаление новых автомобилей, водителей;
- просмотр и генерация отчетов по автомобилям;

![img_4.png](img_4.png)
- просмотр отчетов через Telegram-бот;

  ![img.png](img.png)
- просмотр метрик на Grafana;