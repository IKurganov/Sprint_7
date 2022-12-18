import config.Config;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.OrdersRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrdersTest {
    private static final String ORDERS_CREATION = "/api/v1/orders";
    public static String ORDERS_CANCEL_TRACK = "/api/v1/orders/cancel?track=";
    private final String[] color;

    public OrdersTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Config.getBaseUri();
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Успешное создание заказа с различным значением цвета - color")
    @Step("Проверяется создание заказа с корректными данными и различными значениями в color")
    public void checkCreateOrder() {
        OrdersRequest orderRequest = new OrdersRequest();
        orderRequest.setFirstName(RandomStringUtils.randomAlphanumeric(8));
        orderRequest.setLastName(RandomStringUtils.randomAlphanumeric(8));
        orderRequest.setAddress(RandomStringUtils.randomAlphanumeric(16));
        orderRequest.setMetroStation(RandomStringUtils.randomNumeric(2));
        orderRequest.setPhone(RandomStringUtils.randomNumeric(8));
        orderRequest.setRentTime((int) (Math.random() * 10));
        orderRequest.setDeliveryDate("2022-12-31");
        orderRequest.setComment(RandomStringUtils.randomAlphanumeric(16));
        orderRequest.setColor(color);


        int track = given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .post(ORDERS_CREATION)
                .then().assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue())
                .extract()
                .path("track");

        //удаляем заказ
        String point = ORDERS_CANCEL_TRACK;
        ORDERS_CANCEL_TRACK = ORDERS_CANCEL_TRACK + track;

        given()
                .contentType(ContentType.JSON)
                .put(ORDERS_CANCEL_TRACK)
                .then().assertThat()
                .statusCode(200);

        ORDERS_CANCEL_TRACK = point;
    }
}
