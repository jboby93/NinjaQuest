@echo off
echo removing previously built files...

del src\com\jboby93\ist446demo\*.class
del src\com\jboby93\jgl\*.class
del IST446_Adventure.jar

echo.
echo compiling sources...
javac -cp "./src" src/com/jboby93/ist446demo/*.java src/com/jboby93/jgl/*.java

echo.
echo done!
pause