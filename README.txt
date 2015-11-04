CS410 Ray Tracer - Casey Pore

Building
------------
In the root of the project folder (the folder in which you extracted the tar),
simply type 'ant jar' at the command line. This will create a runnable jar file in the jar folder.

Note that Ant is used to build a jar file. If you have problem building, it is
probably because your JAVA_HOME environment variable is setup incorrectly. If 
you encounter any problems, please ensure this variable refers to a valid Java
JDK.

Running
------------
After building, type:

'java -jar /path/to/jar/raytracer.jar </path/to/camerafile> </path/to/propertiesfile> </path/to/inputfile_1>...</path/to/inputfile_n> </path/to/outputfile>'
