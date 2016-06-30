#!/bin/bash
#set -x

echo removing previously built files...
rm src/com/jboby93/*/*.class

rm IST446_Adventure.jar

echo compiling sources...
javac -cp "./src" src/com/jboby93/*/*.java

#echo compiling JavaGl.jar
#cd "../JavaGameLibrary"
#./makelib.sh
#cp JavaGL.jar "../IST446_GamePrototype/JavaGL.jar"

echo building executable jar file...

# echo $PWD

cd "./src"
jar cvfm ../IST446_Adventure.jar ../manifest com/jboby93/*/*.class

echo done!