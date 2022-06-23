package unsw.blackout;

public class File {
    private String fileName;
    private String content;
    private int accessibleBytes = 0;
    private boolean isTransferred = false;

    /**
     * Construct a file that isn't transferred, and no contents are readable.
     * Creates a deep copy of given File object.
     *
     * @param file
     */
    public File(File file) {
        this.fileName = file.getFileName();
        this.content = file.getContent();
        this.isTransferred = false;
    }

    /**
     * Construct a file that is transferred, and all contents are readable
     *
     * @param fileName
     * @param content
     */
    public File(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.accessibleBytes = content.length();
        this.isTransferred = true;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Get the content currently available
     *
     * @return
     */
    public String getContent() {
        return content.substring(0, accessibleBytes);
    }

    public void makeNextBytesAvailable(int numBytes) {
        if (isTransferred) return;
        accessibleBytes += numBytes;
        if (accessibleBytes >= content.length()) {
            accessibleBytes = content.length();
            isTransferred = true;
        }
    }

    /**
     * Get the size of the content after finishing transferring
     *
     * @return
     */
    public int getSize() {
        return content.length();
    }

    public boolean isTransferred() {
        return isTransferred;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
