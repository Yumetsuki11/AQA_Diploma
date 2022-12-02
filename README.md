## Документы
- [План автоматизации тестирования](https://github.com/Yumetsuki11/AQA_Diploma/blob/9d82846e174f8354e37698037a81bf0ec9c3ef0e/docs/plan.md)
- [Отчёт по итогам тестирования](https://github.com/Yumetsuki11/AQA_Diploma/blob/9d82846e174f8354e37698037a81bf0ec9c3ef0e/docs/Report.md)
- [Отчёт по итогам автоматизации](https://github.com/Yumetsuki11/AQA_Diploma/blob/9d82846e174f8354e37698037a81bf0ec9c3ef0e/docs/Summary.md)

## Инструкция по запуску автотестов

1. Установить и запустить Docker
2. Установить IntelliJ IDEA и запустить в этой среде проект
3. В терминале в папке проекта запустить контейнеры: `docker-compose up`
4. В терминале в папке проекта запустить SUT:  
      c MySQL - командой `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`  
      c PostgreSQL - командой `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`
5. В терминале в папке проекта запустить тесты:  
      c MySQL - командой `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`  
      c PostgreSQL - командой `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
6. Дождаться завершения работы тестов (~10 мин)
7. Сгенерировать отчёт Allure: в терминале командой `./gradlew allureServe`

Остановка процессов производится в соответствующих терминалах следующими командами:  
**Allure** - `Ctrl+C`  
**SUT** - `Ctrl+C`  
**Контейнеры** - `docker-compose down`
