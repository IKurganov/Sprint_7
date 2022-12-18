import config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersTest {
    private static final String GET_ORDERS = "/api/v1/orders";

    @Before
    public void setUp() {
        RestAssured.baseURI = Config.getBaseUri();
    }

    @Test
    @DisplayName("Проверка успешной выгрузки информации по заказам")
    @Step("Проверим, что список заказов будет успешно выгружен")
    public void checkGettingOfOrdersList() {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .queryParam("limit", 10)
                        .queryParam("page", 0)
                        .get(GET_ORDERS);
        response.then().assertThat().statusCode(200)
                .and()
                .body("orders", notNullValue())
                .and()
                .body("orders.findall.size()", equalTo(10));
    }

}
