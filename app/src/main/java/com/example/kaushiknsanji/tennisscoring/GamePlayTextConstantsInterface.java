package com.example.kaushiknsanji.tennisscoring;

/**
 * Created by Kaushik N Sanji on 12-Feb-17.
 * Marker Interface that holds the Text Messages & formats to be displayed on the user screen
 *
 * @author <a href="mailto:kaushiknsanji@gmail.com">Kaushik N Sanji</a>
 */
public interface GamePlayTextConstantsInterface {

    public final String WELCOME_MSG_STR = "Welcome to Tennis Scoring App, to begin please select the Match type above and then click the button below";

    public final String NO_MATCH_TYPE_SELECTED_STR = "Please choose the Match type above to begin scoring";

    public final String TOSS_MSG_STR = "Player - %d won the toss and has opted to serve";

    public final String DEUCE_MSG_STR = "Player %d, Set %d : DEUCE!!";

    public final String DEUCE_REPEAT_MSG_STR = "Player %d, Set %d : Back to DEUCE!!";

    public final String ADV_PLAYER_MSG_STR = "Player %d, Set %d : Advantage Player %d!!";

    public final String GAMEPLAY_POINT_MSG_STR = "Player %d, Set %d : %s - %s";

    public final String TIEBREAKER_POINT_MSG_STR = "Player %d, Set %d, Tiebreaker : %s - %s";

    public final String GAME_POINT_MSG_STR = "Player %d, Set %d : %s - %s";

    public final String TIEBREAKER_START_MSG_STR = "Entering Tie Breaker for Set %d, Player %d to Serve";

    public final String SET_POINT_MSG_STR = "Game, Set %d, Player %d : %s - %s";

    public final String NEXT_SET_MSG_STR = "Player %d : %s - %s, New Set";

    public final String MATCH_POINT_MSG_STR = "Player %d defeats Player %d : %s";

}
