package unsw.blackout;

import java.util.Objects;

public class File {
    private String filename;
    private String content;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
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
