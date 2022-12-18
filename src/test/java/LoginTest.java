import config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierRequest;
import model.LoginRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import testdata.LoginTestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static testdata.CourierTestData.getCourierWithAllFields;
import static testdata.LoginTestData.*;

public class LoginTest {
    private static final String COURIER = "/api/v1/courier";
    private static final String COURIER_LOGIN = "/api/v1/courier/login";

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
    @DisplayName("Проверка успешного логина курьера")
    @Step("Проверим, что курьер будет успешно авторизован в системе, в ответе будет значение id")
    public void checkLoginOfCourier() {
        CourierRequest courierRequest = getCourierWithAllFields();
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(courierRequest)
                .post(COURIER);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(getRequestFromCourier(courierRequest))
                        .post(COURIER_LOGIN);
        response.then().assertThat().statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Запрет на авторизацию несуществующего пользователя")
    @Step("Проверяем, авторизацию пользователя с некорректным логином и паролем")
    public void checkLoginErrorOfNonExistingCourier() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestForNotExistingCourier())
                .post(COURIER_LOGIN);
        response.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка запрета авторизации пользователя с отсутствующем полем в запросе")
    @Step("Проверяем, что если какого-то поля нет в запросе, запрос на авторизацию вернет ошибку")
    public void checkLoginErrorOfBadRequest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestWithoutLogin())
                .post(COURIER_LOGIN);
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}