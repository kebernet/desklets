#!/bin/bash
CLASSPATH=../swingx/dist/swingx.jar
CLASSPATH=$CLASSPATH:../JoshUtil/dist/JoshUtil.jar
CLASSPATH=$CLASSPATH:lib/joshy-common.jar
CLASSPATH=$CLASSPATH:lib/TimingFramework.jar
CLASSPATH=$CLASSPATH:lib/iTunesLib.jar
CLASSPATH=$CLASSPATH:lib/javaws.jar
CLASSPATH=$CLASSPATH:lib/rome-0.8.jar
CLASSPATH=$CLASSPATH:lib/jdom-1.0.jar
CLASSPATH=$CLASSPATH:lib/swing-layout.jar
CLASSPATH=$CLASSPATH:lib/swingx_painterbranch.jar
CLASSPATH=$CLASSPATH:lib/weatherlib.jar
CLASSPATH=$CLASSPATH:dist/AB5k.jar
CLASSPATH=$CLASSPATH:../DeskletSpecification/dist/DeskletSpecification.jar
java -cp $CLASSPATH ab5k.MainFrame


