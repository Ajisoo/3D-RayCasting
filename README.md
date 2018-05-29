# 3D-RayCasting
An immersive basic 3D engine that draws objects to the screen. This program uses 3D Geometry and RayCasting techniques to display objects realistically.

More specifically, Each pixel is a given a certain vector representing the direction and "line of sight" that that pixel would see. Split up among different threads, each "line of sight" is given a color, and together, all pixels draw a realistic display of the objects in the 3D space.

Notes:
	Spheres are easy to draw,  Textures (Triangles) are not. This indicates solving the matrix takes too long. Missing optimization?

Instructions:
Run MainPanel.main().
A Java window will open, defaulting to 1920 by 1080.
WADS to move, SPACE for up, and CTRL for down. Holding SHIFT makes movement faster.
Both the mouse and the arrow keys turn the camera.
