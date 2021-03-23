# Gitlet Design Document

***Name***: Trang Van

## Classes and Data Structures

---

### Main

***Goal***: properly react to various *Gitlet* commands as command-line arguments

---

***Structures***

**I)** `LinkedList` - Singly Linked, acting like a `Stack` (LIFO)

- *Attributes/ Classes:*
    1. `head` - iterator pointing to front of the list 
    2. `front` - first `Node` in the list; represents the latest commit
    3. `next` - reference to the parent commit
    4. `Node` (Class) -  containing `log` message, `timestamp`, mapping of `filename` to **blob** references, parent reference, second parent references *(for merges)*
- *Functions:*
    1. `int getSize()` - returns size of list (i.e. number of commits)
    2. `Node getFront()` - returns info on the latest commit

**II)** `Tree` - `BinaryTree` to support different versions of the same project *(commit tree)*

- *Attributes:*
    1. `Node` - same as `Linked List`
    2. `branch` - immutable chain of commits
    3. `pointerA` and `pointerB` - pointers into each respective tree branch at furthest point
    4. `head` - current *active* branch
- *Functions:*
    1. `Node getHead()`- return latest commit on the current branch
    2. `switchBranches` - returns pointer of the other branch, `head` is now at this other branch

**III)** `Hashmap` - store File objects and retireve in constant time

- *Functions:*
    1. `hashcode()`: SHA-1 (see Algorithms)

---

***Functionalities/Commands***

1. `init` - create new Gitlet version-control system in current directory

    Usage: `java gitlet.Main init`

2. `add` - adds copy of file to *staging area*  

    Usage: `java gitlet.Main add [file name]`

3. `commit` - saving the contents of entire directories of files. "snapshot" of entire pr.oject at one point in time

    Usage: `java gitlet.Main commit [message]`

4. `rm` - unstage the file if currently staged, remove file from working directory

    Usage: `java gitlet.Main rm [file name]`

5. `log` - view history of backups

    Usage: `java gitlet.Main log`

6. `global-log` - view all commits ever made

    Usage: `java gitlet.Main global-log`

7. `find` - prints out ids of all commites that have given commit message

    Usage: `java gitlet.Main find [commit message]`

8. `status` - displays what branches currently exist, mark current branch with a `*` and what files have been staged for addition or removal

    Usage: `java gitlet.Main status`

9. `checkout` - restoring version of one or more *files* or entire *commits*

    Usage: 

    1. `java gitlet.Main checkout -- [file name]`
    2. `java gitlet.Main checkout [commit id] -- [file name]`
    3. `java gitlet.Main checkout [branch name]`
10. `branch` - maintaining related sequence of commits

    Usage: `java gitlet.Main branch [branch name]`

11. `rm-branch` - remove specified branch

    Usage: `java gitlet.Main rm-branch [branch name]`

12. `reset` - reverting back to another state of files

    Usage: `java gitlet.Main reset [commit id]`

13. `merge` - merging changes made in one branch into another

    Usage: `java gitlet.Main merge [branch name]`

---

Utilize following classes and Java standard library to read from and write to files.

### Dumpable

*Interface describing dumpable objects - uses `java.io.Serializable`*

1. `dump` - abstract method, needs to be implemented

### DumpObj

*Debugging class - uses `java.io.File`*

1. `main` - deserializes and apply dump to contents of each of the files 

### Utils

1. `writeContents` - concatenate bytes in the contents to write into the file
2. `readObject` - extends `Serializable` and returns object of type `T`
3. `readContentAsString(File file)` - return contents of file as a String

## Algorithms

---

`LinkedList`

`Tree`

`HashMap`

- SHA-1 (Secure Hash 1): ***cryptographic hash funciton —*** produces 160-bit integer hash from any sequence of bytes
    - extremely difficult to find two different byte streams with same hash (avoid hashing collisions)
    - hash value: 40-character hex string (convienent file name to store data in `.gitlet` directory)
    - use library classes - label objects correctly:
        1. include all metadata and references when hashing commit
        2. distinguish between commit hashes and blob hashes (i.e. hash extra word for each object that has one value for blobs, another for commits)
        3. make hash value file name — allows comparison between two blobs/files to see if they have the same contents (if SHA-1 equals, files equals)

## Persistance

---

**General:** All files of project and commit information are serialized into nodes of the commit tree. A commit tree with one branch is treated like a `LinkedList`. 

**Record State of Program When:**

1. `add` - stages files in working directory to be committed
2. `commit` - takes the added files and saves the changes to the tree

**Each commit contains:**  containing `log` message, `timestamp`, mapping of `filename` to **blob** references, parent reference, second parent references *(for merges)*

***Directory and Files:***

`.gitlet` ⇒ `folderName` → `metadata`, `fileName`

1. `.gitlet` - initializes the system so the Gitlet commands can run
2. `folderName` - arbituary folder with metdata and files 
3. `fileName` - name is determined by hash value of SHA-1 algorithm

