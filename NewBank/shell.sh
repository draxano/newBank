mkdir -p build
javac -classpath ​.​ -d ./build newbank/**/*.java
{ java -classpath ./build newbank/server/NewBankServer & } &> server.log
java -classpath ./build newbank/client/ExampleClient