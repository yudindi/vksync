This program downloads music files from you profile in http://vk.com social network. It remembers which files were downloaded previously and doesn't download them for the second time.
If you want all files to be reloaded again, delete the xml sync log file (see ./src/main/resources/vksync.properties for the path to the file)

It is very handy to point Itunes to the folder you download music to so iTunes automatically picks up new music whenever available and does all covers and tags population.

Project is available at
https://github.com/yudindi/vksync/tree/master/vksync

To run the project:
1. configure ./src/main/resources/vksync.properties
2. mvn exec:java -Dexec.mainClass=ru.yudindi.VkSyncMain -Dexec.classpathScope=runtime
