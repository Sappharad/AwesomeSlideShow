# Java Slide Show Presentation App
Copyright 2008, 2016 Paul Kratt, except where noted in source headers.

I initially wrote this as a final project for an OpenGL class in college. After a few years of giving boring PowerPoint presentations, at some point I decided that I wanted my presentations to have animated backgrounds because it would make them more fun to watch. I looked to see if any software could do such a thing back then, and the only options I found were expensive and didn't offer student discounts. When the opportunity arised to propose a final project that utilized multiple elements that we had learned during the course, I used the opportunity to create the presentation tool that I wanted.

The original goals of the application were as follows:
* Provide a simple XML format for defining the slide show content.
* Object positions and sizes would be relative to the slide, (from 0.0 to 1.0) for screen resolution independence.
* Ability to format text by specifying font, size and color.
* Include multiple slide transitions. Slides continue to animate during transitions.
* Support 3D animated backgrounds, loaded externally as plug-ins so others could develop their own backgrounds.

All of these goals were met within the 3 weeks allocated for the project.

In April 2008, I updated the application again to use it for another presentation. I added support for images as well as multiple elements on a slide (such as bullet points) that could fade in each time you clicked the mouse. Reaction to the presentation was mixed. Some people found it to be cool, while the instructor described the slides as distracting. After graduating in May 2008, I never used this application again.

Since I still liked the idea, I've decided to open source this code as of January 2016. The majority of it has not be modified since April 2008. However, I made some minor changes to ensure it builds and runs against JOGL 2.0.

# To compile and use
The source code is distributed as a project for the NetBeans IDE. It requires JOGL, which is part of the JOGAMP project. Build the project, then copy all of the classes from the compiled project's "backgrounds" folder into the backgrounds folder in the project root. If running from a .jar file instead of inside the NetBeans IDE, a backgrounds folder with all of the .class plug-ins should be placed in the same directory as the JAR, so the slide background plug-ins can be loaded.
When launching the program, a file open dialog appears. Selecting a slideshow XML file (see sample folder for samples) will play the slideshow. Click the mouse or use arrow keys to advance slides. When the slideshow is complete, quit the program using the default keyboard shortcut for the operating system you're running it on. (Alt-F4 on Windows, Command-Q on Mac OS X.)

# License
I'm releasing this under GPLv2, so if anyone ever decides to do anything with it (such as create new backgrounds) this will ensure changes get contributed back.

# Screenshots
One of the backgrounds:
![A still screenshot background that is animated]
(https://raw.githubusercontent.com/Sappharad/AwesomeSlideShow/master/bgsample.jpg)
Shows the rotate transition between two slides:
![A transition between two slides, backgrounds continue to animate during slide transitions]
(https://raw.githubusercontent.com/Sappharad/AwesomeSlideShow/master/transition.jpg)
