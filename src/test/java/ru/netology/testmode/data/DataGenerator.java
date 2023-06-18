package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    // Отправка сервису JSON-пакета данных о пользователе и ожидание кода ответа "200"
    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Случайным образом формируем логин пользователя
    public static String getRandomLogin() {
        // Инициализируем экземпляр Faker-a
        Faker faker = new Faker(new Locale("en"));
        return faker.name().username();
    }

    // Случайным образом формируем пароль пользователя
    public static String getRandomPassword() {
        Faker faker = new Faker(new Locale("en"));
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        // Создаем нового пользователя
        public static RegistrationDto getUser(String status) {
            var user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        // Отправляем пакет данных по зарегистрированному пользователю
        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
