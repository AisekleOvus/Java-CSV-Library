# Java-CSV-Library

## ACSVL - little lib provides find, edit and add methods to TXT data files, such as CSV.

* **find methods** - collection of methods allowing you to search by whole lines or just in single column. Some of them can return number of found line and line itself, and another can consume found line with its number in your special way.
* **edit methods** - to replace specific lines with your ones.
* **add methods** - to add one or collection of lines to the end of file.

### Version 0.2

### find lines methods :
``` public static void findLine(File file, String searchString, long startRow, int inColumn, String delimiter, BiConsumer<Long, String> callback) ```
* **file** - file object to search in
* **searchString** - what to search
* **startRaw**  - points on which raw to start searching, *optional parameter* 
* **inColumn**  - points on in which column to search, *optional parameter* 
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '* 
* **callback**  - BiConumer which conumes *"found raw number"* as Long and *"the found line"* as String
* **The method  does not return enything**

``` public static Map.Entry<Long, String> findNextLine(File file, String searchString, long startRow, int inColumn, String delimiter) ```
* **file** - file object to search in
* **searchString** - what to search
* **startRaw**  - points on which raw to start searching 
* **inColumn**  - points on in which column to search, *optional parameter* 
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '*
* **The method returns Map.Entry Object with *"found raw number as "KEY"* and *the found line as "VALUE"***

``` public static Map<Long, String> findLines(File file, String searchString, long startRow, int inColumn, String delimiter) ```
* **file** - file object to search in
* **searchString** - what to search
* **startRaw**  - points on which raw to start searching, *optional parameter* 
* **inColumn**  - points on in which column to search, *optional parameter* 
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '*
* **The method returns *Map* containing all found lines in , in which *"found raw number as "KEY"* maps to *the found line as "VALUE"***
### edit lines methods :
``` public static void editLines(File file, Map<Long, String> lines, String delimiter) ```
* **file**  - file object to search in
* **lines** - *Map* which constains *KEY = number of line to edit* maped to *VALUE = string value of line to edit*
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '*
### add lines methods :
``` public static void addLines(File file, Iterable<String> lines, String delimiter) ```
* **file**  - file object to search in
* **lines** - Iterable Object containing String lines to add to the file
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '*
``` public static void addLine(File file, String line, String delimiter) ```
* **file**  - file object to search in
* **line**  - single String line to add to the file
* **delimiter** - delimiter of columns in file,  *optional parameter by default is ' __;__ '*

### Important : All methods in the library might throw AcsvlException !
**AcsvlException** is all-in-one exception, it proxies other exceptions (e.g. FileNotFoundException, IOException, AcsvlFileFormatException, AcsvlInputFormatException) which can be reached
by call ***getCause()*** method
