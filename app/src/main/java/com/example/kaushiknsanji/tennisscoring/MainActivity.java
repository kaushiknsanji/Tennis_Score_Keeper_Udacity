package com.example.kaushiknsanji.tennisscoring;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * App's Main Activity which is a Single Screen App that takes care of the Tennis Score keeping
 * of current Match between players based on the Match Type(Gender) Selection
 *
 * @author <a href="mailto:kaushiknsanji@gmail.com">Kaushik N Sanji</a>
 */
public class MainActivity extends AppCompatActivity
        implements GamePlayTextConstantsInterface {

    //holds the state for the start/reset button of the Scoring app
    private boolean mIsGameStarted = false;

    //Radio button booleans for Match Type selection
    private boolean mIsMatchTypeMen = false;
    private boolean mIsMatchTypeWomen = false;

    private boolean mIsDeuceGame = false; //holds the state of Deuce in a Game, defaulted to false
    private boolean mIsTieBreaker = false; //holds the state of Tie Breaker in a Set, defaulted to false

    private int mPlayerToServe; //Integer number of the player who is serving (1 - Player1, 2 - Player2)
    private int mPlayerToOpenSet; //Integer number of the player who opens a set (1 - Player1, 2 - Player2)
    private int mCurrentSetPlay; //Integer number of the current Set being played

    private int mTotalSetsToPlay; //Integer number of the total Sets to be played, decided by the Match type selection
    private int mTotalSetsToWin; //Integer number of the total Sets to be won, decided by the Match type selection

    //Declaring List of TextViews for storing the views of score board for each player
    private ArrayList<TextView> mP1ScoreBoardTextList;
    private ArrayList<TextView> mP2ScoreBoardTextList;

    //Declaring List of TextViews for storing the views of Game play for each player
    private ArrayList<TextView> mP1GamePlayTextList;
    private ArrayList<TextView> mP2GamePlayTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            //When the activity is loaded for the first time

            Log.i(this.getClass().getSimpleName(), "onCreate: loaded for the first time");

            //Hiding the Tie Breaker Score Layout
            toggleTieBreakerLayout(false);

            //Update Game play text with the Welcome message
            updateGamePlayText(WELCOME_MSG_STR);

            //disabling the plus buttons
            enablePlusButtons(false);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(this.getClass().getSimpleName(), "onSaveInstanceState: called");

        //Adding all the member variables to the bundle : START
        outState.putBoolean("IsGameStarted", mIsGameStarted);
        outState.putBoolean("IsMatchTypeMen", mIsMatchTypeMen);
        outState.putBoolean("IsMatchTypeWomen", mIsMatchTypeWomen);
        outState.putBoolean("IsDeuceGame", mIsDeuceGame);
        outState.putBoolean("IsTieBreaker", mIsTieBreaker);

        outState.putInt("PlayerToServe", mPlayerToServe);
        outState.putInt("PlayerToOpenSet", mPlayerToOpenSet);
        outState.putInt("CurrentSetPlay", mCurrentSetPlay);
        outState.putInt("TotalSetsToPlay", mTotalSetsToPlay);
        outState.putInt("TotalSetsToWin", mTotalSetsToWin);

        if (mIsGameStarted) {

            for (int i = 0; i <= mTotalSetsToPlay; i++) {
                outState.putString("P1ScoreBoardTextList_" + i, mP1ScoreBoardTextList.get(i).getText().toString());
                outState.putString("P2ScoreBoardTextList_" + i, mP2ScoreBoardTextList.get(i).getText().toString());
            }

            for (int i = 0; i < 3; i++) {
                outState.putString("P1GamePlayTextList_" + i, mP1GamePlayTextList.get(i).getText().toString());
                outState.putString("P2GamePlayTextList_" + i, mP2GamePlayTextList.get(i).getText().toString());
            }

        }

        //Saving state of plus buttons
        Button p1PlusButton = (Button) findViewById(R.id.p1_plus_btn);
        outState.putBoolean("IsPlusButtonEnabled", p1PlusButton.isEnabled());

        //Saving the Game Play Text
        TextView gamePlayTextView = (TextView) findViewById(R.id.game_play_text);
        outState.putString("GamePlayText", gamePlayTextView.getText().toString());

        //Saving the Start-Reset Button Text
        Button startResetButton = (Button) findViewById(R.id.start_reset_btn);
        outState.putString("StartResetButtonText", startResetButton.getText().toString());

        //Adding all the member variables to the bundle : END

        //Saving the state of the view hierarchy through default implementation
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onRestoreInstanceState: called");

        //Restoring the state of the view hierarchy through default implementation
        super.onRestoreInstanceState(savedInstanceState);

        //Restoring all the member variables from the bundle : START
        mIsGameStarted = savedInstanceState.getBoolean("IsGameStarted");
        mIsDeuceGame = savedInstanceState.getBoolean("IsDeuceGame");
        mIsMatchTypeMen = savedInstanceState.getBoolean("IsMatchTypeMen");
        mIsMatchTypeWomen = savedInstanceState.getBoolean("IsMatchTypeWomen");
        mIsTieBreaker = savedInstanceState.getBoolean("IsTieBreaker");

        mPlayerToServe = savedInstanceState.getInt("PlayerToServe");
        mPlayerToOpenSet = savedInstanceState.getInt("PlayerToOpenSet");
        mCurrentSetPlay = savedInstanceState.getInt("CurrentSetPlay");
        mTotalSetsToPlay = savedInstanceState.getInt("TotalSetsToPlay");
        mTotalSetsToWin = savedInstanceState.getInt("TotalSetsToWin");

        //Restoring the state of Match type selection
        RadioGroup matchTypeRadioGroup = (RadioGroup) findViewById(R.id.match_type_rbtn_grp);
        if (mIsMatchTypeMen) {
            matchTypeRadioGroup.check(R.id.mens_rbtn);
        } else if (mIsMatchTypeWomen) {
            matchTypeRadioGroup.check(R.id.womens_rbtn);
        }

        if (mIsMatchTypeMen || mIsMatchTypeWomen) {
            relayoutMatchTypeScoreboard(); //Relaying out scoreboard only when selection is already done
        }

        //Restoring the state of the plus buttons
        enablePlusButtons(savedInstanceState.getBoolean("IsPlusButtonEnabled"));

        //Restoring the Game Play Text
        updateGamePlayText(savedInstanceState.getString("GamePlayText"));

        //Restoring the Start-Reset Button Text
        changeStartResetButtonText(savedInstanceState.getString("StartResetButtonText"));

        //Restoring the state of Tie-Breaker layout
        toggleTieBreakerLayout(mIsTieBreaker);

        if (mIsGameStarted) {
            enableMatchTypeRadioGrp(false); //Disabling only when the Game was previously started

            //Initializing the ArrayLists to save the scores
            initScoreArrayLists();

            //Restoring the Scoreboard TextViews
            for (int i = 0; i <= mTotalSetsToPlay; i++) {
                String p1ScoreStr = savedInstanceState.getString("P1ScoreBoardTextList_" + i, "0");
                mP1ScoreBoardTextList.get(i).setText(p1ScoreStr);
                String p2ScoreStr = savedInstanceState.getString("P2ScoreBoardTextList_" + i, "0");
                mP2ScoreBoardTextList.get(i).setText(p2ScoreStr);
            }

            //Restoring the GamePlay Scoreboard TextViews
            for (int i = 0; i < 3; i++) {
                String p1ScoreStr = savedInstanceState.getString("P1GamePlayTextList_" + i, "0");
                mP1GamePlayTextList.get(i).setText(p1ScoreStr);
                String p2ScoreStr = savedInstanceState.getString("P2GamePlayTextList_" + i, "0");
                mP2GamePlayTextList.get(i).setText(p2ScoreStr);
            }

            //Restoring the active player highlights
            setActivePlayerAttr();
        }

        //Restoring all the member variables from the bundle : END
    }

    /**
     * Method to toggle the visibility of Tie Breaker Score Layout
     *
     * @param visibilityState <br/><b>TRUE</b> - enables the layout
     *                        <br/><b>FALSE</b> - hides the layout
     */
    private void toggleTieBreakerLayout(boolean visibilityState) {
        View p1TieBreakerLayout = findViewById(R.id.p1_tie_breaker_game_layout);
        View p2TieBreakerLayout = findViewById(R.id.p2_tie_breaker_game_layout);

        if (visibilityState) {
            //If boolean passed is true, then enable the tie breaker layouts
            p1TieBreakerLayout.setVisibility(View.VISIBLE);
            p2TieBreakerLayout.setVisibility(View.VISIBLE);
        } else {
            //If boolean passed is false, then hide the tie breaker layouts
            p1TieBreakerLayout.setVisibility(View.GONE);
            p2TieBreakerLayout.setVisibility(View.GONE);
        }

    }

    /**
     * Method to enable/disable the Plus Button views
     *
     * @param enabled <br/> - Boolean to enable/disable the Plus Button Views 'p1_plus_btn' & 'p2_plus_btn'
     *                <br/><b>TRUE</b> - to enable
     *                <br/><b>FALSE</b> - to disable
     */
    private void enablePlusButtons(boolean enabled) {
        //Changing for Player - 1
        Button p1PlusButton = (Button) findViewById(R.id.p1_plus_btn);
        p1PlusButton.setEnabled(enabled);

        //Changing for Player - 2
        Button p2PlusButton = (Button) findViewById(R.id.p2_plus_btn);
        p2PlusButton.setEnabled(enabled);
    }

    /**
     * Method to initialize ArrayLists to TextViews that save/update the scores
     */
    private void initScoreArrayLists() {
        //Initializing the TextView ArrayLists of player 1 : START
        mP1ScoreBoardTextList = new ArrayList<>(mTotalSetsToPlay + 1);
        mP1GamePlayTextList = new ArrayList<>(3); //Size inclusive of Tie breaker

        if (mIsMatchTypeWomen) {
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_total_set_pts)); //0 index
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set1_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set2_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set3_pts));
        } else if (mIsMatchTypeMen) {
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_total_set_pts)); //0 index
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set1_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set2_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set3_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set4_pts));
            mP1ScoreBoardTextList.add((TextView) findViewById(R.id.p1_set5_pts));
        }

        mP1GamePlayTextList.add((TextView) findViewById(R.id.p1_game_pts_text)); //0 index
        mP1GamePlayTextList.add((TextView) findViewById(R.id.p1_gameplay_pts_text));
        mP1GamePlayTextList.add((TextView) findViewById(R.id.p1_tb_pts_text));
        //Initializing the TextView ArrayLists of player 1 : END

        //Initializing the TextView ArrayLists of player 2 : START
        mP2ScoreBoardTextList = new ArrayList<>(mTotalSetsToPlay + 1);
        mP2GamePlayTextList = new ArrayList<>(3); //Size inclusive of Tie breaker

        if (mIsMatchTypeWomen) {
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_total_set_pts)); //0 index
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set1_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set2_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set3_pts));
        } else if (mIsMatchTypeMen) {
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_total_set_pts)); //0 index
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set1_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set2_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set3_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set4_pts));
            mP2ScoreBoardTextList.add((TextView) findViewById(R.id.p2_set5_pts));
        }

        mP2GamePlayTextList.add((TextView) findViewById(R.id.p2_game_pts_text)); //0 index
        mP2GamePlayTextList.add((TextView) findViewById(R.id.p2_gameplay_pts_text));
        mP2GamePlayTextList.add((TextView) findViewById(R.id.p2_tb_pts_text));
        //Initializing the TextView ArrayLists of player 2 : END
    }

    /**
     * Method called when the toggle button
     * for Start/Reset Game is pressed
     *
     * @param view <br/> - The Button view with id 'start_reset_btn'
     */
    public void onStartResetButtonClicked(View view) {

        if (mIsGameStarted) {
            //Button pressed when match was in progress (or got over), to reset/restart the match

            //Clearing Match Type selection
            clearMatchTypeRadioGrp();

            //Enabling Match Type selection
            enableMatchTypeRadioGrp(true);

            //Resetting scores in Scoreboard
            resetScoreBoardTextScores();

            //Resetting scores in Game Play board
            resetGamePlayTextScores();

            //Hiding the Tie breaker layout
            toggleTieBreakerLayout(false);

            //Changing the Text on the Start/Reset Button
            changeStartResetButtonText("Begin Match");

            //Updating Game Play Text
            updateGamePlayText(WELCOME_MSG_STR);

            //defaulting parameters
            mIsGameStarted = false;
            mIsTieBreaker = false;
            mIsDeuceGame = false;
            mIsMatchTypeWomen = false;
            //Resetting scoreboard layout to 5-set : START
            mIsMatchTypeMen = true;
            relayoutMatchTypeScoreboard();
            //Resetting scoreboard layout to 5-set : END
            mIsMatchTypeMen = false;
            mPlayerToServe = 0;

            //defaulting styles for the player ready to serve (to None)
            setActivePlayerAttr();

            //clearing the lists
            mP1ScoreBoardTextList.clear();
            mP1ScoreBoardTextList = null;
            mP1GamePlayTextList.clear();
            mP1GamePlayTextList = null;

            mP2ScoreBoardTextList.clear();
            mP2ScoreBoardTextList = null;
            mP2GamePlayTextList.clear();
            mP2GamePlayTextList = null;

            //disabling the plus buttons
            enablePlusButtons(false);

        } else if (!mIsGameStarted) {
            //Button pressed when match was not started, to begin the match

            if (!mIsMatchTypeMen && !mIsMatchTypeWomen) {
                //When Match Type selection is not yet done, display a suitable message
                updateGamePlayText(NO_MATCH_TYPE_SELECTED_STR);
            } else {
                //When Match Type selection is done

                //Determining the player to serve based on toss
                mPlayerToOpenSet = tossOfTheMatch();
                mPlayerToServe = mPlayerToOpenSet;

                //updating styles for the player ready to serve
                setActivePlayerAttr();

                //updating the Game play text
                updateGamePlayText(String.format(TOSS_MSG_STR, mPlayerToOpenSet));

                //disabling the Match Type Radio button group
                enableMatchTypeRadioGrp(false);

                //Changing the Text on the Start/Reset Button
                changeStartResetButtonText("Reset Match");

                //Initializing the ArrayLists to save the scores
                initScoreArrayLists();

                //Initializing the current Set being played to 1
                mCurrentSetPlay = 1;

                //enabling the plus buttons
                enablePlusButtons(true);

                //Let the games begin
                mIsGameStarted = true;
            }

        }

    }

    /**
     * Method to change the text on the start/reset button view id 'start_reset_btn'
     * to the given text
     *
     * @param buttonTextMsg <br/> - String message to be displayed on the start/reset button
     */
    private void changeStartResetButtonText(String buttonTextMsg) {
        Button startResetButton = (Button) findViewById(R.id.start_reset_btn);
        //Changing the Text on the Start/Reset Button
        startResetButton.setText(buttonTextMsg);
    }

    /**
     * Method to update the Game play text view 'game_play_text'
     *
     * @param message <br/> - String containing the message to be displayed
     * @param append  <br/> - Boolean to append the text to existing text
     *                <br/><b>TRUE</b> - if text needs to be appended
     *                <br/><b>FALSE</b> - otherwise
     */
    private void updateGamePlayText(String message, boolean append) {
        TextView gamePlayTextView = (TextView) findViewById(R.id.game_play_text);
        if (append) {
            //When the text is to be appended to existing text
            gamePlayTextView.setText(gamePlayTextView.getText().toString() + "\n" + message);
        } else {
            //When the text need not be appended to existing text
            gamePlayTextView.setText(message);
        }
    }

    /**
     * Overloaded method to update the Game play text view 'game_play_text' without
     * appending to existing text
     *
     * @param message <br/> - String containing the message to be displayed
     */
    private void updateGamePlayText(String message) {
        updateGamePlayText(message, false);
    }

    /**
     * Method of randomness for deciding the player to open the match
     *
     * @return Integer of the player to open the match
     */
    private int tossOfTheMatch() {
        Random random = new Random();
        int randInt = random.nextInt(10) + 1; //Generating random number from 1 - 10
        return (randInt % 2 + 1); //Returning either 1 or 2
    }

    /**
     * Method called when the Radio buttons for
     * Match Type selection is done by the user
     *
     * @param view <br/> - The Radio Button views with ids 'mens_rbtn' & 'womens_rbtn'
     */
    public void onMatchTypeRadioButtonClicked(View view) {

        //Is the match type selected
        boolean isChecked = ((RadioButton) view).isChecked();

        //Checking the match type selection done
        switch (view.getId()) {
            case R.id.mens_rbtn:
                if (isChecked) {
                    mIsMatchTypeMen = true;
                    mIsMatchTypeWomen = false;
                    mTotalSetsToPlay = 5;
                    mTotalSetsToWin = (mTotalSetsToPlay + 1) / 2;
                }
                break;
            case R.id.womens_rbtn:
                if (isChecked) {
                    mIsMatchTypeWomen = true;
                    mIsMatchTypeMen = false;
                    mTotalSetsToPlay = 3;
                    mTotalSetsToWin = (mTotalSetsToPlay + 1) / 2;
                }
                break;
        }

        //Calling method to relayout the scoreboard based on match type selection
        relayoutMatchTypeScoreboard();
    }

    /**
     * Method to enable/disable the Match Type Radio Button Group
     *
     * @param enabled <br/> - Boolean to enable/disable the Match Type Radio Button Group
     *                <br/><b>TRUE</b> - to enable
     *                <br/><b>FALSE</b> - to disable
     */
    private void enableMatchTypeRadioGrp(boolean enabled) {
        RadioButton mensRadioButton = (RadioButton) findViewById(R.id.mens_rbtn);
        RadioButton womensRadioButton = (RadioButton) findViewById(R.id.womens_rbtn);

        mensRadioButton.setEnabled(enabled);
        womensRadioButton.setEnabled(enabled);
    }

    /**
     * Method to clear the Match Type Radio Button selection
     */
    private void clearMatchTypeRadioGrp() {
        RadioGroup matchTypeRadioGroup = (RadioGroup) findViewById(R.id.match_type_rbtn_grp);
        matchTypeRadioGroup.clearCheck();
    }

    /**
     * Method to relayout the Scoreboard based on match type selection
     */
    private void relayoutMatchTypeScoreboard() {

        TableLayout scoreboardTableLayout = (TableLayout) findViewById(R.id.scoreboard_table);

        if (mIsMatchTypeWomen) {
            //When it is women's match, set 4 & 5 are not present
            //Hence hiding the corresponding columns

            scoreboardTableLayout.setColumnCollapsed(5, true);
            scoreboardTableLayout.setColumnCollapsed(6, true);

        } else if (mIsMatchTypeMen) {
            //When it is men's match, set 4 & 5 are actually played
            //Hence unhiding the corresponding columns

            scoreboardTableLayout.setColumnCollapsed(5, false);
            scoreboardTableLayout.setColumnCollapsed(6, false);

        }

    }

    /**
     * Method to change the color/style attributes of a player who is currently serving
     */
    private void setActivePlayerAttr() {

        if (mPlayerToServe == 1) {
            //When Player 1 is serving

            //Update styles for Player 1 : START
            TextView p1ScoreBoardText = (TextView) findViewById(R.id.player1_row_0);
            p1ScoreBoardText.setTypeface(Typeface.create(p1ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.BOLD);

            TextView p1GamePlayHdrText = (TextView) findViewById(R.id.p1_gameplay_hdr_text);
            p1GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.activePlayerColor));

            View p1GamePlayHdrLineView = findViewById(R.id.p1_gameplay_hdr_line);
            p1GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.activePlayerColor));
            //Update styles for Player 1 : END


            //Reset styles for Player 2 : START
            TextView p2ScoreBoardText = (TextView) findViewById(R.id.player2_row_1);
            p2ScoreBoardText.setTypeface(Typeface.create(p2ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.NORMAL);

            TextView p2GamePlayHdrText = (TextView) findViewById(R.id.p2_gameplay_hdr_text);
            p2GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.p2GamePlayHdrTextColor));

            View p2GamePlayHdrLineView = findViewById(R.id.p2_gameplay_hdr_line);
            p2GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.p2RowColorBackground));
            //Reset styles for Player 2 : END

        } else if (mPlayerToServe == 2) {
            //When Player 2 is serving

            //Update styles for Player 2 : START
            TextView p2ScoreBoardText = (TextView) findViewById(R.id.player2_row_1);
            p2ScoreBoardText.setTypeface(Typeface.create(p2ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.BOLD);

            TextView p2GamePlayHdrText = (TextView) findViewById(R.id.p2_gameplay_hdr_text);
            p2GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.activePlayerColor));

            View p2GamePlayHdrLineView = findViewById(R.id.p2_gameplay_hdr_line);
            p2GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.activePlayerColor));
            //Update styles for Player 2 : END

            //Reset styles for Player 1 : START
            TextView p1ScoreBoardText = (TextView) findViewById(R.id.player1_row_0);
            p1ScoreBoardText.setTypeface(Typeface.create(p1ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.NORMAL);

            TextView p1GamePlayHdrText = (TextView) findViewById(R.id.p1_gameplay_hdr_text);
            p1GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.p1GamePlayHdrTextColor));

            View p1GamePlayHdrLineView = findViewById(R.id.p1_gameplay_hdr_line);
            p1GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.p1RowColorBackground));
            //Reset styles for Player 1 : END

        } else if (mPlayerToServe == 0) {
            //To completely reset to defaults

            //Reset styles for Player 1 : START
            TextView p1ScoreBoardText = (TextView) findViewById(R.id.player1_row_0);
            p1ScoreBoardText.setTypeface(Typeface.create(p1ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.NORMAL);

            TextView p1GamePlayHdrText = (TextView) findViewById(R.id.p1_gameplay_hdr_text);
            p1GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.p1GamePlayHdrTextColor));

            View p1GamePlayHdrLineView = findViewById(R.id.p1_gameplay_hdr_line);
            p1GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.p1RowColorBackground));
            //Reset styles for Player 1 : END

            //Reset styles for Player 2 : START
            TextView p2ScoreBoardText = (TextView) findViewById(R.id.player2_row_1);
            p2ScoreBoardText.setTypeface(Typeface.create(p2ScoreBoardText.getTypeface(), Typeface.NORMAL), Typeface.NORMAL);

            TextView p2GamePlayHdrText = (TextView) findViewById(R.id.p2_gameplay_hdr_text);
            p2GamePlayHdrText.setTextColor(ContextCompat.getColor(this, R.color.p2GamePlayHdrTextColor));

            View p2GamePlayHdrLineView = findViewById(R.id.p2_gameplay_hdr_line);
            p2GamePlayHdrLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.p2RowColorBackground));
            //Reset styles for Player 2 : END

        }

    }

    /**
     * Method to increment and return the next Game Play point
     *
     * @param currentPointStr <br/> - Current Game Play point in String
     * @return String containing the next Score in Game Play
     */
    private String getNextGamePlayPoint(String currentPointStr) {

        if (currentPointStr.equals("0")) {
            return "15";
        } else if (currentPointStr.equals("15")) {
            return "30";
        } else if (currentPointStr.equals("30")) {
            return "40";
        } else if (currentPointStr.equals("40") && mIsDeuceGame) {
            return "Ad";
        } else if (currentPointStr.equals("40") && !mIsDeuceGame) {
            return "60";
        } else if (currentPointStr.equals("Ad")) {
            mIsDeuceGame = false; //Updating to false as Advantage is won by the player
            return "60";
        }

        return "0"; //Returning 0 by default
    }

    /**
     * Method called when one of the plus buttons 'p1_plus_btn' or 'p2_plus_btn' is clicked to update the
     * corresponding player's scores
     *
     * @param view <br/> - Plus Button Views 'p1_plus_btn' or 'p2_plus_btn'
     */
    public void onPlusButtonClicked(View view) {

        if (!mIsTieBreaker) {
            //When the current game is not a Tie Breaker game

            TextView p1GamePlayTextView = mP1GamePlayTextList.get(1);
            //Retrieving the Current Game Play point of Player 1
            String p1CurrentGamePlayPoint = p1GamePlayTextView.getText().toString();

            TextView p2GamePlayTextView = mP2GamePlayTextList.get(1);
            //Retrieving the Current Game Play point of Player 2
            String p2CurrentGamePlayPoint = p2GamePlayTextView.getText().toString();

            switch (view.getId()) {
                case R.id.p1_plus_btn:
                    String p1NextPointStr = getNextGamePlayPoint(p1CurrentGamePlayPoint);
                    if (p1NextPointStr.equals("60")) {
                        //If the player reaches 60 Game Play point, then the player wins the Game

                        //Resetting Game Play points to 0 for both players
                        p1GamePlayTextView.setText("0");
                        p2GamePlayTextView.setText("0");

                        //Highlighting the player who serves next
                        mPlayerToServe = (mPlayerToServe % 2) + 1;
                        setActivePlayerAttr();

                        //Calling method to update the Game point for Player 1
                        updateGamePoints(1);

                    } else if (p1NextPointStr.equals("40")
                            && p1NextPointStr.equals(p2CurrentGamePlayPoint)) {
                        //If both players are at 40 each, then it is Deuce

                        //enabling the Deuce flag
                        mIsDeuceGame = true;

                        //Updating the Game Play point for Player 1
                        p1GamePlayTextView.setText(p1NextPointStr);

                        //Updating the Game play text
                        updateGamePlayText(String.format(DEUCE_MSG_STR, mPlayerToServe, mCurrentSetPlay));

                    } else if (p1NextPointStr.equals("Ad")
                            && p1NextPointStr.equals(p2CurrentGamePlayPoint)) {
                        //If Player 1 gains Advantage when Player 2 was in Advantage, then it is back to Deuce

                        //Resetting Game Play points to 40 for both players
                        p1GamePlayTextView.setText("40");
                        p2GamePlayTextView.setText("40");

                        //Updating the Game play text
                        updateGamePlayText(String.format(DEUCE_REPEAT_MSG_STR, mPlayerToServe, mCurrentSetPlay));

                    } else {
                        //In other cases, just update the Game Play point for Player 1
                        p1GamePlayTextView.setText(p1NextPointStr);

                        //Updating the Game play text
                        if (p1NextPointStr.equals("Ad")) {
                            //If Player 1 wins the Advantage
                            updateGamePlayText(String.format(ADV_PLAYER_MSG_STR, mPlayerToServe, mCurrentSetPlay, 1));
                        } else {
                            //For all other points
                            if (mPlayerToServe == 1) {
                                updateGamePlayText(String.format(GAMEPLAY_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay, p1NextPointStr, (p2CurrentGamePlayPoint.equals("0")) ? "love" : p2CurrentGamePlayPoint));
                            } else if (mPlayerToServe == 2) {
                                updateGamePlayText(String.format(GAMEPLAY_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay, (p2CurrentGamePlayPoint.equals("0")) ? "love" : p2CurrentGamePlayPoint, p1NextPointStr));
                            }
                        }
                    }
                    break;

                case R.id.p2_plus_btn:
                    String p2NextPointStr = getNextGamePlayPoint(p2CurrentGamePlayPoint);
                    if (p2NextPointStr.equals("60")) {
                        //If the player reaches 60 Game Play point, then the player wins the Game

                        //Resetting Game Play points to 0 for both players
                        p2GamePlayTextView.setText("0");
                        p1GamePlayTextView.setText("0");

                        //Highlighting the player who serves next
                        mPlayerToServe = (mPlayerToServe % 2) + 1;
                        setActivePlayerAttr();

                        //Calling method to update the Game point for Player 2
                        updateGamePoints(2);

                    } else if (p2NextPointStr.equals("40")
                            && p2NextPointStr.equals(p1CurrentGamePlayPoint)) {
                        //If both players are at 40 each, then it is Deuce

                        //enabling the Deuce flag
                        mIsDeuceGame = true;

                        //Updating the Game Play point for Player 2
                        p2GamePlayTextView.setText(p2NextPointStr);

                        //Updating the Game play text
                        updateGamePlayText(String.format(DEUCE_MSG_STR, mPlayerToServe, mCurrentSetPlay));

                    } else if (p2NextPointStr.equals("Ad")
                            && p2NextPointStr.equals(p1CurrentGamePlayPoint)) {
                        //If Player 2 gains Advantage when Player 1 was in Advantage, then it is back to Deuce

                        //Resetting Game Play points to 40 for both players
                        p2GamePlayTextView.setText("40");
                        p1GamePlayTextView.setText("40");

                        //updating the Game play text
                        updateGamePlayText(String.format(DEUCE_REPEAT_MSG_STR, mPlayerToServe, mCurrentSetPlay));

                    } else {
                        //In other cases, just update the Game Play point for Player 2
                        p2GamePlayTextView.setText(p2NextPointStr);

                        //Updating the Game play text
                        if (p2NextPointStr.equals("Ad")) {
                            //If Player 2 wins the Advantage
                            updateGamePlayText(String.format(ADV_PLAYER_MSG_STR, mPlayerToServe, mCurrentSetPlay, 2));
                        } else {
                            //For all other points
                            if (mPlayerToServe == 2) {
                                updateGamePlayText(String.format(GAMEPLAY_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay, p2NextPointStr, (p1CurrentGamePlayPoint.equals("0")) ? "love" : p1CurrentGamePlayPoint));
                            } else if (mPlayerToServe == 1) {
                                updateGamePlayText(String.format(GAMEPLAY_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay, (p1CurrentGamePlayPoint.equals("0")) ? "love" : p1CurrentGamePlayPoint, p2NextPointStr));
                            }
                        }
                    }
                    break;

            }

        } else if (mIsTieBreaker) {
            //When the current game is a Tie Breaker game

            TextView p1GamePointTextView = mP1GamePlayTextList.get(0);
            //Retrieving the current Game point of Player 1
            int p1GamePoints = Integer.parseInt(p1GamePointTextView.getText().toString());

            TextView p2GamePointTextView = mP2GamePlayTextList.get(0);
            //Retrieving the current Game point of Player 2
            int p2GamePoints = Integer.parseInt(p2GamePointTextView.getText().toString());

            TextView p1GamePlayTextView = mP1GamePlayTextList.get(2);
            //Retrieving the Current Tie Breaker point of Player 1
            int p1TieBreakerPoints = Integer.parseInt(p1GamePlayTextView.getText().toString());

            TextView p2GamePlayTextView = mP2GamePlayTextList.get(2);
            //Retrieving the Current Tie Breaker point of Player 2
            int p2TieBreakerPoints = Integer.parseInt(p2GamePlayTextView.getText().toString());

            //Updating the corresponding Player's Tie Breaker point : START
            TextView currentSetP1ScoreboardTextView = mP1ScoreBoardTextList.get(mCurrentSetPlay);
            TextView currentSetP2ScoreboardTextView = mP2ScoreBoardTextList.get(mCurrentSetPlay);
            switch (view.getId()) {
                case R.id.p1_plus_btn:
                    p1TieBreakerPoints += 1;
                    p1GamePlayTextView.setText(String.valueOf(p1TieBreakerPoints));
                    //Updating Scoreboard of Player - 1
                    currentSetP1ScoreboardTextView.setText(String.format("%d(%d)", p1GamePoints, p1TieBreakerPoints));
                    break;
                case R.id.p2_plus_btn:
                    p2TieBreakerPoints += 1;
                    p2GamePlayTextView.setText(String.valueOf(p2TieBreakerPoints));
                    //Updating Scoreboard of Player - 2
                    currentSetP2ScoreboardTextView.setText(String.format("%d(%d)", p2GamePoints, p2TieBreakerPoints));
                    break;
            }
            //Updating the corresponding Player's Tie Breaker point : END

            //Evaluating current total tie breaker points reached in this game
            int totalPointsReached = p1TieBreakerPoints + p2TieBreakerPoints;

            //Highlighting the player who serves next
            if ((totalPointsReached - 1) % 2 == 0) {
                mPlayerToServe = (mPlayerToServe % 2) + 1;
                setActivePlayerAttr();
            }

            //Updating the Game play text
            if (mPlayerToServe == 1) {
                updateGamePlayText(String.format(TIEBREAKER_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay,
                        (p1TieBreakerPoints == 0) ? "love" : String.valueOf(p1TieBreakerPoints), (p2TieBreakerPoints == 0) ? "love" : String.valueOf(p2TieBreakerPoints)));
            } else if (mPlayerToServe == 2) {
                updateGamePlayText(String.format(TIEBREAKER_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay,
                        (p2TieBreakerPoints == 0) ? "love" : String.valueOf(p2TieBreakerPoints), (p1TieBreakerPoints == 0) ? "love" : String.valueOf(p1TieBreakerPoints)));
            }

            //Evaluating the difference in Tie Breaker points of the current game between the players
            int tieBreakerPointDifference = Math.abs(p1TieBreakerPoints - p2TieBreakerPoints);
            //Evaluating the Max points reached by either of the player in the Set
            int maxPointsReached = Math.max(p1TieBreakerPoints, p2TieBreakerPoints);

            if (maxPointsReached >= 7 && tieBreakerPointDifference >= 2) {
                //The player who reaches 7 tiebreaker points or higher
                //with the minimum point difference of 2, wins the Set

                if (p1TieBreakerPoints == maxPointsReached) {
                    //If player 1 has reached the max points, then player 1 wins the set
                    //Updating Game Points of Player - 1
                    p1GamePointTextView.setText(String.valueOf(++p1GamePoints));
                    //Updating Scoreboard of Player - 1
                    currentSetP1ScoreboardTextView.setText(String.format("%d(%d)", p1GamePoints, p1TieBreakerPoints));
                    //Updating Set Points of Player - 1
                    updateSetPoints(1);

                } else if (p2TieBreakerPoints == maxPointsReached) {
                    //If player 2 has reached the max points, then player 2 wins the set
                    //Updating Game Points of Player - 2
                    p2GamePointTextView.setText(String.valueOf(++p2GamePoints));
                    //Updating Scoreboard of Player - 2
                    currentSetP2ScoreboardTextView.setText(String.format("%d(%d)", p2GamePoints, p2TieBreakerPoints));
                    //Updating Set Points of Player - 2
                    updateSetPoints(2);
                }

            }

        }

    }

    /**
     * Method to update the Game Points for the Player
     *
     * @param player <br/> - Integer number of the Player whose Game Point is to be updated
     *               <br/> 1 for Player-1
     *               <br/> 2 for Player-2
     */
    private void updateGamePoints(int player) {
        TextView p1GamePointTextView = mP1GamePlayTextList.get(0);
        //Retrieving the current Game point of Player 1
        int p1GamePoints = Integer.parseInt(p1GamePointTextView.getText().toString());

        TextView p2GamePointTextView = mP2GamePlayTextList.get(0);
        //Retrieving the current Game point of Player 2
        int p2GamePoints = Integer.parseInt(p2GamePointTextView.getText().toString());

        //Updating current Game scores on the Scoreboard & Game play board : START
        if (player == 1) {
            p1GamePoints += 1;
            p1GamePointTextView.setText(String.valueOf(p1GamePoints));

            TextView currentSetP1ScoreboardTextView = mP1ScoreBoardTextList.get(mCurrentSetPlay);
            currentSetP1ScoreboardTextView.setText(String.valueOf(p1GamePoints));

        } else if (player == 2) {
            p2GamePoints += 1;
            p2GamePointTextView.setText(String.valueOf(p2GamePoints));

            TextView currentSetP2ScoreboardTextView = mP2ScoreBoardTextList.get(mCurrentSetPlay);
            currentSetP2ScoreboardTextView.setText(String.valueOf(p2GamePoints));
        }
        //Updating current Game scores on the Scoreboard & Game play board : END

        //Updating the Game play text
        if (mPlayerToServe == 1) {
            updateGamePlayText(String.format(GAME_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay,
                    (p1GamePoints == 0) ? "love" : String.valueOf(p1GamePoints), (p2GamePoints == 0) ? "love" : String.valueOf(p2GamePoints)));
        } else if (mPlayerToServe == 2) {
            updateGamePlayText(String.format(GAME_POINT_MSG_STR, mPlayerToServe, mCurrentSetPlay,
                    (p2GamePoints == 0) ? "love" : String.valueOf(p2GamePoints), (p1GamePoints == 0) ? "love" : String.valueOf(p1GamePoints)));
        }

        //Evaluating the difference in Game points of the current set between the players
        int gamePointDifference = Math.abs(p1GamePoints - p2GamePoints);
        //Evaluating the Max points reached by either of the player in the Set
        int maxPointsReached = Math.max(p1GamePoints, p2GamePoints);

        //Validating for Set point/Tie breaker : START
        if (mCurrentSetPlay != mTotalSetsToPlay) {
            //When the current Set is not the last Set of the Match

            if (maxPointsReached >= 6 && gamePointDifference >= 2) {
                //When the max points reached by either of the player is greater than or equal to 6
                //with the Game point difference of 2 or greater, then that player wins the set

                if (p1GamePoints == maxPointsReached) {
                    //If player 1 has reached the max points, then player 1 wins the set
                    updateSetPoints(1);
                } else if (p2GamePoints == maxPointsReached) {
                    //If player 2 has reached the max points, then player 2 wins the set
                    updateSetPoints(2);
                }

            } else if (maxPointsReached == 6 && gamePointDifference == 0) {
                //When the max points reached by both the players is 6
                //with the Game point difference of 0, then the players enter the tie breaker game

                mIsTieBreaker = true; //Setting the flag as we are entering tie breaker
                //Enabling the Tie Breaker Score layout
                toggleTieBreakerLayout(true);

                //Updating the Game play text
                updateGamePlayText(String.format(TIEBREAKER_START_MSG_STR, mCurrentSetPlay, mPlayerToServe), true);
            }


        } else if (mCurrentSetPlay == mTotalSetsToPlay) {
            //When the current Set is the last Set of the Match (Advantage Set)

            if (maxPointsReached >= 6 && gamePointDifference >= 2) {
                //When the max points reached by either of the player is greater than or equal to 6
                //with the Game point difference of 2 or greater, then that player wins the set

                if (p1GamePoints == maxPointsReached) {
                    //If player 1 has reached the max points, then player 1 wins the set
                    updateSetPoints(1);
                } else if (p2GamePoints == maxPointsReached) {
                    //If player 2 has reached the max points, then player 2 wins the set
                    updateSetPoints(2);
                }

            }

        }
        //Validating for Set point/Tie breaker : END

    }

    /**
     * Method to update the Set points for the player
     *
     * @param player <br/> - Integer number of the Player whose Set Point is to be updated
     *               <br/> 1 for Player-1
     *               <br/> 2 for Player-2
     */
    private void updateSetPoints(int player) {
        TextView p1SetPtsScoreboardTextView = mP1ScoreBoardTextList.get(0);
        //Retrieving the current total Set points of Player 1
        int p1TotalSetPts = Integer.parseInt(p1SetPtsScoreboardTextView.getText().toString());

        TextView p2SetPtsScoreboardTextView = mP2ScoreBoardTextList.get(0);
        //Retrieving the current total Set points of Player 2
        int p2TotalSetPts = Integer.parseInt(p2SetPtsScoreboardTextView.getText().toString());

        //Updating Set points on the Scoreboard : START
        if (player == 1) {
            p1SetPtsScoreboardTextView.setText(String.valueOf(++p1TotalSetPts));
        } else if (player == 2) {
            p2SetPtsScoreboardTextView.setText(String.valueOf(++p2TotalSetPts));
        }
        //Updating Set points on the Scoreboard : END

        //Retrieving the current Game and Tiebreaker points(if any): START
        //Retrieving Player 1 points
        TextView currentSetP1ScoreboardTextView = mP1ScoreBoardTextList.get(mCurrentSetPlay);
        String p1GamePointStr = currentSetP1ScoreboardTextView.getText().toString();

        //Retrieving Player 2 points
        TextView currentSetP2ScoreboardTextView = mP2ScoreBoardTextList.get(mCurrentSetPlay);
        String p2GamePointStr = currentSetP2ScoreboardTextView.getText().toString();
        //Retrieving the current Game and Tiebreaker points(if any) : END

        //Updating the Game Play text
        if (player == 1) {
            //When Player 1 wins the Set
            updateGamePlayText(String.format(SET_POINT_MSG_STR, mCurrentSetPlay, player, p1GamePointStr, p2GamePointStr), true);
        } else if (player == 2) {
            //When Player 2 wins the Set
            updateGamePlayText(String.format(SET_POINT_MSG_STR, mCurrentSetPlay, player, p2GamePointStr, p1GamePointStr), true);
        }

        //Evaluating the Max Set points reached by either of the player in the Match
        int maxPointsReached = Math.max(p1TotalSetPts, p2TotalSetPts);

        if (maxPointsReached == mTotalSetsToWin) {
            //If deciding number of sets has been won, then the match is over

            //Updating the Game Play text
            if (p1TotalSetPts == maxPointsReached) {
                //When Player -1 has won the match

                updateGamePlayText(String.format(MATCH_POINT_MSG_STR, 1, 2, getMatchScore(1)), true);

            } else if (p2TotalSetPts == maxPointsReached) {
                //When Player -2 has won the match

                updateGamePlayText(String.format(MATCH_POINT_MSG_STR, 2, 1, getMatchScore(2)), true);
            }

            //Disabling the plus buttons as the Match is over
            enablePlusButtons(false);

            //Changing the Text on the Start/Reset Button
            changeStartResetButtonText("Restart Match");

        } else if (maxPointsReached < mTotalSetsToWin) {
            //If deciding number of sets has not been won yet, then advance to next set

            //Resetting scores to 0 for the next Set
            resetGamePlayTextScores();

            if (mIsTieBreaker) {
                //When tiebreaker was enabled, turn it off for next Set

                mIsTieBreaker = false;
                toggleTieBreakerLayout(mIsTieBreaker);

            }

            //Updating current Play of Set to Next Set
            mCurrentSetPlay += 1;

            //Updating the Player to Serve the Next Set
            mPlayerToOpenSet = (mPlayerToOpenSet % 2) + 1;
            mPlayerToServe = mPlayerToOpenSet;

            //Highlighting the player who serves next
            setActivePlayerAttr();

            //Updating the Game Play text for Next Set
            if (mPlayerToServe == 1) {
                updateGamePlayText(String.format(NEXT_SET_MSG_STR, mPlayerToServe,
                        (p1TotalSetPts == 0) ? "love" : String.valueOf(p1TotalSetPts), (p2TotalSetPts == 0) ? "love" : String.valueOf(p2TotalSetPts)), true);
            } else if (mPlayerToServe == 2) {
                updateGamePlayText(String.format(NEXT_SET_MSG_STR, mPlayerToServe,
                        (p2TotalSetPts == 0) ? "love" : String.valueOf(p2TotalSetPts), (p1TotalSetPts == 0) ? "love" : String.valueOf(p1TotalSetPts)), true);
            }
        }

    }


    /**
     * This method resets the Scores on the Game play board to 0
     * Used for advancing to Next Set (or) when resetting the entire Match
     */
    private void resetGamePlayTextScores() {

        //Resetting Player - 1 Game play scores
        for (TextView gamePlayTextView : mP1GamePlayTextList) {
            gamePlayTextView.setText(String.valueOf(0));
        }

        //Resetting Player - 2 Game play scores
        for (TextView gamePlayTextView : mP2GamePlayTextList) {
            gamePlayTextView.setText(String.valueOf(0));
        }

    }

    /**
     * This method resets the Scores on the Scoreboard to 0.
     * Used when resetting the entire Match
     */
    private void resetScoreBoardTextScores() {

        //Resetting Player - 1 Scoreboard scores
        for (TextView scoreboardTextView : mP1ScoreBoardTextList) {
            scoreboardTextView.setText(String.valueOf(0));
        }

        //Resetting Player - 2 Scoreboard scores
        for (TextView scoreboardTextView : mP2ScoreBoardTextList) {
            scoreboardTextView.setText(String.valueOf(0));
        }

    }

    /**
     * Method that prepares and returns the Match Score in the end
     *
     * @param player <br/> - Integer number of the Player who won the Match
     *               <br/> 1 for Player-1
     *               <br/> 2 for Player-2
     * @return String containing the match score with respect to the player who won
     */
    private String getMatchScore(int player) {
        StringBuilder scoreBuilder = new StringBuilder();

        for (int index = 1; index <= mCurrentSetPlay; index++) {
            TextView p1CurrentIndexSetTextView = mP1ScoreBoardTextList.get(index);
            TextView p2CurrentIndexSetTextView = mP2ScoreBoardTextList.get(index);

            if (player == 1) {
                //When Player 1 has won the Match
                scoreBuilder.append(String.format("%s - %s", p1CurrentIndexSetTextView.getText().toString(), p2CurrentIndexSetTextView.getText().toString()));
            } else if (player == 2) {
                //When Player 2 has won the Match
                scoreBuilder.append(String.format("%s - %s", p2CurrentIndexSetTextView.getText().toString(), p1CurrentIndexSetTextView.getText().toString()));
            }

            if (index < mCurrentSetPlay) {
                //Appending comma separator after each set scores
                scoreBuilder.append(", ");
            }
        }

        return scoreBuilder.toString();
    }

}