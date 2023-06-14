# mpTeaPlayer
<p>
A light-weight music player and digital audio library by Ian Wallace. 
Copyright 2023 MIT license.
</p>

## How To Install
<p>
While the v1.0 is in development, v0.5 will not have an installer. Instead,
you can download the .rar file from <a href= "https://bucketeer-e731a2f7-3bfd-407d-a7fd-707383238d38.s3.amazonaws.com/public/mpTea_Player_Demo.rar"> here </a></li>  
and run the executable from within the folder.
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
Play/Pause:&ensp;Space<br>
Next Track:&ensp;.<br>
Prev. Track:&ensp;,<br>
Stop:&ensp;/<br>
<br>
Vol. Up: = <br>
Vol. Down: - <br>
Mute: 0 <br>

AutoPlay: 1 <br>
Shuffle: 2 <br>
Repeat: 3 <br>
</p>

### Menu Bar:
<p>
Settings:&ensp;Ctrl + S <br>
Exit:&ensp;Ctrl + E <br>
Import Artist:&ensp;Ctrl + I <br>
Import Album:&ensp;Ctrl + L <br>
Import Track:&ensp;Ctrl + T <br>
Add Artist:&ensp;Ctrl + R <br>
Create Playlist:&ensp;Ctrl + P <br>
About:&ensp;Ctrl + O <br>
GitHub:&ensp;Ctrl + G <br>
Console Log:&ensp;Ctrl + C <br>
Report Bug:&ensp;Ctrl + B <br>
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

## BOM
<p>
This software uses libraries and assets from the following:
Oracle Java SDK version 18.0.1<br>
JavaFX SDK 18.0.1<br>
Apache<br>
GoogleCode json-simple<br>
JAudioTagger<br>
jsoup<br>
kordamp ikonli bootstrapicons<br>
Tea cup by Karen Arnold at PublicDomainPictures.net<br>
</p>