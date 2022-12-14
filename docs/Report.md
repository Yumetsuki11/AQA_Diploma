# Отчёт по итогам тестирования
### Описание
В ходе работ была проведена автоматизация тестирования сервиса покупки тура **"Марракэш"**, взаимодействующего с СУБД и API Банка. Тесты включают позитивные и негативные сценарии, относящиеся к UI приложения, а также взаимодействию с СУБД с использованием MySQL и PostgreSQL (через Docker).

### Обзор тест-кейсов
Всего было выполнено **84** сценария, из которых **53** прошли успешно, **31** неуспешно.  
![image](https://user-images.githubusercontent.com/105731584/205255964-c583e30c-e3e2-4b46-a454-198cb64ac53c.png)  

*Среди неуспешных сценариев **1** не прошёл по причине сбоя в работе системы сборки; дефект не воспроизводится и фактически отсутствует.* Если его не учитывать, доля успешных сценариев, относящихся к модулю покупки в кредит и модулю стандартного платежа, одинакова и составляет **62%**.
![image](https://user-images.githubusercontent.com/105731584/205256493-5024c2c7-ce7a-40a2-b36c-362290212521.png)  

Все позитивные сценарии прошли успешно: таким образом, дефектов на "счастливом пути" не обнаружено. Однакопадающие негативные тесты обнаружились в каждой из подгрупп:
![image](https://user-images.githubusercontent.com/105731584/205258108-c49d3946-0dc0-4c59-abac-ddce61b9a850.png)  

Серьёзность найденных дефектов в среднем невысокая, и в основном они относятся к валидации отдельных полей:
![image](https://user-images.githubusercontent.com/105731584/205259234-eed523b4-d373-45ca-a381-f7a1b74dc9fe.png)

### Рекомендации
1. Доработка валидации полей: сейчас некоторые поля принимают некорректные значения (см. #7 #8); в других же случаях ошибки ловятся, но поясняющий их текст не вполне подходящий (#5)
2. Доработка уведомлений об успешности/ошибке оплаты: хоть ошибок во взаимодействии приложения с базой и не найдено, уведомления пользователю не всегда соответствуют результату (#4 #9)
3. Возможно, в названии тура ошибка, поскольку город пишется "Марракеш" (если это не "фишка" и специальное название для тура - это нигде не указано)
4. Поскольку формы на кредит и обычную оплату одинаковые, более удобным для пользователя был бы интерфейс, где сначала заполнялась бы форма, а потом, вместо кнопки "Продолжить", кнопкой отправки выбирался бы один из способов