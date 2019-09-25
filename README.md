# Tennis Scoring - Score Keeper App

![GitHub](https://img.shields.io/github/license/kaushiknsanji/Tennis_Score_Keeper_Udacity)  ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kaushiknsanji/Tennis_Score_Keeper_Udacity)  ![GitHub repo size](https://img.shields.io/github/repo-size/kaushiknsanji/Tennis_Score_Keeper_Udacity)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/kaushiknsanji/Tennis_Score_Keeper_Udacity)  ![GitHub All Releases](https://img.shields.io/github/downloads/kaushiknsanji/Tennis_Score_Keeper_Udacity/total) ![GitHub search hit counter](https://img.shields.io/github/search/kaushiknsanji/Tennis_Score_Keeper_Udacity/Score%20Keeper%20App) ![Minimum API level](https://img.shields.io/badge/API-15-yellow)

This App has been developed as part of the **Udacity Android Basics Nanodegree Course** for the Exercise Project **"Score Keeper App"**. **Tennis** is the game chosen for the App.

---

## App Compatibility

Android device running with Android OS 4.0.4 (API Level 15) or above. Designed for Phones and NOT for Tablets.

---

## Rubric followed for the Project

* The chosen game has either multiple amounts of points that can be scored or multiple important metrics to track.
* App is divided into two columns, one for each team.
* Each column has a large TextView to keep track of the current score of the Team, and an optional secondary TextView to track other important metrics.
* Each column has Scoring Buttons to record the score for the team, which updates a corresponding TextView in that column to reflect the new score.
* Game has a Reset Button to reset the scores on the TextViews.

---

## Design and Implementation of the App

The Score Keeper App is structured for the **Tennis Scoring Game** based on the rules followed in the Grand Slams for Men's and Women's Tennis. It contains only one Activity, i.e., [MainActivity](/app/src/main/java/com/example/kaushiknsanji/tennisscoring/MainActivity.java) which displays the Scoreboards for the Tennis Match and manages/tracks the scoring for each Player.

<img src="https://user-images.githubusercontent.com/26028981/65620659-d96cd480-dfdf-11e9-9346-9158821351e6.png" width="40%"/>  <img src="https://user-images.githubusercontent.com/26028981/65620663-da9e0180-dfdf-11e9-9c95-7e952384db47.png" width="40%"/>

### Match Type Selection

|Match Type - Men|Match Type - Women|
|---|---|
|![Match_Type_Men](https://user-images.githubusercontent.com/26028981/65620681-e4c00000-dfdf-11e9-850d-a06c967cde37.png)|![Match_Type_Women](https://user-images.githubusercontent.com/26028981/65620684-e5f12d00-dfdf-11e9-8135-917000eaf5c6.png)|

* At the top, we have a RadioButton option for selecting the Type of Tennis Match, i.e., **Men's Match** or **Women's Match**. 
* **Men's Match** will be the best of **5 sets**, while **Women's Match** is the best of **3 Sets**.
* If the Match goes to the deciding Set (Last Set), then there will be no Tie-Breaker for that Set.

### Players' Scoreboard

<img src="https://user-images.githubusercontent.com/26028981/65620698-ec7fa480-dfdf-11e9-9294-c50eb9ac1fd6.png" width="40%"/>

Below the Match Type RadioButton option is the Players' Scoreboard which keeps track of the Game points in each Set and the number of Sets won by each Player. 

### Scoring in an Ongoing Set

|Scoring in a Set|Scoring in a Tie-Breaker|
|---|---|
|![Intermediate_GamePlay_Score](https://user-images.githubusercontent.com/26028981/65620698-ec7fa480-dfdf-11e9-9294-c50eb9ac1fd6.png)|![Intermediate_TieBreaker_Score](https://user-images.githubusercontent.com/26028981/65620704-ee496800-dfdf-11e9-8b40-947d757558ef.png)|

* Below the Players' Scoreboard, is a table of two columns, one for each Player, that displays the various points in an Ongoing Set. 
* Points in an Ongoing Game (labelled as **GamePlay Points**), **Game Points** in the Ongoing Set and the **Tie Break Points** in a Tie-Breaker are recorded and displayed here. 
* Each Player column will have a **"+"** button that increases the Points in the Ongoing Game for the corresponding Player, which in turn updates the Game Points in the Set for the corresponding Player adhering to the Tennis scoring rules. 
* Tie-Breaker will start only when there is a tie in the Game Points for each Player at **"Game Point 6"** in the Set, to decide the winner of the Set, which will be the Player that reaches 7+ Tie-Breaker Points with a difference of 2, as per the rules of Tennis.

### The Start/Reset/Restart Button

|Begin Match|Reset Match|Restart Match|
|---|---|---|
|![Initial_Portrait_2](https://user-images.githubusercontent.com/26028981/65620663-da9e0180-dfdf-11e9-9c95-7e952384db47.png)|![Intermediate_GamePlay_Score](https://user-images.githubusercontent.com/26028981/65620698-ec7fa480-dfdf-11e9-9294-c50eb9ac1fd6.png)|![Match_finish](https://user-images.githubusercontent.com/26028981/65620733-fbfeed80-dfdf-11e9-9098-4597a1a96ad3.png)|

The last Button at the bottom, allows for - 
* Starting a Match, when it displays **Begin Match**. This also does the Toss and decides which Player will Serve (on Random basis).
* Resetting the Match in between the play in order to restart play, when it displays **Reset Match**.
* Restarting a Match after a Match has finished, when it displays **Restart Match**.

### TextView for Announcements

|Help message|Score Announcement|
|---|---|
|![Initial_Portrait_2](https://user-images.githubusercontent.com/26028981/65620663-da9e0180-dfdf-11e9-9c95-7e952384db47.png)|![Score_Announcement](https://user-images.githubusercontent.com/26028981/65620719-f4d7df80-dfdf-11e9-9415-4744c525cede.png)|

In between the Start/Reset/Restart Button and the Players' `+` Buttons, there is a TextView that displays **"Help messages"** and **"Announcements of Scores"** just like in Tennis.

---

## Branches in this Repository

* **[master](https://github.com/kaushiknsanji/Tennis_Score_Keeper_Udacity/tree/master)**
	* Contains the code submitted for review, along with review suggestions incorporated.
* **[release_v1.0](https://github.com/kaushiknsanji/Tennis_Score_Keeper_Udacity/tree/release_v1.0)**
	* Fixed TextView size for Game-Play and Tie-Breaker Points.
	* Game-Play Points hidden during a Tie-Breaker since it is not required.
	* Added App Icon.
	* Other minor changes to prepare the app for local release. 

---

## Icon credits

App Icon is from [Icons8](https://icons8.com).

---

## Review from the Reviewer (Udacity)

![Review_Score_Keeper_App](https://user-images.githubusercontent.com/26028981/65620779-15a03500-dfe0-11e9-87c8-83821c872914.PNG)

---

## License

```
Copyright 2017 Kaushik N. Sanji

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
