import config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.LoginRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import testdata.LoginTestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static testdata.CourierTestData.*;

public class CourierTest {
    private static final String COURIER = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";

    @Before
    public void setUp() {
        RestAssured.baseURI = Config.getBaseUri();
    }

    @AfterClass
    public static void removeTestData() {

        LoginRequest loginRequest = LoginTestData.getRequestFromCourier(getCourierWithAllFields());

        int id = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .post(COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");

        given()
                .header("Content-type", "application/json")
                .delete(COURIER + '/' + id);
    }

    @Test
    @DisplayName("Проверка успешного создания курьера")
    @Step("Проверим, что курьер будет успешно создан, если передать в запросе на создание все поля")
    public void checkCreationOfCourier() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(getCourierWithAllFields())
                        .post(COURIER);
        response.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка запрета на создание двух одинаковых курьеров")
    @Step("Проверим, что курьер-клон не будет создан и что будет возвращен корректный код и сообщение об ошибке")
    public void checkLoginUniqueness() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(getCourierWithAllFields())
                        .post(COURIER);
        response.then().assertThat().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка запрета на создание курьера без логина")
    @Step("Проверим, что курьер не будет создан, если в запросе не был передан логин - будет передано корректное сообщение об ошибке")
    public void checkLoginIsRequired() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(getCourierWithoutLogin())
                        .post(COURIER);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка запрета на создание курьера без пароля")
    @Step("Проверим, что курьер не будет создан, если в запросе не был передан пароль - будет передано корректное сообщение об ошибке")
    public void checkPasswordIsRequired() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(getCourierWithoutPassword())
                        .post(COURIER);
        response.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
