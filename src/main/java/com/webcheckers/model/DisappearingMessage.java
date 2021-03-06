package com.webcheckers.model;

import java.util.logging.Logger;

/**
 * A UI-friendly representation of a message to the user.
 *
 * <p>
 * This is a <a href='https://en.wikipedia.org/wiki/Domain-driven_design'>DDD</a>
 * <a href='https://en.wikipedia.org/wiki/Value_object'>Value Object</a>.
 * This implementation is immutable and also supports a JSON representation.
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class DisappearingMessage extends Message {
  private static final Logger LOG = Logger.getLogger(DisappearingMessage.class.getName());

  /**
   * The amount of times a DisappearingMessage can appear before it hides/disappears.
   * - Davis Pitts (dep2550)
   */
  private int maximumAppears;

  //Store how many times the message has been shown to the user
  private int showNum;

  //
  // Static Factory methods
  //

  /**
   * A static helper method to create new error messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link DisappearingMessage}
   */
  public static DisappearingMessage error(final String message) {
    return new DisappearingMessage(message, Type.ERROR, 3);
  }

  /**
   * A static helper method to create new informational messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link DisappearingMessage}
   */
  public static DisappearingMessage info(final String message, int showNumTemplate) {
    return new DisappearingMessage(message, Type.INFO, showNumTemplate);
  }

  /**
   * Create a new disappearing message.
   *
   * @param message  the text of the message
   * @param type  the type of message
   * @param appear_count  the amount of times the message should appear before disappearing
   */
  private DisappearingMessage(final String message, final Type type, final int appear_count) {
    super(message, type);
    this.showNum = 0;
    this.maximumAppears = appear_count;
    LOG.finer(this + " created.");
  }

  /**
   * 
   * @return as an integer how many more times the message should be displayed (how many more 10 sec refresh cycles)
   */
  public int getRemainingDisplays(){
    showNum++;
    return(maximumAppears - showNum);
  }

}
