package ru.otus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class MockTest extends TestNGCitrusSpringSupport {

  private TestContext context;

  @Test(description = "Mock Test")
  @CitrusTest
  public void mockTest() {
    this.context = citrus.getCitrusContext().createTestContext();

    // настоящий запрос
    // fork для распараллеливания, чтобы клиент увидел заглушку для асинхронного взаимод
    run(http()
            .client("restClientMock")
            .send()
            .get("users/" + context.getVariable("userId"))
            .fork(true));

    run(http()
            .server("restServer")
            .receive()
            .get("/api/users/" + context.getVariable("userId"))
    );
    // answer of mock
    run(http()
            .server("restServer")
            .send()
            .response(HttpStatus.OK)
            .message()
            .type("application/json")
            .body("{\n" +
                    "    \"data\": {\n" +
                    "        \"id\": ${userId},\n" +
                    "        \"email\": \"janet.weaver@reqres.in\",\n" +
                    "        \"first_name\": \"Janet\",\n" +
                    "        \"last_name\": \"Weaver\",\n" +
                    "        \"avatar\": \"https://reqres.in/img/faces/2-image.jpg\"\n" +
                    "    },\n" +
                    "    \"support\": {\n" +
                    "        \"url\": \"https://reqres.in/#support-heading\",\n" +
                    "        \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                    "    }\n" +
                    "}")
    );

    // настоящий ответ
    run(http().client("restClientMock")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .type("application/json")
            .body(new ClassPathResource("json/user2.json"))
    );
  }
}
