@echo off
echo removing previously built files...

del src\com\jboby93\ist446demo\*.class
del src\com\jboby93\jgl\*.class
del IST446_Adventure.jar

echo.
echo compiling sources...
javac -cp "./src" src/com/jboby93/ist446demo/*.java src/com/jboby93/jgl/*.java

echo.
echo building executable jar file...

cd src
jar cvfm ../IST446_Adventure.jar ../manifest com/jboby93/jgl/*.class com/jboby93/ist446demo/*.class

echo.
echo done!
pause