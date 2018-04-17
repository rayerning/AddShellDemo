cd C:\Users\i\Desktop\forceapks
jarsigner -verbose -keystore forceapk -storepass 123456 -keypass 123456 -sigfile CERT -digestalg SHA1 -sigalg MD5withRSA -signedjar ReforceApk_des.apk ReforceApk.apk jiangwei
del ReforceApk.apk