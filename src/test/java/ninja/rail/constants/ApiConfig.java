package ninja.rail.constants;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ninja.rail.constants.Constant.API.HOST;

public class ApiConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
    private static RequestSpecification requestSpec;

    public static void init() {
        if (requestSpec == null) {
            logger.info("Initializing a RestAssured configuration with baseUri: {}", HOST);
            RestAssured.baseURI = HOST;
            requestSpec = new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .build();
            logger.debug("RequestSpecification created successfully");
        }
    }

    public static RequestSpecification getRequestSpec() {
        if (requestSpec == null) {
            logger.warn("RequestSpecification is not initialized, init() is called");
            init();
        }
        return requestSpec;
    }
}
