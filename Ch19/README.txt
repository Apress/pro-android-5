
Instructions for loading sample projects into Android Studio
(Eclipse instructions are in the second half of this document)

Download the sample project zip file from www.androidbook.com/proandroid5/projects
Unzip the sample project zip file to your workstation.
Launch Android Studio.
Ensure that you've downloaded the following SDKs before you import any projects:
	Android 5.1
	Android 5.0.1
	Android 4.1.2
	Android Support Repository
	Google Play services
	Google Repository
You can get to SDK Manager by going to the Configure section of AS.

If Android Studio opens a previous project, use File -> Close project to get to the
main AS screen.
Choose Import project
Navigate to the unzipped project directory and choose the project you want to work with.
Android Studio will look for dependencies on library projects and will offer to replace
jars and library sources with dependencies. Check these checkboxes since it will make
your life much easier. Click Finish.
You will get an import summary text file from AS to tell you what happened during the
import. You shouldn't have any problems. Some source files will have been moved to
accommodate how AS manages files; this is normal.

Before you can work with any of the WhereAmI sample projects, you will need to get
your own Google API Key and put it into the AndroidManifest.xml file in each project.


Instructions for loading sample projects into Eclipse

Download the sample project zip file from www.androidbook.com/proandroid5/projects
Launch Eclipse.
Ensure that you've downloaded the following SDKs before you import any projects:
	Android 5.1
	Android 5.0.1
	Android 4.1.2
	Android Support Library
	Google Play services
	
Create a new workspace for working with the sample projects.
File -> Import...
General -> Existing Projects into Workspace
Click on Select archive file, Browse to the zip file for this chapter, and open it.
Click Finish.

The importing will take a while. Progress is shown in the lower right corner of Eclipse.
When importing is done you will see all of the sample
projects in the navigator on the left, plus 2 support library "projects" that are
dependencies for some of the sample projects. Some may have errors, which we'll address
next.

Before you can work with any of the WhereAmI sample projects, you will need to get
your own Google API Key and put it into the AndroidManifest.xml file in each project.

The simplest way we've found to get rid of the import errors is to quit Eclipse and
restart it. However, if you still have some errors, here are a few things you can try:

Choose Clean... from the Project menu, and clean all projects, or specific ones if there
are only a few with errors.

Some of the sample projects that require the compatibility library and/or the google
play services library may have "lost" their link to the dependent library. For these
you need to right-click on the project, choose Properties, then Android, and in the Library
section in the bottom right, use Add... to link to the library that's needed, and use
Remove to get rid of the extras. Click OK.

