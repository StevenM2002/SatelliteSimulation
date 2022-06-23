package unsw.blackout;

public class Pipeline {
    private File sourceFile;
    private String sendingId;
    private File destFile;
    private String receivingId;
    private boolean isTransferred = false;

    public Pipeline(File sourceFile, String sendingId, String receivingId) {
//        this.sourceFile = new File(sourceFile);
        this.sendingId = sendingId;
        this.receivingId = receivingId;
//        destFile = new File(sourceFile.getFileName());
    }

    public boolean isTransferred() {
        return isTransferred;
    }
    public void sendNextBytes() {

    }
    public void removeBytesT() {

    }
    public void sendAllNow() {

    }
}
