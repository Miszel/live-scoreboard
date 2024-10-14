# Live Football World Cup ScoreBoard Library
The Live Football World Cup ScoreBoard library allows you to manage and track live football matches. It provides functionality to start, update, and finish matches, as well as generate a summary of ongoing matches ordered by their total score.

## How to Build the JAR
Run the following command to build the JAR:
```bash
gradle build
```
or
```bash
./gradlew build
```
The JAR file will be generated in the `build/libs`

## How to Run Tests
```bash
./gradlew test
```

## Contact
For any questions, please contact:
- Michał Kowalik 
- Email: michal.kowalik.82@gmail.com

## Assumptions
- ~~The main assumption is that summary which is returned from the library is a formatted string. It is not stated in the 
  task description, but that was my original understanding.~~
- Specification describes score update operation in the way that it takes pair of absolute scores. To differentiate 
one match from another we need to assume that there is additional input information. It could be a pair of team names.
- If two teams started the match, and it is not finished another start operation will result in an exception and 
  operation will be ignored
- Scoreboard is not tracking if score changes makes sense, update gives absolute result, so it can be made on real 
  score change, or it could be score correction
- Goals should be 0 or positive integer number
- Tested scenarios are not very complex so initial setup for particular tests can be done as a sequence of operations
- Match which is not started cannot be updated

## To be considered
- could a match be created if both home and away team has the same name?
- what if board formatting will change? could we reduce impact which it could have on tests?
  - this could be resolved by refactoring tests to test values from collection of match data. There could be 
    additional test which tests formatting of result [DONE]
- team name should not be null
- possibility to provide external repository implementation [DONE]
- custom Exceptions
- trim team names
- test if name is empty string

## Specification by example
1. After adding a match, it is visible on scoreboard and is preceded by index [DONE]
   - start(Sweden, Norway) 
   - get()
   - result: \
     `1. Sweden 0 - Norway 0`
2. Existing score can be updated [DONE]
   - start(Sweden, Norway)
   - update(Sweden, Norway, 1, 0)
   - get()
   - result: \
     `1. Sweden 1 - Norway 0`
3. Match which is not started cannot be updated [DONE]
    - start(Sweden, Norway)
    - update(Denmark, Belgium, 0, 1)
    - result: exception
    - get()
    - result: \
      `1. Sweden 0 - Norway 0`
4. Match cannot be started multiple times [DONE]
   - start(Sweden, Norway)
   - start(Sweden, Norway)
   result: exception is thrown
   - get()
   - result: \
     `1. Sweden 0 - Norway 0`
5. Matches are sorted by number of total goals [DONE]
   - start(Sweden, Norway)
   - start(Denmark, Belgium)
   - update(Sweden, Norway, 1, 0)
   - get()
   - result: \
     `1. Sweden 1 - Norway 0` \
     `2. Denmark 0 - Belgium 0` 
6. Matches are with the same total goals are sorted by start time, later started precedes earlier started [DONE]
   - start(Sweden, Norway)
   - start(Denmark, Belgium)
   - get()
   - result: \
     `1. Denmark 0 - Belgium 0` \
     `2. Sweden 0 - Norway 0`
7. Finished match is removed from the board [DONE]
   - start(Sweden, Norway)
   - start(Denmark, Belgium)
   - finish(Denmark, Belgium)
   - get()
   - result: \
     `1. Sweden 0 - Norway 0`
8. Cannot finish match which is not on score board [DONE]
    - start(Sweden, Norway)
    - finish(Denmark, Belgium)
    - result: exception is thrown 
    - get()
    - result: \
      `1. Sweden 0 - Norway 0`
9. Board is empty when no match is started [DONE]
   - get()
   - result: \
     ``
10. Board does not accept values which are not 0 or positive integer [DONE]
    - start(Sweden, Norway)
    - update(Sweden, Norway, -1, 0)
    - result: exception
    - get()
    - result: \
     `1. Sweden 0 - Norway 0`
11. Scenario from specification [DONE]
    - start(Mexico, Canada)
    - start(Spain, Brazil)
    - start(Germany, France)
    - start(Uruguay, Italy)
    - start(Argentina, Australia)
    - start(Mexico, Canada, 0, 5)
    - start(Spain, Brazil, 10, 2)
    - start(Germany, France, 2, 2)
    - start(Uruguay, Italy, 6, 6)
    - start(Argentina, Australia, 3, 1)
    - get()
    - result: \
      `1. Uruguay 6 - Italy 6` \
      `2. Spain 10 - Brazil 2` \
      `3. Mexico 0 - Canada 5` \
      `4. Argentina 3 - Australia 1` \
      `5. Germany 2 - France 2`


Candidates to be a separate scenario:
- update is changing score by reducing goals; update result should be success
- update time does not have impact on match order
- when we try to start match multiple times, start time should not be changed