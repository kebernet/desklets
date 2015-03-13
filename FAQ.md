# Frequently Asked Questions #

## How do I build my own desklets? ##

Please read (and improve) our BuildYourFirstDesklet guide.

## Why does AB5k require Java 6? ##

Because we want to take advantage of the new features in Java 6 like MultipleGradientPaint, the java.awt.Desktop APIs, and the

## Does AB5k run on Mac? ##

Yes. Even though Java 6 is not final on Mac, AB5k will run using Apple's latest developer preview (free w/ reg from Apple's dev site).  In fact, Josh uses Mac as his main development platform.


## Do I have to write my desklet in Java? ##

No! AB5k itself is written in Java, but there are a variety of languages which run on top of the JVM and can be used to write desklets.  Please post to the list for more information. We really want to get some people working on desklets written in Javascript, JRuby, F3, and many other cool languages that run well on the JVM.

## Why build another widget system? Why Java? ##

There are a variety of widget sytems out there but they all have several flaws that Java can address.

  * They are not crossplatform. Most support only one platform (I think Yahoo Widgets supports two) and none of them support Linux very well.

  * They don't support multiple languages and locales very well. Most don't even have a sense of resource bundles.

  * You must write in JavaScript. This is great for quick development but you are limited by the JavaScript language and the lack of libraries for it.

  * They have a limited security model.

  * They aren't open source.

Java addresses all of these concerns. Java is open source, fully cross platform by design,  has a robust and mature security system, supports unicode and i18n very well, and has access to a rich ecosystem of 3rd party (often open source) libraries.  We think that Java is a great base to build a top notch widget system.

## What mimetypes do I need on my webserver? ##

If you want to host desklets on your own website you should use the _application/x-ab5k_ mimetype. For Apache you can add this directive to your config:

`AddType application/x-ab5k .desklet`

The desklets should be served

## What does AB5k mean? ##

It's a really, really bad joke. We are supposed to be picking a better name, but have yet to find one. Ask Josh at JavaOne some time.