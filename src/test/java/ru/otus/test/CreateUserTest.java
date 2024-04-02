package ru.otus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import ru.otus.pojo.CreateUserResponse;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.JsonSupport.json;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class CreateUserTest extends TestNGCitrusSpringSupport {
  private TestContext context;

  String name = "Tosha";
  String job = "Worker";

  @Test(description = "Создание пользователя")
  @CitrusTest
  public void createUser(){
    // метод get
    this.context = citrus.getCitrusContext().createTestContext();

    // запрос
    run(http()
            .client("restClientReqres")
            .send()
            .post("users")
            .message()
            .type(MessageType.JSON)
            .body("{\n" +
                    "    \"name\": \"" + name + "\",\n" +
                    "    \"job\": \"" + job + "\"\n" +
                    "}")
    );
    // ответ
    run(http()
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
//            .type(MessageType.JSON)
//            .body(new ClassPathResource("json/user2.json"))
    );
  }
  public CreateUserResponse getResponseData(String name, String job) {

    CreateUserResponse createUserResponse = new CreateUserResponse();
    createUserResponse.setName(name);
    createUserResponse.setJob(job);
    createUserResponse.setId("@isNumber()@");
    createUserResponse.setCreatedAt("unknown");
//        createUserResponse.setCreatedAt("@ignore()@");

    return createUserResponse;
  }

}