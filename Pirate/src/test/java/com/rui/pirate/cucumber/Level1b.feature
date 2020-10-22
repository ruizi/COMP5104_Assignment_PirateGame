@advanced
Feature: The level 1b: Advanced scoring

  Background:
    Given the game service started

  @row70
  Scenario: roll 2 skulls, re-roll one of them due to sorceress, then go to next round of turn
    Given the fortune card is "Sorceress"
    When the player got "coin, parrot, parrot, parrot, parrot, sword, skull, skull" on first roll
    And the player get back 7
    And the player hold "1,2,3,4,5 "
    And set re-roll with "sword, sword"
    Then execute the sorceress card with 2 skull
    And interface reports the end of turn is false

  @row71
  Scenario: roll no skulls, then next round roll 1 skull and re-roll for it, then score
    Given the fortune card is "Sorceress"
    When the player got "coin, parrot, parrot, parrot, parrot, sword, sword, sword" on first roll
    And the player chose 2
    And the player hold "1,2,3,4,5 "
    And set re-roll with "skull, sword, sword"
    And the player chose 3
    And the player get back 6
    And the player hold "1,2,3,4,5 "
    And set re-roll with "monkey,sword,sword "
    Then player chooses to score board with expected score 300

  @row72
  Scenario: roll no skulls, then next round roll 1 skull and re-roll for it, then go to next round
    Given the fortune card is "Sorceress"
    When the player got "coin, parrot, parrot, parrot, parrot, sword, sword, sword" on first roll
    And roll one skull on the next round
    And the player get back 6
    And the player hold "1,2,3,4,5 "
    And set re-roll with "monkey, sword, sword"
    Then execute the sorceress card with 1 skull
    And interface reports the end of turn is false


  @row75
  Scenario: first roll gets 3 monkeys 3 parrots  1 skull 1 coin  SC = 1100  (i.e., sequence of of 6 + coin)
    Given the fortune card is "Monkey Business"
    When the player got "monkey, monkey, monkey, parrot, parrot, parrot, skull, coin" on first roll
    Then player chooses to score board with expected score 1100

  @score_over_several_rolls
  Scenario Outline: Monkey Business (over several rolls)
  row76: over several rolls: 2 monkeys, 1 parrot, 2 coins, 1 diamond, 2 swords  SC 400
  row77: over several rolls get 3 monkeys, 4 parrots, 1 sword    SC 2000 (ie seq of 7)
    Given the fortune card is "Monkey Business"
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And the player hold <holdDice1>
    And set re-roll with <riggedReRoll1>
    And the player chose <menuChoices2>
    And the player hold <holdDice2>
    And set re-roll with <riggedReRoll2>
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | id | Grid Row | dieRoll                                                     | menuChoices1 | holdDice1  | riggedReRoll1                     | menuChoices2 | holdDice2      | riggedReRoll2      | ExpectedScore |
      | 1  | 76       | "monkey, monkey, sword, parrot, parrot, parrot, coin, coin" | 2            | "1,2,7,8 " | "sword, sword, parrot, parrot "   | 2            | "1,2,3,4,7,8 " | "parrot, diamond " | 400           |
      | 2  | 77       | "monkey, monkey, sword, parrot, parrot, parrot, coin, coin" | 2            | "1,2,7,8 " | "monkey, parrot, parrot, parrot " | 2            | "1,2,3,4,5,6 " | "parrot, sword "   | 2000          |

  @row83
  Scenario: Treasure Chest operations row 83
  roll 3 parrots, 2 swords, 2 diamonds, 1 coin     put 2 diamonds and 1 coin in chest
  then re-roll 2 swords and get 2 parrots put 5 parrots in chest and take out 2 diamonds & coin
  then re-roll the 3 dice and get 1 skull, 1 coin and a parrot
  score 6 parrots + 1 coin for 1100 points
    Given the fortune card is "Treasure Chest"
    When the player got "parrot, parrot, parrot, sword, sword, diamond, diamond, coin" on first roll
    And the player chose 3 for menuChoice
    And the player chose 1 for treasure chest menu
    And the player chose "6,7,8 " in treasure chest
    And the player chose 0 for treasure chest menu
    And the player hold "1,2,3 "
    And set re-roll with "parrot,parrot "
    And the player chose 3 for menuChoice
    And the player chose 1 for treasure chest menu
    And the player chose "1,2,3,4,5 " in treasure chest
    And the player chose 2 for treasure chest menu
    And the player chose "6,7,8 " in treasure chest
    And the player chose 0 for treasure chest menu
    And the player hold "0 "
    And set re-roll with "skull,coin,parrot "
    Then player chooses to score board with expected score 1100

  @row87
  Scenario: Treasure Chest operations row 87
  roll 2 skulls, 3 parrots, 3 coins   put 3 coins in chest
  then re-rolls 3 parrots and get 2 diamonds 1 coin    put coin in chest (now 4)
  then re-roll 2 diamonds and get 1 skull 1 coin     SC for chest only = 400 + 200 = 600
  also interface reports death & end of turn
    Given the fortune card is "Treasure Chest"
    When the player got "skull, skull, parrot, parrot, parrot, coin, coin, coin" on first roll
    And the player chose 3 for menuChoice
    And the player chose 1 for treasure chest menu
    And the player chose "6,7,8 " in treasure chest
    And the player chose 0 for treasure chest menu
    And the player hold "0 "
    And set re-roll with "diamond,diamond,coin "
    And the player chose 3 for menuChoice
    And the player chose 1 for treasure chest menu
    And the player chose "5 " in treasure chest
    And the player chose 0 for treasure chest menu
    And the player hold "0 "
    And set re-roll with "skull,coin "
    Then player chooses to score board with expected score 600
    And interface reports death & end of turn

  @Full_Chest_single_roll
  Scenario Outline: row 91,92,93,97
  row91: 3 monkeys, 3 swords, 1 diamond, 1 parrot FC: coin   => SC 400  (ie no bonus)
  row92: 3 monkeys, 3 swords, 2 coins FC: captain   => SC (100+100+200+500)*2 =  1800
  row93: 3 monkeys, 4 swords, 1 diamond, 1 coin FC: coin   => SC 1100  (ie 600+bonus)
  row97: FC: monkey business and RTS: 2 monkeys, 1 parrot, 2 coins, 3 diamonds   SC 1200 (bonus)
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | id | Grid Row | card               | dieRoll                                                         | ExpectedScore |
      | 1  | 91       | "Gold "            | "monkey, monkey, monkey, sword, sword, sword, diamond, parrot"  | 400           |
      | 2  | 92       | "Captain "         | "monkey, monkey, monkey, sword, sword, sword, coin, coin"       | 1800          |
      | 3  | 93       | "Gold "            | "monkey, monkey, monkey, sword, sword, sword, sword, diamond"   | 1000          |
      | 4  | 97       | "Monkey Business " | "monkey, monkey, parrot, coin, coin, diamond, diamond, diamond" | 1200          |

  @row96
  Scenario: FC: 2 sword sea battle, first  roll:  4 monkeys, 1 sword, 2 parrots and a coin then re-roll 2 parrots and get coin and 2nd sword score is: 200 (coins) + 200 (monkeys) + 300 (swords of battle) + 500 (full chest) = 1200
    Given the fortune card is "Two Sabre "
    When the player got "monkey, monkey, monkey, monkey, sword, parrot, parrot, coin" on first roll
    And the player chose 2
    And the player hold "1,2,3,4,5,8 "
    And set re-roll with "coin, sword"
    Then player chooses to score board with expected score 1200

  @skull_card_single_roll
  Scenario Outline: Skull Island and Skull fortune cards (single roll)
  row100: die by rolling one skull and having a FC with two skulls
  row101: die by rolling 2 skulls and having a FC with 1 skull
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And run the play round
    Then interface reports death & end of turn
    Examples:
      | id | Grid Row | card         | dieRoll                                                       |
      | 1  | 100      | "Two Skull " | "monkey, monkey, parrot, coin, coin, diamond, diamond, skull" |
      | 2  | 101      | "One Skull " | "monkey, monkey, parrot, coin, coin, diamond, skull, skull"   |

  @skull_island_two_rolls
  Scenario Outline: Skull Island and Skull fortune cards (two rolls)
  row102: roll 5 skulls with FC: Captain => -1000 for all other players  (Captain doubles the penalty)
  row104: roll 3 skulls AND have a FC with two skulls: roll no skulls next roll  => -500
    Given the fortune card is <card>
    And all player has a initial score of 1000
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And set re-roll with <riggedReRoll1>
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with <ExpectedScore> for other players
    Examples:
      | id | Grid Row | card         | dieRoll                                                    | menuChoices1 | riggedReRoll1                         | ExpectedScore |
      | 1  | 102      | "Captain "   | "monkey, monkey, parrot, coin, skull, skull, skull, skull" | 2            | "skull, parrot, parrot, coin "        | -1000         |
      | 2  | 104      | "Two Skull " | "monkey, monkey, coin, coin, coin, skull, skull, skull"    | 2            | "monkey, monkey, parrot, coin, coin " | -500          |


  @skull_island_several_rolls
  Scenario Outline: Skull Island and Skull fortune cards (Several rolls)
  row103: roll 2 skulls AND have a FC with two skulls: roll 2 skulls next roll, then 1 skull => -700
  row105: roll 3 skulls AND have a FC with 1 skull: roll 1 skull next roll then none => -500
  row106: show deduction received cannot make your score negative
    Given the fortune card is <card>
    And all player has a initial score of <initialScore>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And set re-roll with <riggedReRoll1>
    And the player chose <menuChoices2>
    And set re-roll with <riggedReRoll2>
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with <ExpectedScore> for other players
    Examples:
      | id | Grid Row | initialScore | card         | dieRoll                                                     | menuChoices1 | riggedReRoll1                                | menuChoices2 | riggedReRoll2                   | ExpectedScore |
      | 1  | 103      | 1000         | "Two Skull " | "monkey, monkey, parrot, coin, coin, diamond, skull, skull" | 2            | "skull, skull, parrot, coin, coin, diamond " | 2            | "skull, coin, coin, diamond "   | -700          |
      | 2  | 105      | 1000         | "One Skull " | "monkey, monkey, coin, coin, coin, skull, skull, skull"     | 2            | "monkey, monkey, parrot, coin, skull "       | 2            | "monkey, monkey, parrot, coin " | -500          |
      | 3  | 106      | 300          | "One Skull " | "monkey, monkey, parrot, coin, coin, skull, skull, skull"   | 2            | "monkey, monkey, parrot, coin, skull "       | 2            | "monkey, monkey, parrot, coin " | -500          |

  @sea_battles_single_roll
  Scenario Outline: row 109,110,111,112
  row109: FC 2 swords, die on first roll   => lose 300 points
  row110: FC 3 swords, die on first roll   => lose 500 points
  row111: FC 4 swords, die on first roll   => lose 1000 points
  row112: show deduction received cannot make your score negative
  row113: FC 2 swords, roll 3 monkeys 2 swords, 1 coin, 2 parrots  SC = 100 + 100 + 300 = 500
  row116: FC 3 swords, roll 3 monkeys 4 swords  SC = 100 + 200 + 500 = 800
    Given the fortune card is <card>
    And all player has a initial score of <initScore>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedOutput>
    And check the score board with expected score <ExpectedOutput> for the player
    Examples:
      | id | Grid Row | initScore | card           | dieRoll                                                      | ExpectedOutput |
      | 1  | 109      | 1000      | "Two Sabre "   | "monkey, monkey, parrot, coin, coin, skull, skull, skull"    | -300           |
      | 2  | 110      | 1000      | "Three Sabre " | "monkey, monkey, parrot, coin, coin, skull, skull, skull"    | -500           |
      | 3  | 111      | 1000      | "Four Sabre "  | "monkey, monkey, parrot, coin, coin, skull, skull, skull"    | -1000          |
      | 4  | 112      | 300       | "Four Sabre "  | "monkey, monkey, parrot, coin, coin, skull, skull, skull"    | -1000          |
      | 5  | 113      | 1000      | "Two Sabre "   | "monkey, monkey, monkey, sword, sword, coin, parrot, parrot" | 500            |
      | 6  | 116      | 1000      | "Three Sabre " | "monkey, monkey, monkey, sword, sword, sword, sword, parrot" | 800            |

  @row115
  Scenario Outline: Sea Battles (Several rolls)
  FC 2 swords, roll 4 monkeys 1 sword, 1 skull  2 parrots then re-roll 2 parrots and get 1 sword and 1 skull   SC = 200 +  300 = 500
    Given the fortune card is <card>
    And all player has a initial score of <initScore>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And the player hold <holdDice1>
    And set re-roll with <riggedReRoll1>
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with expected score <ExpectedScore> for the player
    Examples:
      | id | Grid Row | initScore | card         | dieRoll                                                        | menuChoices1 | holdDice1    | menuChoices1 | riggedReRoll1   | ExpectedScore |
      | 1  | 115      | 1000      | "Two Sabre " | "monkey, monkey, monkey, monkey, sword, skull, parrot, parrot" | 2            | "1,2,3,4,5 " | 2            | "sword, skull " | 500           |

  @row118
  Scenario: FC 3 swords, roll 4 monkeys 2 swords 2 skulls then re-roll 4 monkeys and get  2 skulls and 2 swords   -> DIE
    Given the fortune card is "Three Sabre "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, monkey, monkey, sword, sword, skull, skull" on first roll
    And the player chose 2
    And the player hold "5,6 "
    And set re-roll with "skull, skull, sword, sword "
    Then player chooses to score board with expected score -500
    And check the score board with expected score -500 for the player

  @row119
  Scenario: FC 4 swords, roll 3 monkeys 4 swords 1 skull  SC = 100 +200 + 1000 = 1300
    Given the fortune card is "Four Sabre "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, monkey, sword, sword, sword, sword, skull" on first roll
    Then player chooses to score board with expected score 1300
    And check the score board with expected score 1300 for the player

  @row122
  Scenario: FC 4 swords, roll 3 monkeys, 1 sword, 1 skull, 1 diamond, 2 parrots then re-roll 2 parrots and get 2 swords thus you have 3 monkeys, 3 swords, 1 diamond, 1 skull   then reroll 3 monkeys and get  1 sword and 2 parrots  SC = 200 + 100 + 1000 = 1300
    Given the fortune card is "Four Sabre "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, monkey, sword, skull, diamond, parrot, parrot" on first roll
    And the player chose 2
    And the player hold "1,2,3,4,6 "
    And set re-roll with "sword, sword "
    And the player chose 2
    And the player hold "4,6,7,8 "
    And set re-roll with "sword, parrot, parrot "
    Then player chooses to score board with expected score 1300
    And check the score board with expected score 1300 for the player