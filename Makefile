java:
	java Main.java

cpp:
	g++ -Wall -Wextra -Werror -pedantic -std=c++14 -o main main.cpp && ./main

python:
	python3 main.py

nodejs:
	node main.js

go:
	go run go/main.go

rust:
	cargo run -q

csharp:
	dotnet run

clean:
	rm -rf main target bin obj

.PHONY: java cpp python nodejs go rust csharp
