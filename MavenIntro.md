# Introduction #

Maven is a build system for java applications. We don't actually use maven for building ab5k (at least not yet) but we do use Maven's repository system.  Basically your desklet can request a certain version of a certain jar and Maven will make sure it gets into the classpath of your app. It may be downloaded from the web or come out of a cache. You don't have to care. Maven will just make it happen.

# Using Maven in your desklet #

To use maven in your desklet you just need to include the maven tags in your desklet.xml file.  For example, the Clock desklet needs joda time. We have joda time already set up in the ab5k maven repository, so it use it you can just put this in your desklet.xml

```

    <repository>http://www.ab5k.org/maven/</repository>
    <dependency>
        <groupId>joda</groupId>
        <artifactId>joda-time</artifactId>
        <version>1.4</version>
        <type>jar</type>
    </dependency>
```

# Setting up your own repository #

To set up a maven repository you just need to put your jars on a webserver somewhere with a very specific url. It should fit this pattern:

_base repository url_ / _groupid_ /jars/ _artifactid_ - _version_ .jar.

And that's it. As an example, the joda-time jar above is downloaded from:

http://www.ab5k.org/maven/joda/jars/joda-time-1.4.jar

You can put up your own jars and then refer to them from your desklet.xml. The jar will be downloaded once and then cached, so don't update that jar on your webserver. If you need to update it then use a different version number and change your desklet.xml to point to the new version.


# The standard Jars in AB5k's repository #

In ab5k we have jars for: swingx, swingx-ws, joda, JoshUtil, and support jars like swingx-layout, http commons 