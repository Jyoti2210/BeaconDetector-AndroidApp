##################
### Objective ####
##################

This android application is developed as a part of the research project currently carried out in Syracuse University under Professor Yun Haong. The research project is all about automating the attendance system wherein the students have to use their Android phones to connect to the beacons installed in each classroom and mark their attendance.

This android application assists in configuring the setup required for the research project. The features of the application are listed as follows:

1) This android app helps to collect location coordinates of the beacons and the four corners of the classroom to create a boundary. This boundary ensures that though the students can connect to the beacons from outside the classroom because of the huge range of beacon connectivity, the attendance marked will be considered valid only if the students current location while marking the attendance is within the boundary defined.

2) The app uses Android Beacon Library to detect all the beacons in its range displaying all the information related to the beacon like UUID, Major, Minor, etc. alongwith their distance and accordingly update the information at the backend database.

3) Once a new beacon is installed, the app can detect all the beacons and ensure that the new beacon is displayed in the list to ensure that it installed and configured correctly.

##################
## Dependencies ##
##################

Android SDK
Android Beacon Library
AndroidStudio IDE
JDK 1.8
JRE 1.8