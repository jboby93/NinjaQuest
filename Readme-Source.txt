Ninja Quest v1.0 - Source Code
Author: jboby93
Last revised: 2/27/2015
=======================
Requires Java JDK 1.7 to compile

Changes:
========
-com.jboby93.jgl.GameWindow: fixed drawing bug on Mac OS X

Folders:
========
src/com/jboby93/ist446demo
- contains the game's code
src/com/jboby93/jgl
- contains the JavaGL library (the game framework used by the game)
resources
- contains resources used by the game

Files:
========
build.sh
- cleans any existing compiled code, and recompiles the program from its sources
clean.sh
- removes compiled Java files
makejar.sh
- recompiles the code and creates an executable JAR file
run.sh
- runs the program. must be compiled first
manifest
- required to create the JAR file. don't modify this!
Readme.txt
- this file :)

The .sh shell scripts are also included as Windows Batch Files (.bat) which do the same thing.  I'd recommend installing a UNIX shell for Windows.  I personally use Cygwin which is a million times better than the archaic Windows Command Prompt.