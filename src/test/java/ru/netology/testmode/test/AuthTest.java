package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    // Перед каждым прогоном тестов подключаемся к сервису
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    // Проведем функциональное позитивное тестирование сервиса --------------------------------
    // Тест подключения зарегистрированного пользователя
    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        // Вводим логин пользователя
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        // Вводим пароль пользователя
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        // Нажимаем кнопку отправки формы
        $("button.button").click();
        // Ожидаем "входа в кабинет" - появления заголовка с нужным текстом
        $("H2").shouldHave(exactText("Личный кабинет"))
                        .shouldBe(visible);
    }

    // Проведем функциональные негативные тесты сервиса ------------------------------------------
    // Тест подключения нерегистрированного пользователя
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        // Вводим логин пользователя
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        // Вводим пароль пользователя
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        // Нажимаем кнопку отправки формы
        $("button.button").click();
        // Ожидаем "входа в кабинет" - появления текста с ошибкой
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    // Тест подключения блокированного пользователя
    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        // Вводим логин пользователя
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        // Вводим пароль пользователя
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        // Нажимаем кнопку отправки формы
        $("button.button").click();
        // Ожидаем "входа в кабинет" - появления текста с ошибкой
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"))
                .shouldBe(visible);
    }

    // Тест подключения пользователя с неверным логином
    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        // Вводим логин пользователя
        $("[data-test-id=login] input").setValue(wrongLogin);
        // Вводим пароль пользователя
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        // Нажимаем кнопку отправки формы
        $("button.button").click();
        // Ожидаем "входа в кабинет" - появления текста с ошибкой
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    // Тест подключения пользователя с неверным паролем
    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        // Вводим логин пользователя
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        // Вводим пароль пользователя
        $("[data-test-id=password] input").setValue(wrongPassword);
        // Нажимаем кнопку отправки формы
        $("button.button").click();
        // Ожидаем "входа в кабинет" - появления текста с ошибкой
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible);
    }
}
