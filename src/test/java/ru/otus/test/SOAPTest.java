package ru.otus.test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.dataaccess.webservicesserver.NumberToDollars;
import com.dataaccess.webservicesserver.NumberToDollarsResponse;
import org.testng.annotations.Test;
import ru.otus.feature.CustomMarshaller;

import java.math.BigDecimal;

import static com.consol.citrus.ws.actions.SoapActionBuilder.soap;
public class SOAPTest extends TestNGCitrusSpringSupport {
  private TestContext context;

  @Test(description = "SOAP Test")
  @CitrusTest
  public void soapTest() {

    this.context = citrus.getCitrusContext().createTestContext();

    CustomMarshaller<Class<NumberToDollars>> ptxRq = new CustomMarshaller<>();
    CustomMarshaller<Class<NumberToDollarsResponse>> ptxRs = new CustomMarshaller<>();

    run(soap()
            .client("soapClient")
            .send()
            .message()
            .body(ptxRq.convert(NumberToDollars.class, getNumberToDollarsRequest(),
                    "http://www.dataaccess.com/webservicesserver/", "NumberToDollars")));

    run(soap()
            .client("soapClient")
            .receive()
            .message()
            .body(ptxRs.convert(NumberToDollarsResponse.class, getNumberToDollarsResponse(),
                    "http://www.dataaccess.com/webservicesserver/", "NumberToDollarsResponse")));
  }

  public NumberToDollars getNumberToDollarsRequest() {
    NumberToDollars numberToDollars = new NumberToDollars();
    numberToDollars.setDNum(new BigDecimal("15"));
    return numberToDollars;
  }

  public NumberToDollarsResponse getNumberToDollarsResponse() {
    NumberToDollarsResponse numberToDollarsResponse = new NumberToDollarsResponse();
    numberToDollarsResponse.setNumberToDollarsResult("fifteen dollars");
    return numberToDollarsResponse;
  }
}
