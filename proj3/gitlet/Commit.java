package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Class to encapsulate the contents of a commit. Node and list structure with
 * references to all commits.
 * @author Trang Van
 */
public class Commit implements Serializable {
    /**
     * Path to the objects folder where blobs are stored.
     */
    static final File OBJ_FOLDER = new File(".gitlet", "objects");
    /**
     * Path to commit folders that holds all commits made in flat directory.
     */
    static final File COM_FOLDER = new File(".gitlet", "commits");
    /** A user-defined message to give information about the commmit.*/
    private String _message;
    /** Indicator of the date and time of the commit. */
    private String _datetime;
    /** Holds UID SHA-1 Reference to the previous commit - parent commit. */
    private String _parent;
    /** Holds UID of current commit. Method to set UID with required fields. */
    private String _uid;
    /** Keep track of what branch name commit is on.*/
    private String _branch;

    /** Keep track of the file names that commit is supposed to
     * point to {filename: file uid}.*/
    private HashMap<String, String> _trackedFiles;
    /** Holds all commits made using the SHA-1 reference.*/
    private ArrayList<String> _allCommits;

    /** Constructor for Commit.
     * @param message user defined message
     * @param parent commit ID of latest ID is now parent
     * @param branch specifies what branch this is on
     */
    public Commit(String message, String parent, String branch) {
        _message = message;
        _parent = parent;
        if  (_parent == null) {
            _datetime = "Thu Jan 01 00:00:00 1970 +0000";
        } else {
            _datetime = getCommitTime();
        }

        _uid = Utils.sha1(this.toString());
        _branch = branch;

        _trackedFiles = new HashMap<>();
        _allCommits = new ArrayList<>();
    }

    /** Getter for message attribute.
     * @return message*/
    public String getMessage() {
        return _message;
    }
    /** Getter for datetime attribute.
     * @return date and time*/
    public String getDatetime() {
        return _datetime;
    }
    /** Getter for parent attribute.
     * @return parent UID*/
    public String getParent() {
        return _parent;
    }
    /** Getter for Commit's UID.
     * @return commit's UID*/
    public String getUID() {
        return _uid;
    }
    /** Getter for Commit's Branch.
     * @return branch name*/
    public String getBranch() {
        return _branch;
    }

    /** Getter for Commit's tracked files.
     * @return HashMap of tracked files*/
    public HashMap<String, String> getTrackedFiles() {
        return _trackedFiles;
    }
    /** Get date and time of commit using Java's Date
     *  and Simple Date Formatter.
     *  Source: TutorialsPoint - Java Date and Time
     *  URL: https://www.tutorialspoint.com/java/java_date_time.htm
     * @return String time generated
     */
    public String getCommitTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "E MMM dd HH:mm:ss yyyy Z");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /** Generate UID for a Commit object using the SHA-1 Algorithm.*/
    public void setUID() {
        List<String> references = new ArrayList<>();
        for (String key: getTrackedFiles().keySet()) {
            references.add(getTrackedFiles().get(key));
        }
        references.add(getParent());
        references.add(getMessage());
        references.add(getCommitTime());
        _uid =  Utils.sha1(references.toString());
    }

    /**
     * Sets the tracked files if there has been changes.
     * This prevents error where you are modifying the list
     * concurrently.
     * @param newTracked HashMap of updated tracked files
     */
    public void setTrackedFiles(HashMap<String, String> newTracked) {
        _trackedFiles.clear();
        _trackedFiles = newTracked;
    }

    /**
     * Makes the blobs from passed in File objects.
     * @param f File object to make into blob
     */
    public void makeBlob(File f) {
        String name = f.getName();
        String fileUID = Utils.sha1(Utils.readContentsAsString(f));
        String contents = Utils.readContentsAsString(f);
        _trackedFiles.put(name, fileUID);
        saveBlobs(fileUID, contents);
    }

    /**
     * PERSISTANCE: Saves the files as blobs using a HashTable-like
     * implementation. The file, named by generating SHA-1, is stored
     * in bucket-like directories, named by the first two characters of
     * the blob's UID.
     * @param fileUID String to be file's name
     * @param contents Content as String to Serialize
     */
    public void saveBlobs(String fileUID, String contents) {
        String bucketName = fileUID.substring(0, 2);
        File bucket = new File(OBJ_FOLDER, bucketName);
        if (!bucket.exists() && !bucket.isDirectory()) {
            bucket.mkdir();
        }
        File obj = Utils.join(bucket, fileUID);
        Utils.writeContents(obj, contents);
    }

    /**
     * PERSISTANCE: Saves the Commit instance to
     * a file that is named by it's whole UID.
     */
    public void saveCommit() {
        File obj = Utils.join(COM_FOLDER, getUID());
        Utils.writeObject(obj, this);
    }

    /**
     * PERSISTANCE: Reads Commit instance from given UID.
     * Supports shortUID under 10 characters and returns instance.
     * @param fileName Commit UID
     * @return Commit
     */
    public static Commit fromFile(String fileName) {
        if (fileName.length() < 10) {
            List<String> allFiles = Utils.plainFilenamesIn(COM_FOLDER);
            if (allFiles != null) {
                for (String f: allFiles) {
                    if (f.contains(fileName)) {
                        File obj = Utils.join(COM_FOLDER, f);
                        return Utils.readObject(obj, Commit.class);
                    }
                }
            }

        }
        File obj = Utils.join(COM_FOLDER, fileName);
        if (!obj.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(obj, Commit.class);
    }

}
