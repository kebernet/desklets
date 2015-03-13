# A quick tutorial to build a simple desklet. #

# Getting a project set up #

  * Make sure java 6 is installed.
  * Run AB5k, make sure it works. The first time you install it you should see six desklets pre-installed.
  * Make sure you have subversion installed
  * Check out the code from subversion.  See the google code project. [link](link.md)
  * Create a new java application project in NetBeans. (NetBeans is not required, but it makes these instructions easier. You can replicate the placement of your classes and the xml in any IDE, or by hand).
  * In the project properties add the 'spec' project to your classpath.  This project contains the desklet specification jars which you will compile against.

# Creating your desklet #

Create a new class like _MyAppDesklet_.

Make MyAppDesklet  subclass _ab5k.Desklet_ and implement the abstract methods

Make a main method like this:
```
	public static void main(String ... args) {
	        DeskletTester.start(myapp.MyAppDesklet.class);
	}
```

# Stopping your desklet #

In your stop() method you should be sure to call getContext().notifyStopped() after you have killed any threads or other resources you have created. If you don't call notifyStopped() it will take your widget 20 seconds (the default timeout) to actually quit.

# Starting your desklet #

This main method will run your desklet in a test environment. This lets you test your desklet without running it in the full AB5k container

In your MyAppDesklet class go to the init() method and type something like this:

```
        getContext().getContainer().setContent(new JButton("Greetings earthling!");
        getContext().getContainer().setVisible(true);
```

Compile your code and run the main method in MyAppDesklet.  The DeskletTester will start your desklet in a test environment and you should see a window appear with your desklet in it.

# Packaging your desklet #

Create a _META-INF_ directory in your src directory.  This directory must end up in your finished jar, so if you are not using NetBeans then you may need to modify your build environment to ensure that the _META-INF_ directory and it's contents are copied to the compiled jar.

Create a _desklet.xml_ file in the _META-INF_ directory. The contents of the desklets should be something like this:

```
<?xml version="1.0" encoding="UTF-8"?>
<desklet version="0.1">
    <name>My App</name>
    <author>Meee</author>
    <homepage>http://mydomain.com/ab5k/</homepage>
    <description>My Cool App</description>
    <class>myapp.MyAppDesklet</class>
</desklet>
```

Build a jar of your class files, including the _META-INF_ directory and the desklet.xml.   Rename your _.jar_ file as a _.desklet_ file. Now you are done!  You can run AB5k and add your desklet using the 'add' button in the manage dialog. _we might also want to mention that there is a quick
change in the project properties you can do so that the built jar has
the .desklet extension instead of .jar. Maybe we just need a NB /
Eclipse specific wiki page_

# Testing #

To aid in testing you can use these system properties.

  * -Dorg.ab5k.test.useDesktopPane  to use the older desktop pane verison of the window manager
  * -Dorg.ab5k.test.autoGrantAll=true  to turn off the permission request and just allow everything
  * -Dorg.ab5k.test.althomedirname=.ab5k\_windowmanager   to use a different home directory for ab5k