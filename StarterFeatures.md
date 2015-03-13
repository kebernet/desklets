# Introduction #

These are features which are either very easy to implement or are considered low risk and don't require much knowledge of core.


Rather than put them here I have started adding issues to the issue tracker using the keyword 'starter'. This will let you easily see all bugs that are good to start with.  Search here:

http://code.google.com/p/desklets/issues/list?can=2&q=starter&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary

**Build an error reporting system**  Errors that occur when loading a desklet are shown using the JXErrorPane.  This pane has the ability to send an error report to email or any other plugable system, though we are currently not using it. It would be nice if someone could write an error reporting webservice so that end users could press the 'send report' button in the error dialog and have a full report (including OS, JVM, AB5k version number, etc) sent to us.  The webservice should then let us dump or search the error reports to extract useful information about the problems people are having.