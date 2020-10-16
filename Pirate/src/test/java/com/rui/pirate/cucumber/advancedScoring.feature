@advanced
Feature: The level 1b: Advanced scoring

  Background:
    Given the game service started

  @row71
  Scenario: roll 2 skulls, re-roll one of them due to sorceress, then go to next round of turn
    Given the fortune card is "Sorceress"
    When the player got "coin, parrot, parrot, parrot, parrot, sword, skull, skull" on first roll
    And the player get back 7
    And the player hold "1,2,3,4,5 "
    And set re-roll with "sword, sword"
    Then execute the sorceress card with 2 skull
    And interface reports the end of turn is false

  @row72
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

  @row73
  Scenario: roll no skulls, then next round roll 1 skull and re-roll for it, then go to next round
    Given the fortune card is "Sorceress"
    When the player got "coin, parrot, parrot, parrot, parrot, sword, sword, sword" on first roll
    And roll one skull on the next round
    And the player get back 6
    And the player hold "1,2,3,4,5 "
    And set re-roll with "monkey, sword, sword"
    Then execute the sorceress card with 1 skull
    And interface reports the end of turn is false


  @row76
  Scenario: first roll gets 3 monkeys 3 parrots  1 skull 1 coin  SC = 1100  (i.e., sequence of of 6 + coin)
    Given the fortune card is "Monkey Business"
    When the player got "monkey, monkey, monkey, parrot, parrot, parrot, skull, coin" on first roll
    Then player chooses to score board with expected score 1100

  @score_over_several_rolls
  Scenario Outline: Monkey Business (over several rolls)
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
      | row | dieRoll                                                     | menuChoices1 | holdDice1  | riggedReRoll1                     | menuChoices2 | holdDice2      | riggedReRoll2      | ExpectedScore | hints                                                                         |
      | 77  | "monkey, monkey, sword, parrot, parrot, parrot, coin, coin" | 2            | "1,2,7,8 " | "sword, sword, parrot, parrot "   | 2            | "1,2,3,4,7,8 " | "parrot, diamond " | 400           | over several rolls: 2 monkeys, 1 parrot, 2 coins, 1 diamond, 2 swords  SC 400 |
      | 78  | "monkey, monkey, sword, parrot, parrot, parrot, coin, coin" | 2            | "1,2,7,8 " | "monkey, parrot, parrot, parrot " | 2            | "1,2,3,4,5,6 " | "parrot, sword "   | 2000          | over several rolls get 3 monkeys, 4 parrots, 1 sword    SC 2000 (ie seq of 7) |

  @row81
  Scenario: Treasure Chest operations row81
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

  @row82
  Scenario: Treasure Chest operations row82
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

  @row92,93,94,98
  Scenario Outline: Full Chest (single roll)
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    Examples:
      | row | card               | dieRoll                                                         | ExpectedScore | hints                                                                                   |
      | 92  | "Gold "            | "monkey, monkey, monkey, sword, sword, sword, diamond, parrot"  | 400           | 3 monkeys, 3 swords, 1 diamond, 1 parrot FC: coin   => SC 400  (ie no bonus)            |
      | 93  | "Captain "         | "monkey, monkey, monkey, sword, sword, sword, coin, coin"       | 1800          | 3 monkeys, 3 swords, 2 coins FC: captain   => SC (100+100+200+500)*2 =  1800            |
      | 94  | "Gold "            | "monkey, monkey, monkey, sword, sword, sword, sword, diamond"   | 1000          | 3 monkeys, 4 swords, 1 diamond, 1 coin FC: coin   => SC 1100  (ie 600+bonus)            |
      | 98  | "Monkey Business " | "monkey, monkey, parrot, coin, coin, diamond, diamond, diamond" | 1200          | FC: monkey business and RTS: 2 monkeys, 1 parrot, 2 coins, 3 diamonds   SC 1200 (bonus) |

  @row95
  Scenario: FC: 2 sword sea battle, first  roll:  4 monkeys, 1 sword, 2 parrots and a coin then re-roll 2 parrots and get coin and 2nd sword score is: 200 (coins) + 200 (monkeys) + 300 (swords of battle) + 500 (full chest) = 1200
    Given the fortune card is "Two Sabre "
    When the player got "monkey, monkey, monkey, monkey, sword, parrot, parrot, coin" on first roll
    And the player chose 2
    And the player hold "1,2,3,4,5,8 "
    And set re-roll with "coin, sword"
    Then player chooses to score board with expected score 1200

  @row101,102
  Scenario Outline: Skull Island and Skull fortune cards (single roll)
    Given the fortune card is <card>
    When the player got <dieRoll> on first roll
    And run the play round
    Then interface reports death & end of turn
    Examples:
      | row | card         | dieRoll                                                       | hints                                                    |
      | 101 | "Two Skull " | "monkey, monkey, parrot, coin, coin, diamond, diamond, skull" | die by rolling one skull and having a FC with two skulls |
      | 102 | "One Skull " | "monkey, monkey, parrot, coin, coin, diamond, skull, skull"   | die by rolling 2 skulls and having a FC with 1 skull     |

  @row103,104
  Scenario Outline: Skull Island and Skull fortune cards (Several rolls)
    Given the fortune card is <card>
    And all player has a initial score of 1000
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And set re-roll with <riggedReRoll1>
    And the player chose <menuChoices2>
    And set re-roll with <riggedReRoll2>
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with <ExpectedScore> for other players
    Examples:
      | row | card         | dieRoll                                                     | menuChoices1 | riggedReRoll1                                | menuChoices2 | riggedReRoll2                 | ExpectedScore | hints                                                                                        |
      | 103 | "Captain "   | "monkey, monkey, parrot, coin, skull, skull, skull, skull"  | 2            | "skull, parrot, parrot, coin "               | 2            | "skull, coin, diamond "       | -1200         | roll 5 skulls with FC: Captain => -1000 for all other players  (Captain doubles the penalty) |
      | 104 | "Two Skull " | "monkey, monkey, parrot, coin, coin, diamond, skull, skull" | 2            | "skull, skull, parrot, coin, coin, diamond " | 2            | "skull, coin, coin, diamond " | -700          | roll 2 skulls AND have a FC with two skulls: roll 2 skulls next roll, then 1 skull => -700   |

  @row105
  Scenario: roll 3 skulls AND have a FC with two skulls: roll no skulls next roll  => -500
    Given the fortune card is "Two Skull "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, coin, coin, coin, skull, skull, skull" on first roll
    And the player chose 2
    And set re-roll with "monkey, monkey, parrot, coin, coin "
    Then player chooses to score board with expected score -500
    And check the score board with -500 for other players

  @row106
  Scenario: roll 3 skulls AND have a FC with 1 skull: roll 1 skull next roll then none => -500
    Given the fortune card is "One Skull "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, coin, coin, coin, skull, skull, skull" on first roll
    And the player chose 2
    And set re-roll with "monkey, monkey, parrot, coin, skull "
    And the player chose 2
    And set re-roll with "monkey, monkey, parrot, coin "
    Then player chooses to score board with expected score -500
    And check the score board with -500 for other players

  @row107
  Scenario: show deduction received cannot make your score negative
    Given the fortune card is "One Skull "
    And all player has a initial score of 300
    When the player got "monkey, monkey, parrot, coin, coin, skull, skull, skull" on first roll
    And the player chose 2
    And set re-roll with "monkey, monkey, parrot, coin, skull "
    And the player chose 2
    And set re-roll with "monkey, monkey, parrot, coin "
    Then player chooses to score board with expected score -500
    And check the score board with -500 for other players

  @row110,111,112,113
  Scenario Outline: Sea Battles die on first roll   => lose points
    Given the fortune card is <card>
    And all player has a initial score of <initScore>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with expected score <ExpectedScore> for the player
    Examples:
      | row | initScore | card           | dieRoll                                                   | ExpectedScore | hints                                                |
      | 110 | 1000      | "Two Sabre "   | "monkey, monkey, parrot, coin, coin, skull, skull, skull" | -300          | FC 2 swords, die on first roll   => lose 300 points  |
      | 111 | 1000      | "Three Sabre " | "monkey, monkey, parrot, coin, coin, skull, skull, skull" | -500          | FC 3 swords, die on first roll   => lose 500 points  |
      | 112 | 1000      | "Four Sabre "  | "monkey, monkey, parrot, coin, coin, skull, skull, skull" | -1000         | FC 4 swords, die on first roll   => lose 1000 points |
      | 113 | 300       | "Four Sabre "  | "monkey, monkey, parrot, coin, coin, skull, skull, skull" | -1000         | FC 4 swords, die on first roll   => lose 1000 points |

  @row114,117
  Scenario Outline: Sea Battles die on first roll   => lose points
    Given the fortune card is <card>
    And all player has a initial score of <initScore>
    When the player got <dieRoll> on first roll
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with expected score <ExpectedScore> for the player
    Examples:
      | row | initScore | card           | dieRoll                                                      | ExpectedScore | hints                                                                               |
      | 114 | 1000      | "Two Sabre "   | "monkey, monkey, monkey, sword, sword, coin, parrot, parrot" | 500           | FC 2 swords, roll 3 monkeys 2 swords, 1 coin, 2 parrots  SC = 100 + 100 + 300 = 500 |
      | 117 | 1000      | "Three Sabre " | "monkey, monkey, monkey, sword, sword, sword, sword, parrot" | 800           | FC 3 swords, roll 3 monkeys 4 swords  SC = 100 + 200 + 500 = 800                    |

  @row115
  Scenario Outline: Sea Battles (Several rolls)
    Given the fortune card is <card>
    And all player has a initial score of <initScore>
    When the player got <dieRoll> on first roll
    And the player chose <menuChoices1>
    And the player hold <holdDice1>
    And set re-roll with <riggedReRoll1>
    Then player chooses to score board with expected score <ExpectedScore>
    And check the score board with expected score <ExpectedScore> for the player
    Examples:
      | row | initScore | card         | dieRoll                                                        | menuChoices1 | holdDice1    | menuChoices1 | riggedReRoll1   | ExpectedScore | hints                                                                                                                              |
      | 115 | 1000      | "Two Sabre " | "monkey, monkey, monkey, monkey, sword, skull, parrot, parrot" | 2            | "1,2,3,4,5 " | 2            | "sword, skull " | 500           | FC 2 swords, roll 4 monkeys 1 sword, 1 skull  2 parrots then re-roll 2 parrots and get 1 sword and 1 skull   SC = 200 +  300 = 500 |

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

  @row120
  Scenario: FC 4 swords, roll 3 monkeys 4 swords 1 skull  SC = 100 +200 + 1000 = 1300
    Given the fortune card is "Four Sabre "
    And all player has a initial score of 1000
    When the player got "monkey, monkey, monkey, sword, sword, sword, sword, skull" on first roll
    Then player chooses to score board with expected score 1300
    And check the score board with expected score 1300 for the player

  @row121
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