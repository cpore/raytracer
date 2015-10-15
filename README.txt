CS410 Ray Tracer - Casey Pore

Building
------------
In the root of the project folder (named raytracer) simply type 'ant jar' at the command line.
This will create a runnable jar file in the raytracer/jar folder.

Note that Ant is used to build a jar file. If you have problem building, it is
probably because your JAVA_HOME environment variable is setup incorrectly. If 
you encounter any problems, please ensure this variable refers to a valid Java
JDK.

Running
------------
After building, type:

'java -jar /path/to/jar/raytracer.jar </camera/file/path/camerafile> </in/file/path/inputfile_1>...</in/file/path/inputfile_n> <out/file/path/outputfile>'
