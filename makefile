JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
        SecureClient.java \
		Client.java \
		SenderThread.java \
		ReceiverThread.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class