# Introduction #

Small is the new big. While desktop applications have certainly not gone anywhere, the rise of connected applications has brought about new uses. Yahoo! Widget engine (formerly Konfabulator) was the first to bring small, web like apps to the desktop. A variation on the old "Desk Accessories" applications from early WIMP operating systems, these make developing small, single purpose apps easy, and usually wrap them in a bit of fancy chrome so they are visually appealing. Glossitope brings this to the world of Java.

In the Glossitope world, you begin by building a Desklet. This is a small class that expresses the lifecycle of your application in the container. If you have written Xlets or MIDlets before, it will look very familiar. If you haven't that is OK too.


# Getting Started #

What you need to get:
  * A Java 6 JDK. For Windows and Linux this is available from [java.sun.com](http://java.sun.com). For Mac OSX users, you can get a preview from [developer.apple.com](http://developer.apple.com/java/).
  * Maven 2. Available at http://maven.apache.org
  * Your IDE/Editor of choice.

Once you have Java 6 and Maven 2 installed, you can use the Desklet Archetype to create a skeleton project. This is done by invoking at the command line:
```
mvn archetype:create -DgroupId=org.me -DartifactId=mydesklet \
    -DarchetypeGroupId=org.ab5k -DarchetypeArtifactId=desklet-archetype \
    -DarchetypeVersion=1.0  \
    -DremoteRepositories=http://desklets.googlecode.com/svn/mavenrepo 
```

This will create a basic project with a simple MyDesklet class. If you use IDEA or Eclipse you can create an IDE project by moving into the new project directory and invoking one of:
```
  mvn idea:idea
  mvn eclipse:eclipse
```
Using [Mevenide](http://mevenide.codehaus.org) plugin for NetBeans you can make this an IDE supported archetype by clicking the "Custom" button in the New Project -> Maven 2 -> Archetype dialog and entering the archetype... values above.


# The Basic Desklet Lifecycle #

All desklets must implement 4 methods: init, start, stop, and destroy. These method are called as your desklet moves through its lifecycle. The init() method is used to set up resources you might need, The destroy method is used to clean up. Start and Stop should begin or end the execution of any running code. You should be sure to call...
```
 this.getContext().notifiyStopped(); 
```
...inside your stop() method. This lets the container know you have successfully completed shutting down any long-lived processes or threads (like Timers).

When start() is called, your desklet is on its own thread. That means for simple things, it is perfectly OK to do something like:
```
while( !stopped ){
   // do something over and over
   try{ Thread.sleep( 500 ); } catch(InterruptedException ie){}
}
```

You must remember, however, that if you want something to execute on the Swing Event Dispatch Thread, you should use the SwingUtilities class to execute that code.

Your desklet may be stopped or destroyed at any time by the container. If the container has been idle for a long time, desklets may be destroyed to reduce the standing overhead taken up by the application. If CPU resources seem at a minimum, the container might .stop() and .start() desklets only when they are visible.

Sometimes, however, you might have a desklet that needs to be doing something regularly. Perhaps it is interacting in an ongoing basis with a server. If you only want your desklet to be stopped or destroyed when the container shuts down, or the user stops or uninstalls the desklet, you can override this behavior by calling:
```
 this.getContext().setShutdownWhenIdle( false );
```

# Drawing to the Screen #

You can use regular Swing widgets to draw your desklet. Soon we hope to add support for HTML and F3 UIs, but for now, Swing is it. To add your desklet's UI to the screen, you will use the three DeskletContainer objects provided by the DeskletContext.

```
   SwingUtilties.invokeAndWait( new Runnable(){ 
          public void run(){
              DeskletContainer main = getContext().getContainer();
              main.setContent( mySwingComponent );
              // This is the main ui for your desklet.

              DeskletContainer config = getContext().getConfigurationContainer();
              // This is the configuration ui for setting preferences, etc.

              DeskletContainer dock = getContext().getDockingContainer();
              // This is the ui for your desklet on the dock bar.

          } } );
```

Here we used SwingUtilities to make sure the initialization takes place on the Swing EDT. Remember, your desklet doesn't run on the EDT by default, so if you are attempting operations that must take place in Swing's thread, you should use SwingUtilities.

You are not required to use any particular one of these containers but you will need to use one of them or you won't have anything to show on screen. You cannot create your own JFrames and JDialogs.

When using the containers, there are several option you can specify that will determine how your JComponent is rendered in Glossitope:

  * setBackgroundDraggable( boolean ) : Determines whether the default action for mouse drags will let the user reposition the desklet.
  * setResizable( boolean ) : Toggles the resizability of the desklet container.
  * setShaped( boolean ) : Toggles the default window decorations and the transparency of the default background.
  * setSize( Dimension2D ) : Sets the size the desklet container.
  * setVisible( boolean ) : Toggles the visibility of the container.


# Local Storage #

There are two ways your desklet might persist data inside the container. The first is through the preferences on the desklet context. The getPreference and setPreference methods will let you store name-value pairs. This is an easy way to save user preferences or state information you might need across lifecycles of the desklet.

The other way is by using the filesystem. Your desklet has a directory deemed "safe" to work with. It is available as a java.io.File reference by calling getWorkingDirectory() on the DeskletContext. You can use the filesystem anywhere on the local machine, but doing so will cause the user to be prompted to grant your desklet permission to access any files outside of this folder, so it is best if your desklet remains inside this area. Upon installation of the desklet, this folder will contain the contents of your .desklet file (a simple JAR file renamed .desklet).

Using the DeskletContext.setPreference method is the recommended way to save preferences information. The reason is this: when a user creates a portable profile, these preferences will be preserved when your desklet is installed on a new container. Files you create will not be present in the users new copy, and must be re-created by you or by the user herself.

# The Desklet Descriptor #

While the .desklet file is simply a JAR file, there is one other requirement: a META-INF/desklet.xml descriptor file. Hopefully, you shouldn't have to worry about this too much. The Maven project template includes a call to the maven-desklet-plugin that will automatically create the desklet descriptor as you build your project. We are going to look at this file, though, because it can serve as an entre into the next few topics we will discuss.

A [schema](http://desklets.googlecode.com/svn/trunk/spec/src/desklet.xsd) is available with the desklet specification library, but looking at a sample is sometimes easier:

```
<?xml version="1.0" encoding="UTF-8"?>
<desklet version="0.1">
```
The version attribute specifies the version of the desklet schema this desklet was built with. Future versions of Glossitope may support multiple desklet specification version simultaneously.
```
    <name>Eyeball</name>
    <author>Joshua Marinacci</author>
    <homepage>http://svn.joshy.org</homepage>
    <description>Watches your mouse very closely</description>
```
These are pretty self explanitory. It is good to fill these out, though. When you upload your desklet to the Glossitope gallery, these will determine what the user sees.
```
    <class>eyeball.EyeballDesklet</class>
```
This is the fully qualified name of your Desklet implementation class.
```
    <version>1.0.1</version>
```
The version of this desklet, as opposed to the version of the specification on the 

&lt;desklet&gt;

 tag.
```
    <repository>http://www.ab5k.org/maven/</repository>
```
This is a URL where your dependency libraries can be downloaded. By default it uses a Maven 1 style repository. This a dependency: groupId=com.me, artifactId=MyLibrary, version=1.0 would be found at:

> http://repository.me.com/com.me/jars/MyLibary-1.0.jar

You can specify a layout attribute on the repository that gives your more options. If you specify layout="maven-2" then the container will search for:

> http://repository.me.com/com/me/MyLibrary/1.0/MyLibrary-1.0.jar

If you specify layout="flat" then then container will search for:

> http://repository.me.com/MyLibrary-1.0.jar
```
    <dependency>
        <groupId>swingx</groupId>
        <artifactId>swingx</artifactId>
        <version>ab5k.02</version>
        <type>jar</type>
    </dependency>
    <dependency>
        <groupId>JoshUtil</groupId>
        <artifactId>JoshUtil</artifactId>
        <version>0.1</version>
        <type>jar</type>
    </dependency>
```
These are the dependencies for your desklet in Maven style. If you are using the project template, these will be automatically created from your Maven pom.xml file.

```
<source>http://www.ab5k.org/downloads/daily/new_window_manager/pre-install/Eyeball.desklet</source>

</desklet>
```
The 

&lt;source&gt;

 tag gives a URL to an authoritative version of your desklet. If you upload your desklet to the Glossitope gallery, this will be set for you and you don't have to worry about it. If you want to host this yourself, you need to have the desklet placed on a server that will correctly report the last modified time for the .desklet file. The container will then use this to compare to its local copy to see if it needs updating. If you can't host it on a server that correctly reports last modified time, you can specify a 

&lt;source-def&gt;

 that contains a URL to your authoritative desklet descriptor (desklet.xml) file. The container will then check that file to see if the version is different from the local copy, then download the new version from the URL specified by 

&lt;source&gt;

.

It is recommended that if you are not uploading your desklet to the Glossitope gallery, that you provide a 

&lt;source&gt;

 specification. This is because when the user creates a roaming profile, this is the location the container will search to install the desklet in the new container. If you have not specified a 

&lt;source&gt;

, your desklet will not be included in the profile.

# More on Dependencies #

Specifying dependencies in the Desklet descriptor (or in your pom.xml file for the Maven archetype project) is the recommended way. You can also include deps right in the .desklet file by placing them in the META-INF/lib folder. The reason we recommend not doing this except when absolutely necessary is dependencies are downloaded to a shared repository. By not including, say ROME 0.9 in your desklet file, you reduce the size of the .desket for initial download. If another desklet has already used ROME 0.9, it will not have to be downloaded by the user again. For common Java libraries like the Jakarta Commons or the JDesktop libraries, this can greatly improve the user experience. It will also save disk space for the user, especially if she is running multiple instance of your desklet at the same time.

# How Glossitope Installs a Desklet #

When a user installs a desklet, it is first assigned a UUID. This will be the internal reference for the desklet until it is uninstalled. The file is then copied to:
```
 ~/.ab5k/[UUID].jar
```
and uncompressed to:
```
 ~/.ab5k/[UUID]/
```
This is very similar to exploding a WAR file in a Tomcat servlet container. This exploded directory then becomes the working directory available to you from the DeskletContext. Glossitope will also put other files here for storing the preferences you set on the context, the position and sizing information of your desklet, etc.

Any dependencies are downloaded (when needed) and placed in a Maven 1 style repository under:
```
 ~/.ab5k/repository
```

These files are then shared by all the desklets. However, each desklet will get its own reference to the file in an isolated classloader. You don't have to worry about static initializations or variables from another desklet affecting yours.

# How Roaming Profiles Work #

The roaming profile mechanism is really quite simple. It simply saves out the UUID of the users desklets, the preferences they have saved, and the 

&lt;source&gt;

 location to an XML file called filename.ab5k. When the user opens this file, the container will download each desklet, place it in its original UUID locations, install (as above) and restore the preferences. We will be working to expand this functionality in the future.

# Building and Uploading #

Using the Maven project template, building your desklet jar is easy. Simply invoke:

```
mvn install
```

This will give you a jar file you can rename to .desklet, or upload to the Glossitope gallery for users to begin using. There is one more file you might want to include in your project before uploading.

Packaging a PNG file as META-INF/preview.png will give the gallery an image to associate with your desklet. It should be (but doesn't have to be) a square image at 300x300 pixels or better. Be advised, though, it might be scaled down to smaller sizes, so something that looks OK in a smaller size is a good idea too.