**MAZE GAME 9000**
# About the game
- To play a level, run the game then enter a number from 1 to 5 in the text box below the play button, then press Play.
- Move the player, using WASD. Press key to move that direction. Holding down a key in WASD, only moves one tile. Press and hold space then press and hold WASD, you can keep moving.
- Collect all regular rewards, represented by golden coins, then step onto the orange end point to win the game.
- Bonus rewards represented by green gems appear. Collect them for a nice points increase! But you must be fast since they only appear for a short time!
- If a snake or any of the moving enemies touch you, you die! Avoid them!
- There are 3 types of moving enemies: green(snake), red(rat), and blue(bat). Snakes follow and track you down. Red Rats move left and right. Blue Bats move up and down.
- Don't step on a punishment, represented by a bomb. If you do, you lose 25 points. If your score does negative, you die!
- Try to beat the level as fast as possible. The faster you are, the greater your time bonus is!
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 09 PM" src="https://user-images.githubusercontent.com/58926132/114286866-6e46ef80-9a17-11eb-8329-31d6ac326f6b.png">
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 15 PM" src="https://user-images.githubusercontent.com/58926132/114286868-7010b300-9a17-11eb-8591-0ef60296b672.png">
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 25 PM" src="https://user-images.githubusercontent.com/58926132/114286877-7e5ecf00-9a17-11eb-99d6-5c3bf8705324.png">
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 31 PM" src="https://user-images.githubusercontent.com/58926132/114286881-80c12900-9a17-11eb-838c-7ea8e453c568.png">
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 38 PM" src="https://user-images.githubusercontent.com/58926132/114286885-83238300-9a17-11eb-8428-b7faf3dc23e9.png">
<img width="899" alt="Screen Shot 2021-04-10 at 4 09 44 PM" src="https://user-images.githubusercontent.com/58926132/114286886-84ed4680-9a17-11eb-98d8-12e7b34b5f55.png">


# How to make a level
- Let's look at level 1 as an example.
```
startPointWallDir down
endPointWallDir right
bonusRewardAppearOnTickTimes 100 300 500 700 900
bonusRewardLifetimeTickTimes 200
ticksUntil0TimeBonus 1500
maxTimeBonusScore 60
layout
############################
#   S         L            E
#    #########            ##
# #  B   #        ##########
# #     C#   ####    S     #
# # ######      # C   #    #
# #        # #  ##### #   G#
# ########## #### G # #    #
#  #G  #        #     #    #
#           # ######  # U C#
# ### ###   #     G   #    #
#      #       #           #
# C    # ##### #######     #
####   #   C   #   #    B###
#      ######### G         #
# B #             #######  #
# ###  ##########   # C    #
#   ## #       C#   #   #  #
#                       #  #
#####P######################
```
There are parameters then there is a level layout.
The level layout starts after the line that contains only the word `layout`. Nothing can come after the 20 lines that make up a level. Levels are 28 * 20 cells large. Draw your levels using https://textpaint.net/ and change the canvas size to 28 x 20.
**What kinds of objects does the layout support?**
- "#" for SolidWall, a wall that no Entity can move through.
- S for MovingEnemy, an enemy which follows the player.
- L for LeftRightEnemy, an enemy that moves horizontally, switching direction of it finds a wall.
- U for UpDownEnemy, an enemy that moves vertically, switching direction of it finds a wall.
- P for StartCell, the place where the Player appears when the level starts.
- E for EndCell, the place where the Player must go to after collecting all regular rewards to win the level.
- C for RegularReward, a reward worth 5 points that all must be collected to win.
- G for BonusReward, an optional reward worth 15 points that appears and disappears at given times.
- An empty space, , for an empty cell.

**What is a tick?**
- The game runs at 30 frames per second, or 30 ticks per second.
- You might think that 1 tick = 33.33 milliseconds, but actually, 1 tick = 33 milliseconds.
- This is due to how Thread.sleep can only pause the game in whole milliseconds, not decimal milliseconds.
- This 1t = 33ms means that for the game, 990 ms makes up 1 second. While a proper game would never do this, it's good enough for the circumstances of this project.
- Just keep in mind that 30 ticks = 1 second when setting these parameters.


**How about the parameters?**
- startPointWallDir and endPointWallDir: StartCell and EndCell mainly lie along the game barrier. Since the points are non-solid, they can be moved through. So, they require walls to keep players and enemies from going out of bounds. This method creates up to 3 walls beside the StartCell and EndCell to keep things in. Valid values: up, down, left, right (places the walls beside StartCell and EndCell in that direction), or none for no 3 walls.
- bonusRewardAppearOnTickTimes: Bonus rewards will appear on these given ticks. For example, if a reward appears on tick 10, it won't be visible and can't be collected until tick 10. One value is assigned to the first reward, then the second value is assigned to the second reward, and so on. The order of rewards is left to right, then up to down, similar to how you'd read words in a book. If there are more bonuses than ticks, values assigned will restart from the beginning of the given list.
- bonusRewardLifetimeTickTimes: Bonus rewards will appear for the length of these given ticks. For example, if a reward appears on tick 15, and its lifetime is 5 ticks, it will disappear on tick 20, since it appeared for ticks 15 to 19. Assignment works just like it does for bonusRewardAppearOnTickTimes.
- ticksUntil0TimeBonus and maxTimeBonusScore: There is a concept called time bonus. The faster the player completes the level, the more score they will get. Time bonus is calculated by: time_bonus = maxTimeBonusScore * (ticksUntil0TimeBonus - tick) / ticksUntil0TimeBonus, where tick is how many ticks the player took to complete the level. If the player takes too long to beat the level, where tick > ticksUntil0TimeBonus, the player gets a time bonus of 0.
