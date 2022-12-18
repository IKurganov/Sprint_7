package testdata;

import io.qameta.allure.Step;
import model.CourierRequest;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierTestData {

    private static final String LOGIN = RandomStringUtils.randomAlphanumeric(5);
    private static final String PASSWORD = "Курьерович123";
    private static final String NAME = "Курьерович";

    @Step("Подготовим запрос на создание пользователя со всеми полями для последующего создания")
    public static CourierRequest getCourierWithAllFields(){
        CourierRequest courier = new CourierRequest();
        courier.setLogin(LOGIN);
        courier.setPassword(PASSWORD);
        courier.setFirstName(NAME);
        return courier;
    }

    @Step("Подготовим запрос на создание пользователя со всеми полями, кроме логина")
    public static CourierRequest getCourierWithoutLogin(){
        CourierRequest courier = new CourierRequest();
        courier.setPassword(PASSWORD);
        courier.setFirstName(NAME);
        return courier;
    }

    @Step("Подготовим запрос на создание пользователя со всеми полями, кроме пароля")
    public static CourierRequest getCourierWithoutPassword(){
        CourierRequest courier = new CourierRequest();
        courier.setLogin(LOGIN);
        courier.setFirstName(NAME);
        return courier;
    }

    @Step("Подготовим запрос на создание пользователя с заранее определенным логином")
    public static CourierRequest getCourierWithDefinedLogin(String login){
        CourierRequest courier = new CourierRequest();
        courier.setLogin(login);
        courier.setPassword(PASSWORD);
        courier.setFirstName(NAME);
        return courier;
    }
}
