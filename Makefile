JAVAC = JAVAC
CLASSES = src/abscon/instance/components/PVariable.java src/abscon/instance/components/PConstraint.java Variable.java Domain.java Constraint.java Extension.java Intension.java MyParser.java 
MAIN_CLASS = MyParser

all: 
	mkdir -p bin
	javac -J-Xmx256m -d bin -sourcepath src src/csp/MyParser.java src/abscon/instance/intension/*/*.java
	jar -J-Xmx256m cfm csp.jar src/META-INF-MANIFEST.MF -C bin

compile: $(CLASSES:.java=.class)

%.class: %.java
	$(JAVAC) $<

run: 
	java $(MAIN_CLASS)

clean: 
	rm -f *.class