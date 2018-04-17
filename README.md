# AddShellDemo
对apk进行加壳保护的Demo

SourceApk 为源Android的Project。
ShellApk 为解壳Apk的Android Project。
DexAddShellTool 为java的Project， 将源apk加密并和解壳dex文件合并成新的dex

re_sign_Apk 为新生成的apk进行签名。（如果是自己的签名文件，使用jarsigner进行签名）
