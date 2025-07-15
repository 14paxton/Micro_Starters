package com.skeleton;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.skeleton.Command.QueryCommand;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HomeController handles API requests related to sending emails for different campaigns.
 */
@Controller
public class HomeController {
  private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

  /**
   * Handles the endpoint for testing email functionality. This method processes the request and sends a test
   * email based on the provided query command. If the query command is not provided, it creates a default
   * QueryCommand instance.
   *
   * @param body An optional QueryCommand object which may contain email, name, and campaign information.
   * @return A response event indicating the result of the email sending operation.
   */
  @Get("/test")
  public APIGatewayProxyResponseEvent index(@Nullable @Body Optional<QueryCommand> body) {
    return processRequest(() -> {
      QueryCommand queryCommand = body.orElse(new QueryCommand());
      return new APIGatewayProxyResponseEvent();
    });
  }

  /**
   * Processes an API request using the given {@code Processable} instance.
   *
   * @param processable the instance to process the request
   * @return the result of processing the request, returned as an {@code APIGatewayProxyResponseEvent}.
   */
  private APIGatewayProxyResponseEvent processRequest(Processable processable) {
    try {
      return processable.process();
    }
    catch (Exception ex) {
      LOG.error("Exception occurred: ", ex);
      APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
      response.setStatusCode(500);
      response.setBody(ex.getMessage());
      return response;
    }
  }

  /**
   * Endpoint to send a test email.
   *
   * @param email The email address to which the test email will be sent.
   * @return The response of the email sending process encapsulated in an APIGatewayProxyResponseEvent.
   */
  @Get("/test/{email}")
  public APIGatewayProxyResponseEvent testEmail(@PathVariable String email) {
    return processRequest(() -> {
      QueryCommand queryCommand = new QueryCommand(email, null);
      return new APIGatewayProxyResponseEvent();
    });
  }

  @FunctionalInterface
  private interface Processable {
    APIGatewayProxyResponseEvent process() throws Exception;
  }
}