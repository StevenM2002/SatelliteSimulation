package unsw.blackout.files;

import java.util.Objects;

public class File {
    private String filename;
    private String content;
    private boolean isTransferred;

    public File(String filename, String content, boolean isTransferred) {
        this.filename = filename;
        this.content = content;
        this.isTransferred = isTransferred;
    }

    /**
     * Constructor for empty file that is not finished transferring
     * @param filename
     */
    public File(String filename) {
        this.filename = filename;
        this.content = "";
        this.isTransferred = false;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public int getFileSize() {
        return content.length();
    }

    public boolean getIsTransferred() {
        return isTransferred;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsTransferred(boolean transferred) {
        isTransferred = transferred;
    }

    @Override
    public String toString() {
        return "File: " + "filename: " + filename + ", content: " + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return filename.equals(file.getFilename()) && content.equals(file.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, content);
    }
}
