./gradlew assembleRelease
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ~/Dev/android/keys/my-release-key.keystore ~/Dev/android/purduefoodcourts/app/build/outputs/apk/app-release-unsigned.apk kptech
jarsigner -verify /home/kyle/Dev/android/purduefoodcourts/app/build/outputs/apk/app-release-unsigned.apk
rm -rf release.apk
zipalign -v 4 ~/Dev/android/purduefoodcourts/app/build/outputs/apk/app-release-unsigned.apk release.apk
