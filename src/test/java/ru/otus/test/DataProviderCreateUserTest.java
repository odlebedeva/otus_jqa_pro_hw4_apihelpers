package ru.otus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.otus.pojo.CreateUserResponse;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.JsonSupport.json;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

// Параметризованный тест
public class DataProviderCreateUserTest extends TestNGCitrusSpringSupport {
  private TestContext context;

  @DataProvider(name = "usersProvider")
  public Object[][] getDataProvider(){
    return new Object[][]{
            new Object[]{"George", "Driver"},
            new Object[]{"Nick", "Teacher"},
            new Object[]{"Anna", "Tester"},
            new Object[]{"Mike", "Actor"},
            new Object[]{"Liana", "Assistant"},
            new Object[]{"Peter", "Chef"},
            new Object[]{"Mary", "Conductor"},
            new Object[]{"Alex", "Controller"},
            new Object[]{"Ariel", "Decorator"},
            new Object[]{"Greg", "Fixer"},
    };
  }

  @Test(description = "Создание пользователя", dataProvider = "usersProvider")
  @CitrusTest
  public void createUserParametrized(String name, String job){
    this.context = citrus.getCitrusContext().createTestContext();

    $(http()
            .client("restClientReqres")
            .send()
            .post("users")
            .message()
            .type("application/json")
            .body("{\n" +
                    "    \"name\": \"" + name + "\",\n" +
                    "    \"job\": \"" + job + "\"\n" +
                    "}"));
    // ответ
    $(http()
            .client("restClientReqres")
            .receive()
            .response(HttpStatus.CREATED)
            .message()
            .type("application/json")
            .body(new ObjectMappingPayloadBuilder(getResponseData(name, job), "objectMapper"))
            .validate(json()
                    .ignore("$.createdAt"))
            .validate(jsonPath()
                    .expression("$.name", name)
                    .expression("$.job", job))
            .extract(fromBody()
                    .expression("$.id", "currentId")
                    .expression("$.createdAt", "createdAt"))
    );
    $(echo("currentId = ${currentId} and createdAt = ${createdAt}"));
  }
  public CreateUserResponse getResponseData(String name, String job) {

    CreateUserResponse createUserResponse = new CreateUserResponse();
    createUserResponse.setName(name);
    createUserResponse.setJob(job);
    createUserResponse.setId("@isNumber()@");
    createUserResponse.setCreatedAt("unknown");
    //createUserResponse.setCreatedAt("@ignore()@");

    return createUserResponse;
  }
}
