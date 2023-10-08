MAIN_CLASS = dev.ekuinox.miharu.Miharu
OUTPUT_FILE = Miharu.jar

all:
	scala-cli --power package --assembly . --main-class $(MAIN_CLASS) --force -o $(OUTPUT_FILE)
