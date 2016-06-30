Ninja Quest v1.0
Author: Jace Boby
Build Date: 2/11/2015
=========================

Contents:
1. System Requirements
2. Story
3. Controls
4. In-Game Elements
  a. The Player
  b. Monsters
  c. Items
5. Building a Custom Map
6. Known Issues
7. Contact Info

1. System Requirements
========================
Your computer must have at least version 1.7 of the Java Runtime Environment installed.  It should work on most recent Windows, Mac OS X, or Linux computers, although slower devices may notice performance issues.

2. Story
========================
An evil mage has laid a terrible curse on an ancient land that reanimates the dead.  The townspeople have sent you to stop the mage before he and his hordes of monsters can crumble their society.  Do you have what it takes to save the land?

3. Controls
========================
WASD - move up/left/down/right
(alternatively, you can use the arrow keys for movement)

Space - throw a ninja star in the direction you are facing

ESC - exits the game.  You will be prompted to confirm this action; clicking the Close (X) button to close the window will exit the game immediately.

4. In-Game Elements
========================
a) The Player
You are an elite ninja and the town's last hope of defeating the mage and restoring peace to the land.

At the start of the game, you have 10HP, represented by 5 heart icons.  As you take damage, you will lose half of a heart or a whole one depending on the amount of damage taken.  If you run out of hearts, it's game over!

You can pick up ninja stars scattered throughout the land which can be thrown at monsters.

b) Monsters

The following monsters wander the game world, with their sole purpose being to stop you.

Zombie - A mindless, undead being that roams the land searching for fresh brains to snack on.
  HP: 2
  Damage on contact: 1HP

Skeleton - A reanimated skeleton that can throw bones at you as weapons
  HP: 2
  Damage on contact: 2HP
  Damage from thrown bone: 1HP

Mage - The being who has awakened the dead in a selfish quest to rule the land.  Defeating him will reverse his spell and restore peace.
  HP: 16
  Damage on contact: 3HP
  Damage from magic spell: 1HP
  Damage from fire spell: 2HP

c) Items

Various items can be picked up to aid your progress.  To pick up an item, simply walk over it.

Heart - Restores your health.  A heart has a number in it, representing the HP recovered by picking it up.

Ninja Stars - Provides you with ninja stars that can be thrown to attack monsters.  These pickups have a blue number in the corner indicating how many ninja stars you will get by picking it up.

Keys - Used to unlock gates.  Each key is used to unlock a specific gate within the world.

5. Building a Custom Map
========================
The game engine reads the map data from the file resources/map.ini.  A map file consists of a list of commands that add 'rooms' and add elements (such as items, monsters, walls, and gates) to those rooms, as well as defining the paths between rooms.

Included is a (somewhat crude, but usable) room editor.  The room editor, due to time constraints, was written in a hurry in Visual Basic; thus, it is only compatible with Windows.

The editor includes support for importing existing room commands, editing existing objects, adding new objects, and exporting the current design as a list of room generation commands.  The exported data can be pasted into a new map.ini file.

While the workflow of creating and 'programming' a room is fairly straightforward, designing a playable map can be tricky and time-consuming.

You can manually tell the game which map file to load through command-line arguments.  To specify a custom map from the command line:
	java -jar IST446_Adventure.jar -file "path/to/map/file"

6. Known Issues
========================
Movement: You can't walk along a wall while you are colliding with it.  This is done intentionally, for the sole reason that occasionally you can glitch the movement system by colliding with multiple walls at once if they overlap each other, and as a result you can be pushed through walls or locked gates.  Finding and fixing the root cause of this problem would likely take me longer than the project deadline, hence this restriction on movement.

7. Contact Info
========================
Questions?  Comments?
Email: jboby93@gmail.com

No software is perfect: in the event of a program error or crash, please send a screenshot of the error message, the map file you are playing (located in resources/map.ini), and a description of what you were trying to do that caused the error.  Often, a crash is the result of an improperly-written map file (such as following a path to a room that doesn't exist).  The more information you can provide about a problem, the more likely I'll be able to find the cause and help you out.