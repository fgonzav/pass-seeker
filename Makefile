arg ?= "test.rar"


build-only:
	./gradlew clean build

package-only:
	./gradlew jar

copy:
	cp build/libs/pass-seeker-0.0.1-SNAPSHOT.jar run/

build: build-only copy

run-dev-only:
	./gradlew run --args=$(arg)

run-dev: build-only run-dev-only

run-only:
	java -jar build/libs/pass-seeker-0.0.1-SNAPSHOT.jar $(arg)

run: build-only package-only run-only