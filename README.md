# Switcheroo
A Windows application made in Java, able to change the user's current wallpaper. The user can select folders and manually change wallpaper or, in the future, let the application update it automatically in regular time intervals from the chosen folder. 


Changing the wallpaper is done through the Win32 API (or SPI); more info from:
http://channel9.msdn.com/coding4fun/articles/Setting-Wallpaper 



References:
Makes use of the "imgscalr" library to scale the images as needed in the preview-window:
https://github.com/thebuzzmedia/imgscalr