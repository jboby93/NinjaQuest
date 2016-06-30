echo removing previously built files...
rm src/com/jboby93/*/*.class

rm IST446_Adventure.jar

echo compiling sources...
javac -cp "./src" src/com/jboby93/*/*.java

echo done!