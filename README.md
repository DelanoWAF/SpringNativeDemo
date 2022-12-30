# SpringNativeDemo
Start this application like any other Spring Boot application with:

mvn spring-boot:run

And visit:
http://localhost:8080/greeting
for generating checkpoints in Ladybug.

You can then visit:
http://localhost:8080/ladybug
to view and use Ladybug.

If you have made changes to Ladybug and would like to use those changes in this project, you must make sure to change the version of Ladybug inside this project's pom.xml to the SNAPSHOT version of your Ladybug project. You must restart the application after every Ladybug build that contains your new changes.

You can generate a Native Image in Docker while Docker is running on your computer using:
mvn spring-boot:build-image

Do note that this program returns errors when ran as a Native Image in Docker.
