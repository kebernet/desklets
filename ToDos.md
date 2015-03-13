# ToDos #

Put animation back into Manager dialog when starting / stopping. At least fade it with a progress bar on top.

Dialogs on macosx don't repaint properly if there is animation underneath them.

**done** Picture of the day needs a 'loading...' indicator

You get a ton of dialogs on startup.  We must eliminate some of them. No warning when creating a thread, for example.

We should have our own warning dialog instead of using JOptionPane

Make an 'adding' transition. When the desklet appears on screen have it transition in instead of just appearing.

Make new 'stopping desklet' transition. render into image and then zoom forward while fading?

Put up javascript to detect installed version of java. Make sure you have Java 6

Make countdown desklet use a more christmasy font

Add a dialog api for desklets to show and move their own dialogs. make them not be heavyweight. make them be attached to the desklet's window

Add a dialog transition of some sort. just zooming from nothing using image scaling? Can we do 3d yet?

**done** Put swingx-ws (both) and fs jars inside the wikipedia desklet, remove from core. The same for the MapViewer app.

Write a quick tutorial on building your first desklet. new proj, import jar, make a gui, new class to extend abstract desklet. test in main method. add desklet.xml, jar and run

Fix loading desklet through new open event on windows

Build a proper database driving gallery. allow upload to staging, approval, and then download.

ab5k: idea: mini-dock mode that collapses to a strip 16px wide showing just prefs, +/-, and ab5k logo

ab5k: idea: collapse to 1px strip and auto-unhide when you move over it. then click to open the full container.

Make the weather desklet use the full SVG weather icons rather than the pre-rendered ones. Then use the icons in the dock view as well as the main view.

### Weather ###

Make weather desklet prettier. make a better background with a glossy effect.

### Core ###