These images were created using GIFCon, XnView and Paint Shop Pro.
Feel free to use for personal or professional purposes, subject to the following:
Additional Copyright information:
Larry Ewing <lewing@isc.tamu.edu> created Tux using GIMP.
Marshall Kirk McKusick <mckusick@mckusick.com> is the copyright holder and creator of the BSD Daemon image.
The "Windows" cards were originally designed by Susan Kare for Microsoft.
To the best of my knowledge, the images used in any "standard" French/British 52 card deck are public domain.
The top Jokers are derived from an image I found of a John Waddington design.
The bottom Jokers were created by me as quick placeholders for a design I never got around to implementing.
I guess it's possible that Microsoft could claim copyright on the Windows Ace of Spades.
If they did, I'd suggest filling in or changing the white pattern on the Spade.


http://www.jfitz.com/cards/


10/03/10 rdb: UNH

File naming conventions

----------------------------------------------
Faces
-----
Start: c, d, h, s: club, diamond, spade, heart

Rest:

2-10: cards 2-10

1, 14, a: Ace
11, j: Jack
12, q: Queen
13, k: King

The multiple naming of the high cards makes it easy to read
the files into arrays and do it such that Ace can be either high or low.

Low Ace:
   for start = ("c", "d", "h", "s")
      for i = 1, 13
         name = start + i
         name = name + ".gif"
         read name file

High Ace:
   for start = ("c", "d", "h", "s")
      for i = 2, 14
         name = start + i
         name = name + ".gif"
         read name file

jr.gif: red joker
jb.gif: black joker

-----------------------------------------------------
Backs
-----
Start: b1 for blue, b2 for red
Rest:
fh: full horizontal
fv: full vertical
pl: partial left 
pr: partial right
pt: partial top
pb: partial bottom

opening the index.html shows all images
