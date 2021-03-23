package gitlet;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Driver class for Gitlet, the tiny version-control system.
 *
 * @author Trang Van
 */
public class Main {
    /**
     * Path to Gitlet directory.
     */
    static final File GITLET_FOLDER = new File(".gitlet");
    /**
     * Path user's current working directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     */
    public static void main(String... args) {
        checkArgLength(args.length);
        switch (args[0]) {
        case "init":
            init();
            break;
        case "add":
            checkGitletDir();
            add(args[1]);
            break;
        case "commit":
            checkGitletDir();
            checkCommit(args[1]);
            commit(args[1]);
            break;
        case "log":
            checkGitletDir();
            log();
            break;
        case "global-log":
            checkGitletDir();
            globalLog();
            break;
        case "rm":
            checkGitletDir();
            rm(args[1]);
            break;
        case "checkout":
            checkGitletDir();
            checkoutHelper(args);
            break;
        case "branch":
            checkGitletDir();
            branch(args[1]);
            break;
        case "find":
            checkGitletDir();
            find(args[1]);
            break;
        case "status":
            checkGitletDir();
            status();
            break;
        case "reset":
            checkGitletDir();
            reset(args[1]);
            break;
        case "rm-branch":
            checkGitletDir();
            removeBranch(args[1]);
            break;
        case "merge":
            checkGitletDir();
            merge(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    /**
     * Check if user entered any arguments.
     * @param len length of arguments
     */
    public static void checkArgLength(int len) {
        if (len == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
    }

    /**
     * Check if there is an initialized Gitlet directory.
     */
    public static void checkGitletDir() {
        File path = new File(CWD, ".gitlet");
        if (!path.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /** For checkout, check number of operands inputted.
     *
     * @param arg String for --
     */
    public static void correctOperands(String arg) {
        if (!arg.equals("--")) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /**
     * Check if commit message is empty.
     * @param arg Length of message
     */

    public static void checkCommit(String arg) {
        if (arg.length() == 0) {
            System.out.println(" Please enter a commit message.");
        }
    }

    /**
     * Helper function to run all the checkout functions.
     * @param args All arguments from main
     */

    public static void checkoutHelper(String[] args) {
        if (args.length == 2) {
            checkout(args[1], true);
        } else if (args.length == 3) {
            correctOperands(args[1]);
            checkout(args[2]);
        } else if (args.length == 4) {
            correctOperands(args[2]);
            checkout(args[1], args[3]);
        }
    }
    /**
     * PERSISTANCE: Get the current branch from the HEAD file in .gitlet/refs.
     * @return String of branch name
     */
    public static String getHeadFile() {
        File head = Utils.join(GITLET_FOLDER, "refs", "HEAD");
        return Utils.readContentsAsString(head);
    }

    /**
     * PERSISTANCE: Get the current commit from the branch file in
     * .gitlet/refs/branches.
     * @return String of commit's UID
     */
    public static String getCurrentCommit() {
        String branchName = getHeadFile();
        File br = Utils.join(GITLET_FOLDER, "refs", "branches", branchName);
        return Utils.readContentsAsString(br);
    }

    /**
     * PERSISTANCE: Saves the latest commit to the current branch.
     * Updates HEAD file with the current branch name.
     * @param commit String for UID
     * @param branchName Used for filename
     */
    public static void saveReference(String commit, String branchName) {
        File gitletRefs = new File(new File(".gitlet"), "refs");
        File head = Utils.join(gitletRefs, "HEAD");
        Utils.writeContents(head, branchName);
        File branch = Utils.join(gitletRefs, "branches", branchName);
        Utils.writeContents(branch, commit);
    }
    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that
     * contains no files and has the commit message initial commit. It will
     * have a single branch: master, which initially points to this initial
     * commit, and master will be the current branch. The timestamp for this
     * initial commit will be 00:00:00 UTC, Thursday, 1 January 1970. Since
     * the initial commit in all repositories created by Gitlet will have
     * exactly the same content, it follows that all repositories will
     * share this commit (same UID) and all commits in all repositories will
     * trace back to it.
     * Starting Source: Itai Smith's Gitlet Intro Video Pt.3
     */
    public static void init() {
        File repo = Utils.join(CWD, ".gitlet");
        if (repo.exists() && repo.isDirectory()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        } else {
            repo.mkdir();
        }
        File staging = Utils.join(GITLET_FOLDER, "staging");
        if  (!staging.exists() && !staging.isDirectory()) {
            staging.mkdir();
        }
        File removal = Utils.join(GITLET_FOLDER, "removal");
        if (!removal.exists() && !removal.isDirectory()) {
            removal.mkdir();
        }
        File refs = Utils.join(GITLET_FOLDER, "refs");
        if (!refs.exists() && !refs.isDirectory()) {
            refs.mkdir();
        }
        File refBranch = Utils.join(GITLET_FOLDER, "refs", "branches");
        if (!refBranch.exists() && !refBranch.isDirectory()) {
            refBranch.mkdir();
        }

        File objs = Utils.join(GITLET_FOLDER, "objects");
        if (!objs.exists() && !objs.isDirectory()) {
            objs.mkdir();
        }

        File commits = Utils.join(GITLET_FOLDER, "commits");
        if (!commits.exists() && !commits.isDirectory()) {
            commits.mkdir();
        }

        Commit initial = new Commit("initial commit", null, "master");
        saveReference(initial.getUID(), "master");
        initial.saveCommit();
    }

    /**
     * Adds a copy of the file as it currently exists to the staging area for
     * addition. Staging an already-staged file overwrites the previous entry
     * in the staging area with the new contents. If the current working
     * version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it
     * is already there.
     * @param fileName String of file name
     */
    public static void add(String fileName) {
        File src = Utils.join(CWD, fileName);
        if (!src.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        File removal = new File(GITLET_FOLDER, "removal");
        File removedFile = Utils.join(removal, fileName);
        if (removedFile.exists()) {
            removedFile.delete();
        }

        File staging = new File(GITLET_FOLDER, "staging");
        File copy = Utils.join(staging, fileName);
        Utils.writeContents(copy, Utils.readContentsAsString(src));

        String commitUID = getCurrentCommit();
        Commit currCo = Commit.fromFile(commitUID);
        if (currCo.getParent() != null) {
            String fileUID = currCo.getTrackedFiles().get(fileName);
            String hash = Utils.sha1(Utils.readContentsAsString(copy));
            if (fileUID != null && fileUID.equals(hash)) {
                copy.delete();
            }
        }
    }

    /**
     * Saves a snapshot of certain files in the current commit and staging area
     * so they can be restored at a later time, creating a new commit. The
     * commit is said to be tracking the saved files. By default, each commit's
     * snapshot of files will be exactly the same as its parent commit's
     * snapshot of files; it will keep versions of files exactly as they are,
     * and not update them. A commit will only update the contents of files it
     * is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that
     * was staged instead of the version it got from its parent. A commit will
     * save and start tracking any files that were staged for addition but
     * weren't tracked by its parent.
     * @param message user-defined message
     */

    public static void commit(String message) {
        String branchName = getHeadFile();
        String commitUID = getCurrentCommit();

        Commit newCommit = new Commit(message, commitUID, branchName);

        File getToStage = new File(GITLET_FOLDER, "staging");
        File getToRemove = new File(GITLET_FOLDER, "removal");
        if (getToStage.list().length == 0 && getToRemove.list().length == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        File[] getStageFiles = getToStage.listFiles();
        if (getStageFiles != null) {
            for (File s: getStageFiles) {
                newCommit.makeBlob(s);
                s.delete();
            }
            newCommit.setUID();
        }

        File[] getRemovalFiles = getToRemove.listFiles();
        if (getRemovalFiles != null) {
            for (File r: getRemovalFiles) {
                r.delete();
            }
        }

        saveReference(newCommit.getUID(), branchName);
        newCommit.saveCommit();
    }

    /**
     * Unstage the file if it is currently staged for addition. If the file is
     * tracked in the current commit, stage it for removal and remove the file
     * from the working directory if the user has not already done so (do not
     * remove it unless it is tracked in the current commit).
     * @param fileName String
     */
    public static void rm(String fileName) {
        boolean isDeleted = false;
        File staging = new File(GITLET_FOLDER, "staging");
        isDeleted = Utils.join(staging, fileName).delete();

        String commitUID = getCurrentCommit();
        Commit currCo = Commit.fromFile(commitUID);
        if (currCo.getTrackedFiles().containsKey(fileName)) {
            String fileUID = currCo.getTrackedFiles().get(fileName);
            File gitletObj = new File(GITLET_FOLDER, "objects");
            File obj = Utils.join(gitletObj, fileUID.substring(0, 2), fileUID);
            File removal = new File(GITLET_FOLDER, "removal");
            File f = Utils.join(removal, fileName);
            Utils.writeContents(f, Utils.readContentsAsString(obj));

            File workDir = Utils.join(CWD, fileName);
            workDir.delete();
            isDeleted = true;
        }
        if (!isDeleted) {
            System.out.println("No reason to remove the file.");
        }
    }

    /**
     * Takes the version of the file as it exists in the head commit, the front
     * of the current branch, and puts it in the working directory, overwriting
     * the version of the file that's already there if there is one. The new
     * version of the file is not staged.
     * @param fileName String
     */
    public static void checkout(String fileName) {
        String commitUID = getCurrentCommit();
        checkout(commitUID, fileName);
    }

    /**
     * Takes the version of the file as it exists in the commit with the given
     * id, and puts it in the working directory, overwriting the version of the
     * file that's already there if there is one. The new version of the file
     * is not staged.
     * @param commitID String
     * @param fileName String
     */
    public static void checkout(String commitID, String fileName) {

        Commit target = Commit.fromFile(commitID);
        if (!target.getTrackedFiles().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String fileUID = target.getTrackedFiles().get(fileName);
        File gitletObj = new File(GITLET_FOLDER, "objects");
        File blob = Utils.join(gitletObj, fileUID.substring(0, 2), fileUID);

        File copy = new File(CWD, fileName);
        Utils.writeContents(copy, Utils.readContentsAsString(blob));
    }

    /**
     * Takes all files in the commit at the head of the given branch, and puts
     * them in the working directory, overwriting the versions of the files
     * that are already there if they exist. Also, at the end of this command,
     * the given branch will now be considered the current branch (HEAD). Any
     * files that are tracked in the current branch but are not present in the
     * checked-out branch are deleted. The staging area is cleared, unless the
     * checked-out branch is the current branch.
     * @param givenBr Given branch
     * @param isBranch Specifies if it's a branch or filename
     */
    public static void checkout(String givenBr, boolean isBranch) {
        String currBranch = getHeadFile();
        String currCoID = getCurrentCommit();
        Commit currCo = Commit.fromFile(currCoID);
        HashMap<String, String> currTracked = currCo.getTrackedFiles();

        File given = Utils.join(GITLET_FOLDER, "refs", "branches", givenBr);
        if (!given.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String branchCoID = Utils.readContentsAsString(given);
        Commit branchCo = Commit.fromFile(branchCoID);
        HashMap<String, String> branchTracked = branchCo.getTrackedFiles();

        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);
        if (cwdFiles != null) {
            for (String name: cwdFiles) {
                File f = Utils.join(CWD, name);
                boolean inGiven = branchTracked.containsKey(name);
                boolean inCurr = currTracked.containsKey(name);
                String hash = Utils.sha1(Utils.readContentsAsString(f));
                String brHash = branchTracked.get(name);
                if (inGiven && !inCurr && !hash.equals(brHash)) {
                    System.out.println("There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                    System.exit(0);

                }
            }
        }

        HashMap<String, String> tempCurrTrack = new HashMap<>(currTracked);
        if (givenBr.equals(currBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Set<String> currKeySet = currTracked.keySet();
        for (String file: currKeySet) {
            File path = Utils.join(CWD, file);
            if (!branchTracked.containsKey(file)) {
                path.delete();
            }
        }
        currCo.setTrackedFiles(tempCurrTrack);
        for (String file: branchTracked.keySet()) {
            checkout(branchCoID, file);
        }
        File[] staging = new File(GITLET_FOLDER, "staging").listFiles();
        if (staging != null) {
            for (File f: staging) {
                f.delete();
            }
        }
        saveReference(branchCoID, givenBr);
    }

    /**
     * Starting at the current head commit, display information about each
     * commit backwards along the commit tree until the initial commit,
     * following the first parent commit links, ignoring any second parents
     * found in merge commits. For every node in this history, the information
     * it should display is the commit id, the time the commit was made, and
     * the commit message.
     * Format:
     * ===
     * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     * Date: Thu Nov 9 20:00:05 2017 -0800
     * A commit message.
     *
     * For Merge Commits:
     * ===
     * commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
     * Merge: 4975af1 2c1ead1
     * Date: Sat Nov 11 12:30:00 2017 -0800
     * Merged development into master.
     */
    public static void log() {
        String commitUID = getCurrentCommit();
        Commit currCo = Commit.fromFile(commitUID);
        while (currCo != null) {
            System.out.println("===");
            System.out.println(String.format("commit %s", currCo.getUID()));
            System.out.println(String.format("Date: %s", currCo.getDatetime()));
            System.out.println(String.format("%s", currCo.getMessage()));
            System.out.println();
            String parent = currCo.getParent();
            if (parent != null) {
                currCo = Commit.fromFile(parent);
            } else {
                break;
            }
        }
    }

    /**
     * Displays information about all commits ever made. The order of the
     * commits does not matter. Same format as log.
     */
    public static void globalLog() {
        String commitUID = getCurrentCommit();
        File gitletCommits = new File(GITLET_FOLDER, "commits");
        File[] commitFiles = gitletCommits.listFiles();
        if (commitFiles != null) {
            for (File f: commitFiles) {
                Commit co = Commit.fromFile(f.getName());
                System.out.println("===");
                System.out.println(String.format("commit %s", co.getUID()));
                System.out.println(String.format("Date: %s", co.getDatetime()));
                System.out.println(String.format("%s", co.getMessage()));
                System.out.println();
            }
        }
    }

    /**
     * Prints out the ids of all commits that have the given commit message,
     * one per line. If there are multiple such commits, it prints the ids
     * out on separate lines.
     * @param message String
     */
    public static void find(String message) {
        File gitletCommit = new File(GITLET_FOLDER, "commits");
        boolean found = false;
        List<String> fileList = Utils.plainFilenamesIn(gitletCommit);
        if (fileList != null) {
            for (String name: fileList) {
                Commit co = Commit.fromFile(name);
                if (co.getMessage().equals(message)) {
                    System.out.println(co.getUID());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * Displays what branches currently exist, and marks the current branch
     * with a *. Also displays what files have been staged for addition or
     * removal. An example of the exact format it should follow is as follows.
     * === Branches ===
     * *master
     *
     * === Staged Files ===
     * wug.txt
     *
     * === Removed Files ===
     * goodbye.txt
     *
     * === Modifications Not Staged For Commit ===
     * wug3.txt (modified)
     *
     * === Untracked Files ===
     * random.stuff
     */
    public static void status() {
        String currBranch = getHeadFile();
        System.out.println("=== Branches ===");
        File branchPath = Utils.join(GITLET_FOLDER, "refs", "branches");
        List<String> brList = Utils.plainFilenamesIn(branchPath);
        if (brList != null) {
            for (String f: brList) {
                if (f.equals(currBranch)) {
                    System.out.println("*" + f);
                } else {
                    System.out.println(f);
                }
            }
        }
        System.out.println(String.format("\n=== Staged Files ==="));
        File stagedPath = Utils.join(GITLET_FOLDER, "staging");
        List<String> stList = Utils.plainFilenamesIn(stagedPath);
        if (stList != null) {
            for (String f: stList) {
                System.out.println(f);
            }
        }

        System.out.println(String.format("\n=== Removed Files ==="));
        File removedPath = Utils.join(GITLET_FOLDER, "removal");
        List<String> rmList = Utils.plainFilenamesIn(removedPath);
        if (rmList != null) {
            for (String f: rmList) {
                System.out.println(f);
            }
        }
        statusHelper();
    }

    /**
     * Helper function for status for Extra functionality.
     */
    public static void statusHelper() {
        System.out.println(String.format("\n=== Modifications Not Staged "
                + "For Commit ==="));
        String currCoID = getCurrentCommit();
        Commit currCo = Commit.fromFile(currCoID);
        HashMap<String, String> currTracked = currCo.getTrackedFiles();
        for (String name: currTracked.keySet()) {
            File cwdPt = Utils.join(CWD, name);
            File stagePt = Utils.join(GITLET_FOLDER, "staging", name);
            File removalPt = Utils.join(GITLET_FOLDER, "removal", name);
            String cwdHash;
            String stageHash;
            if (cwdPt.exists()) {
                cwdHash = Utils.sha1(Utils.readContentsAsString(cwdPt));
                if (!cwdHash.equals(currTracked.get(name))
                        && !stagePt.exists()) {
                    System.out.println(name + " (modified)");
                }

            } else if (stagePt.exists()) {
                stageHash = Utils.sha1(Utils.readContentsAsString(stagePt));
                if (!stageHash.equals(currTracked.get(name))) {
                    System.out.println(name + " (modified)");
                }
                if (!cwdPt.exists()) {
                    System.out.println(name + " (deleted)");
                }
            }

            if (!cwdPt.exists() && !removalPt.exists()) {
                System.out.println(name + " (deleted)");
            }
        }
        System.out.println(String.format("\n=== Untracked Files ==="));
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);

        if (cwdFiles != null) {
            for (String n: cwdFiles) {
                File stagePt = Utils.join(GITLET_FOLDER, "staging", n);
                File removalPt = Utils.join(GITLET_FOLDER, "removal", n);
                boolean flag = removalPt.exists()
                        || (!currTracked.containsKey(n) && !stagePt.exists());
                if (flag) {
                    System.out.println(n);
                }
            }
        }
    }

    /**
     * Creates a new branch with the given name, and points it at the current
     * head node. A branch is nothing more than a name for a reference
     * (a SHA-1 identifier) to a commit node. This command does NOT immediately
     * switch to the newly created branch (just as in real Git). Before you
     * ever call branch, your code should be running with a default branch
     * called "master".
     * @param branchName String
     */
    public static void branch(String branchName) {
        String currBranch = getHeadFile();
        String currCo = getCurrentCommit();
        File refBr = Utils.join(GITLET_FOLDER, "refs", "branches", branchName);
        if (branchName.equals(currBranch) || refBr.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        File newBr = Utils.join(GITLET_FOLDER, "refs", "branches", branchName);
        Utils.writeContents(newBr, currCo);
    }

    /**
     * Deletes the branch with the given name. This only means to delete the
     * pointer associated with the branch; it does not mean to delete all
     * commits that were created under the branch, or anything like that.
     * @param branchName String
     */
    public static void removeBranch(String branchName) {
        String currBranch = getHeadFile();
        if (branchName.equals(currBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File path = Utils.join(GITLET_FOLDER, "refs", "branches", branchName);
        if (!path.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        path.delete();
    }

    /**
     * Checks out all the files tracked by the given commit. Removes tracked
     * files that are not present in that commit. Also moves the current
     * branch's head to that commit node. See the intro for an example of what
     * happens to the head pointer after using reset. The [commit id] may be
     * abbreviated as for checkout. The staging area is cleared. The command is
     * essentially checkout of an arbitrary commit that also changes the current
     * branch head.
     * @param commitID String
     */
    public static void reset(String commitID) {
        Commit givenCo = Commit.fromFile(commitID);
        HashMap<String, String> givenTracked = givenCo.getTrackedFiles();

        String currBranch = getHeadFile();
        String currCoUID = getCurrentCommit();
        Commit currCo = Commit.fromFile(currCoUID);
        HashMap<String, String> currTracked = currCo.getTrackedFiles();
        HashMap<String, String> tempCurrTrack = new HashMap<>(currTracked);

        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);
        if (cwdFiles != null) {
            for (String name: cwdFiles) {
                File f = Utils.join(CWD, name);
                boolean inGiven = givenTracked.containsKey(name);
                boolean inCurr = currTracked.containsKey(name);
                String hash = Utils.sha1(Utils.readContentsAsString(f));
                String brHash = givenTracked.get(name);
                if (inGiven && !inCurr && !hash.equals(brHash)) {

                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);

                }
            }
        }

        Set<String> currKeySet = currTracked.keySet();
        for (String file: currKeySet) {
            File path = Utils.join(CWD, file);
            if (!givenTracked.containsKey(file)) {
                path.delete();
            }
        }
        currCo.setTrackedFiles(tempCurrTrack);
        for (String f: givenTracked.keySet()) {
            checkout(commitID, f);
        }

        saveReference(commitID, currBranch);


        File[] staging = new File(GITLET_FOLDER, "staging").listFiles();
        if (staging != null) {
            for (File f: staging) {
                f.delete();
            }
        }

        File[] removal = new File(GITLET_FOLDER, "removal").listFiles();
        if (removal != null) {
            for (File f: removal) {
                f.delete();
            }
        }
    }

    /**
     * Merges files from the given branch into the current branch.
     * @param branch String
     */
    public static void merge(String branch) {
        String currBranch = getHeadFile();
        String currCo = getCurrentCommit();

        if (branch.equals(currBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        File findBr = Utils.join(GITLET_FOLDER, "refs", "branches", branch);
        if (!findBr.exists()) {
            System.out.println("A branch with that name does not exist.");
        }
    }


}
