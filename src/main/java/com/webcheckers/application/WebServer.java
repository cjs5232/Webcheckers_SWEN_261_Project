package com.webcheckers.application;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.ui.*;
import com.webcheckers.util.BackupMoveRoute;
import com.webcheckers.util.CheckTurnRoute;
import com.webcheckers.util.ReplayNextTurnRoute;
import com.webcheckers.util.ReplayPreviousTurnRoute;
import com.webcheckers.util.ResignRoute;
import com.webcheckers.util.SubmitTurnRoute;
import com.webcheckers.util.ValidateMoveRoute;

import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  /**
   * Defines one PlayerController
   */
  public static final PlayerController GLOBAL_PLAYER_CONTROLLER = new PlayerController();

  /**
   * Defines one GameController
   */
  public static final GameController GLOBAL_GAME_CONTROLLER = new GameController();

  /**
   * Defines one Queue
   */
  public static final Queue GLOBAL_QUEUE = new Queue();


  /**
   * The URL pattern to request the Home page.
   */
  public static final String HOME_URL = "/";

  /**
   * The URL pattern to request the Login page.
   */
  public static final String LOGIN_URL = "/login";

  /**
   * The URL pattern to add a player 
   */
  public static final String ADD_PLAYER_URL = "/addPlayer";

  /**
   * The URL pattern to start a game.
   * This should not be directly accessed by the user
   */
  public static final String GAME_URL = "/game";

  /**
   * The URL pattern to send a prompt.
   * This should not be directly accessed by the user
   */
  public static final String SEND_PROMPT_URL = "/sendPrompt";

  /**
   * The URL pattern to accept a game.
   * This should not be directly accessed by the user
   */
  public static final String ACCEPT_URL = "/acceptPrompt";

  /**
   * The URL pattern to remove the currentuser
   */
  public static final String LOGOUT_URL = "/logout";

  /**
   * The URL pattern to join the queue
   */
  public static final String QUEUE_URL = "/queue";

  /**
   * The URL pattern to validate a move
   */
  public static final String VALIDATE_MOVE_URL = "/validateMove";

  /**
   * The URL pattern to check if it is a player's turn or not
   */
  public static final String CHECK_TURN_URL = "/checkTurn";

  /**
   * The URL pattern to Submit a turn
   */
  public static final String SUBMIT_TURN_URL = "/submitTurn";

  /**
   * The URL pattern to back up a move
   */
  public static final String BACKUP_MOVE_URL = "/backupMove";

  /**
   * The URL pattern to resign a game
   */
  public static final String RESIGN_URL = "/resignGame";

  /**
   * The URL pattern to browse past games to replay
   */
  public static final String REPLAY_BROWSE_URL = "/replayBrowse";

  /**
   * The URL pattern to replay a game
   */
  public static final String REPLAY_URL = "/replay";

  /**
   * The URL pattern to advance the move in replay mode
   */
  public static final String REPLAY_NEXT_URL = "/replay/nextTurn";

  /**
   * The URL pattern to undo a move in replay mode
   */
  public static final String REPLAY_PREVIOUS_URL = "/replay/previousTurn";

  /**
   * The URL pattern to stop watching a replay
   */
  public static final String STOP_REPLAY_URL = "/replay/stopWatching";

  /**
   * The URL pattern to browse a game to spectate
   */
  public static final String SPECTATE_BROWSE_URL = "/spectateBrowse";

  /**
   * The URL pattern to spectate a game
   */
  public static final String SPECTATE_URL = "/spectate";

  /**
   * The URL pattern for checking a turn while spectating a game
   */
  public static final String SPECTATE_CHECK_TURN_URL = "/spectator/checkTurn";

  /**
   * The URL pattern for stopping spectating a game
   */
  public static final String STOP_SPECTATE_URL = "/spectator/stopWatching";

  /**
   * "/favicon.ico" is the default place that a browser will look for for the 'display icon'
   * that is placed next to a webpage. The default access to said icon is a GET request.
   */
  private static final String FAVICON_URL = "/favicon.ico";

  /**
   * Controls debug messages for the server - if true, debug messages will be printed to the console
   */
  public static final boolean DEBUG_FLAG = true;

  /**
   * Controls easy testing mode board generation
   * 0 - normal game
   * 1 - 2 pieces, one for each color
   * 2 - Double jump
   * 3 - Triple jump
   * 4 - Promotion
   */
  
  public static final Integer TEST_MODE = 4;

  //
  // Attributes
  //
  private final TemplateEngine templateEngine;
  private final Gson gson;

  //
  // Constructor
  //

  /**
   * The constructor for the Web Server.
   *
   * @param templateEngine
   *    The default {@link TemplateEngine} to render page-level HTML views.
   * @param gson
   *    The Google JSON parser object used to render Ajax responses.
   *
   * @throws NullPointerException
   *    If any of the parameters are {@code null}.
   */
  public WebServer(final TemplateEngine templateEngine, final Gson gson) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(gson, "gson must not be null");
    //
    this.templateEngine = templateEngine;
    this.gson = gson;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    //FavIcon
    get(FAVICON_URL, new GetFavIconRoute());

    //Routes for AJax requests
    post(VALIDATE_MOVE_URL, new ValidateMoveRoute());
    post(CHECK_TURN_URL, new CheckTurnRoute());
    post(SUBMIT_TURN_URL, new SubmitTurnRoute());
    post(BACKUP_MOVE_URL, new BackupMoveRoute());
    post(RESIGN_URL, new ResignRoute());

    //Browse games that can be replayed
    get(REPLAY_BROWSE_URL, new GetReplayBrowseRoute(templateEngine));

    //Replay a game
    get(REPLAY_URL, new GetReplayRoute(templateEngine));
    post(REPLAY_NEXT_URL, new ReplayNextTurnRoute());
    post(REPLAY_PREVIOUS_URL, new ReplayPreviousTurnRoute());
    get(STOP_REPLAY_URL, new StopReplayingRoute(templateEngine));

    //Browse games that can be spectated
    get(SPECTATE_BROWSE_URL, new GetSpectateBrowseRoute(templateEngine));

    //Spectate a game
    post(SPECTATE_CHECK_TURN_URL, new CheckTurnRoute());
    get(SPECTATE_URL, new GetSpectateRoute(templateEngine));
    get(STOP_SPECTATE_URL, new StopSpectatingRoute(templateEngine));

    //Starts the sign-in
    post(ADD_PLAYER_URL, new AddPlayerRoute(templateEngine));
    //Finished with sign-in
    get(ADD_PLAYER_URL, new GetHomeRoute(templateEngine));

    //Starts the signout
    post(LOGOUT_URL, new RemovePlayerRoute(templateEngine));
    //Finished with sign-in
    get(LOGOUT_URL, new GetHomeRoute(templateEngine));

    //Shows the signin page
    get(LOGIN_URL, new GetLoginRoute(templateEngine));

    //Shows the game page (not fully implemented yet)
    get(GAME_URL, new GetGameRoute(templateEngine));

    //Shows the Checkers game Home page.
    get(HOME_URL, new GetHomeRoute(templateEngine));

    //Shows queue waiting page.
    get(QUEUE_URL, new GetQueueRoute(templateEngine));

    get(SEND_PROMPT_URL, new SendPromptRoute(templateEngine));

    //Shows the Accept Game page (not fully implemented yet)
    get(ACCEPT_URL, new AcceptPromptRoute(templateEngine));

    //Error routes just in case anything bad happens
    notFound(new GetNotFoundRoute(templateEngine));
    internalServerError(new GetInternalErrorRoute(templateEngine));

    //
    if (DEBUG_FLAG) LOG.config("WebServer is initialized.");
    if (DEBUG_FLAG) LOG.info("Gson object from class: " + gson.getClass() + "is initialized");
  }

}