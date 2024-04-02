package ru.otus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

// Junit4CitrusSpringSupport
public class GetUser2Test extends TestNGCitrusSpringSupport {

  private TestContext context;

  @Test(description = "Получение информации о пользователе")
  @CitrusTest
  public void getUserInfo() {

    this.context = citrus.getCitrusContext().createTestContext();
    run(http()
            .client("restClientReqres")
            .send()
            .get("users/" + context.getVariable("userId"))
    );

    run(http().client("restClientReqres")
            .receive()
            .response(HttpStatus.OK)
            .message()
            .type("application/json")
            .body(new ClassPathResource("json/user2.json"))
    );
  }
}
//  @Test(description = "Получение информации о пользователе")
//  @CitrusTest
//  public void getUserInfo(){
//
//    this.context = citrus.getCitrusContext().createTestContext();
//
//    // это тест-степы
//    context.setVariable("value", "superValue");
//
//    $(echo("Property \"value\" = " + context.getVariable("value")));
//
//    $(echo("We have userId = " + context.getVariable("userId")));
//    // запрос
//    run(http()
//            .client("restClientReqres")
//            .send()
//            .get("users/" + context.getVariable("userId"))
//    );
//    // ответ
//    run(http()
//            .client("restClientReqres")
//            .receive()
//            .response(HttpStatus.OK)
//            .message()
//            .type(MessageType.JSON)
//            .body(new ClassPathResource("json/user2.json"))
//    );
////    $(echo("We have userId= " + "${userId}")); - тоже самое, что выше
//  }
//}
