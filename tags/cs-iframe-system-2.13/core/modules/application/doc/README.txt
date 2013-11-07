To build and run this module:

1. Execute "mvn clean install -Pprofile" where "profile" is replaced by the appropriate profile.

2. The executable jar-file is placed in the target directory as well as a directory called "lib". You need to
manually place a directory named "security", in the target folder, containing a file called cv.key where the
key is kept.

3. You can execute the java archive directly from the target folder. If so, execute
"java -jar the_name_of_the_executable_java_archive.jar" to see what parameters that are possible. Then execute
"java -jar the_name_of_the_executable_java_archive.jar parameter" where "parameter" is replaced by a parameter
of your choice.

4. If you want to port the application to another location, zip (or similar) the jar-file, the lib folder and
the security folder into one archive, unzip the archive to the appropriate location and repeat step 3.

Note. You may have to add a security.properties file to the root of the executable jar-file. The content is
specified by security.properties.template.