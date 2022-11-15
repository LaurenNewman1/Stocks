JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Main.java \
		  Cache.java \
		  Problem1.java \
		  Problem2.java \
		  Problem3.java \
		  Transaction.java

default: classes jar

jar:
	jar cmf manifest.mf Stocks.jar *.class

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java Main
