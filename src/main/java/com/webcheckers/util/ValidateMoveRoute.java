package com.webcheckers.util;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class ValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(ValidateMoveRoute.class.getName());

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public ValidateMoveRoute() {
        LOG.config("ValidateMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.info("ValidateMoveRoute is invoked.");

        LOG.info("move param passed from js (request.queryParams(\"move\")): " + request.queryParams("move"));
        LOG.info("move param passed from js (request.params().get(\"move\")): " + request.params().get("move"));
        LOG.info("move param passed from js (request.attribute(\"move\")): " + request.attribute("move"));

        System.out.println(request.body());
        System.out.println(request.queryParams());
        System.out.println(request.params());
        System.out.println(request.attributes());

        

        return null;
    }
    
}
