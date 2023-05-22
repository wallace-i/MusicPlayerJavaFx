# mpTeaPlayer
<p>
A light-weight music player and digital audio library by Ian Wallace. 
Copyright 2023 MIT license.
</p>

## How To Install
<p>
While the v1.0 is in development, v0.5 will not have an installer. Instead,
you can download the .rar file from this link ___ and run the application file from
within the folder.
Download at https://www.spectrallines.dev/html/projects.html.
</p>

## How To Use
### Initialization
<p>
When first opening the application, the settings menu will pop up. Click 'Music Folder' to 
initialize your music library and start using the program. Once selected, a second window will 
pop up asking if you would like to do a 'Standard' or 'Recursive' initialization. Standard uses a 
strict file hierarchy and will fail if the folders are not organized as follows: 
'music folder -> artist -> album -> song.mp3' or 'music folder -> artist -> song.mp3'. This is because
standard uses file and folder titles for labeling the tracks in the user library. If an iTunes or album
image folder is in your music folder it may cause the initialization to fail. Recursive will go through 
every file in every folder and will ignore any non-audio formatted files. It works better with unorganized
music folders however the data is obtained solely from the track metadata, which may be missing or
incomplete. Once the audio files are parsed and imported into the application, mpTea Player is ready to go!
</p>

### Application Layout
<p>
There are three main sections of the program: the Artist List, the Playlists List, and the Track List. The Artist List holds a 
list of artist names representing the artist folders in your Music Folder (or if recursive, the artists in your
track metadata). The Playlists List is managed by the user; you can create or delete any amount of playlists that
you wish, and add any amount of tracks to each playlist. Currently, each track can only be added to a playlist
once (there is only one slot available for each track to hold a playlist title). Finally, the Track List displays
all the audio files which are currently related to either: the artist selected in Artist List, the playlist selected
in Playlists List, or from the Search Bar in the top right corner. The application uses a java Predicate system to
search for all tracks relating to the user's selection or search parameter.
</p>

### Playing Audio Files
<p>
At the bottom of the application are the player controls. The volume is controlled on the bottom left by a slider and a
mute box. In the center is the seeker slider with previous, play/pause, stop, and next buttons. There are also radio buttons
for 'auto play' which will play the next track in sequence, 'shuffle', and 'repeat'. The bottom right is reserved for album art
images contained within the audio file metadata (some tracks may not have album art).
</p>

### Bugs or Issues
<p>
Contact the developer at bugreportsldev@gmail.com for any issues you may encounter with the application or if you need assistance
setting it up.
</p>

## Keyboard Shortcuts:
### Music Player Controls:
<p>
Play/Pause:&ensp;Space
Next Track:&ensp;.
Prev. Track:&ensp;,
Stop:&ensp;/

Vol. Up:&ensp;=
Vol. Down:&ensp;-
Mute:&ensp;0

AutoPlay:&ensp;1
Shuffle:&ensp;2
Repeat:&ensp;3
</p>

### Menu Bar:
<p>
Settings:&ensp;Ctrl + S
Exit:&ensp;Ctrl + E
Import Artist:&ensp;Ctrl + I
Import Album:&ensp;Ctrl + L
Import Track:&ensp;Ctrl + T
Add Artist:&ensp;Ctrl + R
Create Playlist:&ensp;Ctrl + P
About:&ensp;Ctrl + O
GitHub:&ensp;Ctrl + G
Console Log:&ensp;Ctrl + C
Report Bug:&ensp;Ctrl + B
</p>

## License
MIT License
Copyright 2023, Spectral Lines Music LLC
Contact: iandw@spectrallines.dev
<p>
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
</p>

## About the program and developer
<p>
At roughly ~15000 lines of code, this audio media player is my first full software  
release and largest project to date. I chose javaFX as the base style to develop 
in after spotting the Media object in my java textbook.

While the textbook called for making a small Video player with a single button I wanted
to use it to create a mp3 player to handle the music on my PC, since after updating
my OS 4 years ago I have been lazy and neglected to re-install iTunes or any other music player
save VLC.

What started as a music player project quickly developed into a file management system project.
The actual music player and GUI only took a few weeks to put together and work properly. The real
issue of reading, writing, and presenting audio file metadata within the javaFX GUI objects
took about 2/3 of the project development time. I have no experience using winAmp, however I
wanted to design this program similarly- to be as lightweight, simple, and accessible as possible.
</p>

## BOM
<p>
This software uses libraries from the following:
Oracle Java SDK version 18.0.1
JavaFX SDK 18.0.1
Apache
GoogleCode json-simple
JAudioTagger
jsoup
kordamp ikonli bootstrapicons
Tea cup by Karen Arnold at PublicDomainPictures.net
</p>