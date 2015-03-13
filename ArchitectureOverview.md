**taken from Cooper's post to the group**

Since I have a few minutes, I will lay out the general theme of the desklet execution environment here:

# 1. What is a desklet? #

A desklet is a JAR file with a META-INF/desklet.xml file, and an implementation that conforms to the desklet specification. The desklet.xml file defines a few basic things about the desklet and provides depedency information.

Information on the dependecies can be found here:

http://www.oreillynet.com/onjava/blog/2007/03/desklets_102_dependencies.html

# 2. How are desklets run? #

When a desklet file is installed, it gets assigned a UUID and is exploded into the ~/.ab5k folder. Once in this form it runs very much like a WAR file in a servlet container: Each installed desklet context gets an isolated classloader with just the dependencies and the desklet file itself. A custom security manager is installed that prompts the user when the desklet tries to do something that the user might not want. This can be overly agressive right now, but we will work on that. Basically, a desklet by default can:

a) Draw stuff.
b) Create threads.
c) Write files to the location returned by DeskletContext.getWorkingDirectory(), which returns a reference to the exploded desklet jar location in the ~/.ab5k folder. If you want to embed Derby or HSQL for whatever reason, this would be the place you want to write out your local DBs to.
d) Write files to the default java.io.tmpdir folder.

Pretty much anything else will prompt the user for permission.

When your desklet begins running, the .start() method is called on its own thread. This means your start() method won't block anyone elses and for people that don't want to deal with threading issues on their own, they can put a while() block in there and not worry about it.

# 3. Desklet Preferences #

The DeskletContext has get/setPreference methods on it. These are basic string value prefs that are, by default, stored to [Directory](Working.md)/META-INF/preferences.properties. If you wanted to you could store initial values in that file as you package the desklet. However, I consider this to be an ab5k implementation detail, NOT a specification detail. The best case is to use the default value argument on get property. Desklet preferences are persisten on load/unload of the desklet.

# 4. Desklet Updating #

See http://groups.google.com/group/desklets-contributors/browse_thread/thread/e54b8e1da126a9e9/904bcffcf8ce3ca8?hl=en#904bcffcf8ce3ca8

# 5. Auto installation with .ab5k files #

See
http://groups.google.com/group/desklets-contributors/browse_thread/thread/509fc59650e3fc09/eb01552ae3ffef68?hl=en#eb01552ae3ffef68



# 6. Containter Architecture #
In terms of Architecture, there are several main classes in the ab5k.security package that controll the desklet lifecycle stuff:

  * Registry. This is the maintainer of the desklet installation/uninstallation and dependency ananlysis/repository.

  * DeskletManager. This is the root runtime controller for all of the desklets. It maintains a set of runners that controll the lifecycle for individual desklets.

  * DeskletRunner. This is the execution environment for a single desklet. It builds the appropriate ClassLoader and threading for the desklet, and sets up the DeskletContext (DefaultContext) that gets inited to the desklet.

  * SecurityPolicy. This is the prompting system security policy. It stores user granted permissions based on the code base URL of the requesting library. That is, the classes under the [Directory](Working.md) for the desklet.

There are a few other classes in there, but they are mostly util and data classes.