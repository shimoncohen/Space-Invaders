# 315383133
# shimon cohen

compile: bin
	javac -d bin -cp biuoop-1.4.jar src/*.java
run:
	java -cp biuoop-1.4.jar:bin:resources Ass7Game 
jar:
	jar cfm space-invaders.jar Manifest.mf -C bin . -C resources .
bin:
	mkdir bin