Gitlet - CS61B (Data Structures) Project

The goal of this project was to implement a version control system using the Java serializable interface.

Attached is the design document with more details on the fields and methods involve.

main.java --> Driver for the program. 
	Can use calls to gitlet to initialize directory (like .git) and branch commits.
	Can add ,remove, or commit files
	Can view status & commit log
	Can create merge, or checkout branches

commit.java --> Class for Commits
	Class to encapsulate the contents of a commit. Node and list structure with
 	references to all commits. Uses Java File, Serializable, Arraylist, Date, HashMap, List

	Used a directory file system (creating folders and files depending on user)
