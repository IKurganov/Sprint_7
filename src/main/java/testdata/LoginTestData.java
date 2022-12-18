package testdata;

import io.qameta.allure.Step;
import model.CourierRequest;
import model.LoginRequest;
import org.apache.commons.lang3.RandomStringUtils;

public class LoginTestData {
    private static final String LOGIN = RandomStringUtils.randomAlphanumeric(5);
    private static final String PASSWORD = RandomStringUtils.randomAlphanumeric(5);

    @Step("Подготовим запрос на авторизацию пользователя со всеми полями - взяв информацию из запроса на создание курьера")
    public static LoginRequest getRequestFromCourier(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;

    }

    @Step("Подготовим запрос на авторизацию пользователя со всеми полями - взяв рандомную информацию для запроса")
    public static LoginRequest requestForNotExistingCourier() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(LOGIN);
        loginRequest.setPassword(PASSWORD);
        return loginRequest;
    }

    @Step("Подготовим запрос на авторизацию пользователя без поля логина")
    public static LoginRequest requestWithoutLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(PASSWORD);
        return loginRequest;
    }
}
