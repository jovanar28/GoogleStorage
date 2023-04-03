# GoogleFileStorage
This is an implementation of an API specification which can be found here *.

Fully written in java and deployed as a maven package which can be used as a dependency. Along with the LocalStorage implementation which is also deployed as a maven dependency it's integrated inside a client application which supports managing both local and google file storage.

CLI: *


Supported options are:
* Directory creation
  * createDirectory - with provided path, folder name and capacity
  * createDirectories - with provided path and folder creation pattern
  * createDir - with provided path and folder name

* File creation
  * createFile - with provided path and file name including extension

* File moving
  * moveFile - with provided path to file to be moved, destination path
  * moveFiles - with provided path to directory which files should be moved to provided location

* File deletion
  * deleteFiles - with provided list of files to be deleted
  
* File renaming
  * rename - with provided old file and new file name
* File copying
  * copyFile - with provided source and destination path

* Directory listing
  * listFilesInDir - lists files only in provided directory
  * listFilesInSubDir - lists files in provided directory and all its subdirectories
  * listFilesForExtension - lists files with specified extension
  * listFilesForName - lists files with specified name
  * listDirForNames - checks whether provided directory contains all provided file names
  * listFilesByDate - lists files created/modified on provided date
  * listFilesBetweenDates - lists files created/modified within provided date range

* Data filtering
  * filter - filters search/listing data based on provided filter flags
