@basic
Feature: The level 1a: Basic Dying and Scoring

  Background:
    Given the game service started

  @row39
  Scenario Outline: die with 3 skulls on first roll -> interface reports death & end of turn
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    And run the play round
    Then interface reports death & end of turn
    Examples:
      | dieRoll                                                         |
      | "skull, skull, skull, coin, diamond, diamond, diamond, diamond" |

  @row40,41
  Scenario Outline: die at the second roll.
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices>
    And the player hold <holdDice>
    And set re-roll with <riggedReRoll>
    Then interface reports death & end of turn
    Examples:
      | row | card    | dieRoll                                                      | menuChoices | holdDice   | riggedReRoll          | hints                                                                                     |
      | 40  | "Gold " | "skull, parrot, parrot, parrot, parrot, sword, sword, sword" | 2           | "2,3,4,5 " | "skull, skull, sword" | roll 1 skull, 4 parrots, 3 swords, hold parrots, reroll swords, get 2 skulls 1 sword  die |
      | 41  | "Gold " | "skull, skull, parrot, parrot, parrot, parrot, sword, sword" | 2           | "3,4,5,6 " | "skull, sword"        | roll 2 skulls, 4 parrots, 2 swords, hold parrots, reroll swords, get 1 skull 1 sword  die |

  @row43
  Scenario Outline: die at the third roll.
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And the player hold <holdDice1>
    And set re-roll with <riggedReRoll1>
    And the player chose <menuChoices2>
    And the player hold <holdDice2>
    And set re-roll with <riggedReRoll2>
    Then interface reports death & end of turn
    Examples:"2 " + "2,3,4,5 " + "skull,monkey,monkey " + "2 " + "2,3,4,5 " + "skull,monkey ";
      | row | card    | dieRoll                                                      | menuChoices1 | holdDice1  | riggedReRoll1           | menuChoices2 | holdDice2  | riggedReRoll2   | hints                                                                                                                                    |
      | 43  | "Gold " | "skull, parrot, parrot, parrot, parrot, sword, sword, sword" | 2            | "2,3,4,5 " | "skull, monkey, monkey" | 2            | "2,3,4,5 " | "skull, monkey" | //roll 1 skull, 4 parrots, 3 swords, hold parrots, re-roll swords, get 1 skull 2 monkeys re-roll 2 monkeys, get 1 skull 1 monkey and die |


  @row44
  Scenario Outline: score first roll with nothing but 2 diamonds and 2 coins and FC is captain (SC 800)
    Given the fortune card is "Captain"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                        | ExpectedScore |
      | "monkey, monkey, parrot, diamond, diamond, coin, coin, parrot" | 800           |


  @row45
  Scenario Outline: get set of 2 monkeys on first roll, get 3rd monkey on 2nd roll (SC 200 since FC is coin)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices>
    And the player hold <holdDice>
    And set re-roll with <riggedReRoll>
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                       | menuChoices | holdDice | riggedReRoll                          | ExpectedScore |
      | "monkey, monkey, parrot, parrot, sword, sword, parrot, skull" | 2           | "1,2 "   | "monkey, parrot, sword, sword, skull" | 200           |

  @row46
  Scenario Outline: score 2 sets of 3 (monkey, swords) in RTS on first roll   (SC 300)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                       | ExpectedScore |
      | "monkey, monkey, monkey, sword, sword, sword, parrot, parrot" | 300           |

  @row47
  Scenario Outline: score 2 sets of 3 (monkey, parrots) in RTS using 2 rolls   (SC 300)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices>
    And the player hold <holdDice>
    And set re-roll with <riggedReRoll>
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                      | menuChoices | holdDice   | riggedReRoll     | ExpectedScore |
      | "monkey, monkey, parrot, parrot, sword, sword, skull, skull" | 2           | "1,2,3,4 " | "monkey, parrot" | 300           |

  @row48
  Scenario Outline: score a set of 3 diamonds correctly (i.e., 400 points)   (SC 500)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                           | ExpectedScore |
      | "diamond, diamond, diamond, monkey, monkey, parrot, sword, sword" | 500           |

  @row49
  Scenario Outline: score a set of 4 coins correctly (i.e., 200 + 400 points) with FC is a diamond (SC 700)
    Given the fortune card is "Diamond"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                | ExpectedScore |
      | "coin, coin, coin, coin, monkey, parrot, sword, sword" | 700           |

  @row50
  Scenario Outline: score set of 3 swords and set of 4 parrots correctly on first roll (SC 400 because of FC)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                       | ExpectedScore |
      | "sword, sword, sword, parrot, parrot, parrot, parrot, monkey" | 400           |

  @row54
  Scenario Outline: score set of 6 monkeys on first roll (SC 1100)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                         | ExpectedScore |
      | "monkey, monkey, monkey, monkey, monkey, monkey, skull, parrot" | 1100          |

  @row55
  Scenario Outline: score set of 7 parrots on first roll (SC 2100)
    Given the fortune card is "Gold"
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | dieRoll                                                          | ExpectedScore |
      | "parrot, parrot, parrot, parrot, parrot, parrot, parrot, monkey" | 2100          |

  @score_at_first_roll
  Scenario Outline: the player chose to score at the first roll.
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | row | card       | dieRoll                                                           | ExpectedScore | hints                                                                                           |
      | 44  | "Captain " | "monkey, monkey, parrot, diamond, diamond, coin, coin, parrot"    | 800           | score first roll with nothing but 2 diamonds and 2 coins and FC is captain (SC 800)             |
      | 46  | "Gold "    | "monkey, monkey, monkey, sword, sword, sword, parrot, parrot"     | 300           | score 2 sets of 3 (monkey, swords) in RTS on first roll   (SC 300)                              |
      | 48  | "Gold "    | "diamond, diamond, diamond, monkey, monkey, parrot, sword, sword" | 500           | score a set of 3 diamonds correctly (i.e., 400 points)   (SC 500)                               |
      | 49  | "Diamond " | "coin, coin, coin, coin, monkey, parrot, sword, sword"            | 700           | score a set of 4 coins correctly (i.e., 200 + 400 points) with FC is a diamond (SC 700)         |
      | 50  | "Gold "    | "sword, sword, sword, parrot, parrot, parrot, parrot, monkey"     | 400           | score set of 3 swords and set of 4 parrots correctly on first roll (SC 400 because of FC)       |
      | 54  | "Gold "    | "monkey, monkey, monkey, monkey, monkey, monkey, skull, parrot"   | 1100          | score set of 6 monkeys on first roll (SC 1100)                                                  |
      | 55  | "Gold "    | "parrot, parrot, parrot, parrot, parrot, parrot, parrot, monkey"  | 2100          | score set of 7 parrots on first roll (SC 2100)                                                  |
      | 56  | "Gold "    | "coin, coin, coin, coin, coin, coin, coin, coin"                  | 5400          | score set of 8 coins on first roll (SC 5400)  seq of 8 + 9 coins +  full chest (if you have it) |
      | 57  | "Diamond " | "coin, coin, coin, coin, coin, coin, coin, coin"                  | 5400          | score set of 8 coins on first roll and FC is diamond (SC 5400)                                  |
      | 58  | "Captain " | "sword, sword, sword, sword, sword, sword, sword, sword"          | 9000          | score set of 8 swords on first roll and FC is captain (SC 4500x2 = 9000) if you have full chest |
      | 64  | "Gold "    | "coin, coin, monkey, monkey, monkey, skull, skull, monkey"        | 600           | score a set of 4 monkeys and a set of 3 coins (including the COIN fortune card) (SC 600)        |

  @score_over_two_rolls
  Scenario Outline: the player re-roll once
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices>
    And the player hold <holdDice>
    And set re-roll with <riggedReRoll>
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | row | card       | dieRoll                                                          | menuChoices | holdDice   | riggedReRoll                          | ExpectedScore | hints                                                                                    |
      | 45  | "Gold "    | "monkey, monkey, parrot, parrot, sword, sword, parrot, skull"    | 2           | "1,2 "     | "monkey, parrot, sword, sword, skull" | 200           | get set of 2 monkeys on first roll, get 3rd monkey on 2nd roll (SC 200 since FC is coin) |
      | 47  | "Gold "    | "monkey, monkey, parrot, parrot, sword, sword, skull, skull"     | 2           | "1,2,3,4 " | "monkey, parrot"                      | 300           | score 2 sets of 3 (monkey, parrots) in RTS using 2 rolls   (SC 300))                     |
      | 60  | "Diamond " | "diamond, parrot, parrot, monkey, monkey, skull, skull, parrot"  | 2           | "1,2,3 "   | "diamond, sword, sword"               | 400           | score a set of 2 diamonds over 2 rolls with FC is diamond (SC 400)                       |
      | 61  | "Gold "    | "diamond, diamond, parrot, monkey, monkey, skull, skull, parrot" | 2           | "1,2 "     | "diamond, monkey, monkey, parrot"     | 500           | score a set of 3 diamonds over 2 rolls (SC 500)                                          |
      | 62  | "Gold "    | "coin, coin, parrot, monkey, monkey, skull, skull, parrot"       | 2           | "1,2 "     | "coin, monkey, monkey, parrot"        | 600           | score a set of 3 coins over 2 rolls  (SC 600)                                            |
      | 63  | "Diamond " | "coin, coin, parrot, monkey, monkey, skull, skull, parrot"       | 2           | "1,2 "     | "coin, monkey, monkey, parrot"        | 500           | score a set of 3 coins over 2 rolls  with FC is diamond (SC 500)                         |


  @row51,52,53,59
  Scenario Outline: several rolls
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And the player hold <holdDice1>
    And set re-roll with <riggedReRoll1>
    And the player chose <menuChoices2>
    And the player hold <holdDice2>
    And set re-roll with <riggedReRoll2>
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:"2 " + " 1,2,3,4,5,6 " + " monkey,monkey " + "2 " + " 1,2,4,5,7,8 " + " monkey,monkey " + "1";
      | row | card       | dieRoll                                                        | menuChoices1 | holdDice1      | riggedReRoll1                   | menuChoices2 | holdDice2      | riggedReRoll2           | ExpectedScore | hints                                                                                              |
      | 51  | "Gold "    | "coin, coin, sword, sword, parrot, parrot, monkey, monkey"     | 2            | "1,2,3,4 "     | "sword, parrot, monkey, monkey" | 2            | "1,2,3,4,5 "   | "sword, parrot, coin"   | 800           | score set of 3 coins+ FC and set of 4 swords correctly over several rolls (SC = 200+400+200 = 800) |
      | 52  | "Captain " | "coin, coin, sword, sword, parrot, parrot, monkey, monkey"     | 2            | "1,2,3,4 "     | "sword, parrot, monkey, monkey" | 2            | "1,2,3,4,5 "   | "sword, parrot, coin"   | 1200          | same as previous row but with captain fortune card  (SC = (100 + + 300 + 200)*2 = 1200)            |
      | 53  | "Gold "    | "sword, sword, sword, parrot, monkey, parrot, skull, monkey"   | 2            | "1,2,3 "       | "sword, monkey, parrot, monkey" | 2            | "1,2,3,4 "     | "sword, parrot, monkey" | 600           | score set of 5 swords over 3 rolls (SC 600)                                                        |
      | 59  | "Gold "    | "monkey, monkey, parrot, monkey, monkey, sword, sword, parrot" | 2            | "1,2,3,4,5,6 " | "monkey, monkey"                | 2            | "1,2,4,5,7,8 " | "monkey, monkey"        | 4600          | score set of 8 monkeys over several rolls (SC 4600 because of FC is coin and full chest)           |

  @row66
  Scenario Outline: get 7 swords on first roll, try to roll the 8 die by itself -> interface reports not allowed
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And try to re-roll
    Then interface reports not allowed
    Examples:
      | row | card    | dieRoll                                                   |
      | 45  | "Gold " | "sword, sword, sword, sword, sword, sword, sword, monkey" |