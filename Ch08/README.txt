ShakespeareInstrumented is a Honeycomb application so it takes advantage of the modern fragment classes. This application also includes lots of Log'ing so you can easily see the progression of events for activities and fragments as the application runs.

ShakespeareSDK is based on Gingerbread but uses the compatibility library to enable fragments in pre-Honeycomb applications. It is the same application as ShakespeareInstrumented but for the older versions of Android. You will see that it is very similar, with only a few changes where the modern classes must be implemented using compatibility versions (for example FragmentActivity instead of just Activity).

HoneycombGallery is a sample application that has been modified a bit to clean it up.
