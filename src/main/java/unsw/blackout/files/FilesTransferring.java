package unsw.blackout.files;

import java.util.ArrayList;

public class FilesTransferring {
    private File transferFrom;
    private File transferTo;

    private boolean isFinishedTransfer = false;

    public FilesTransferring(File transferFrom) {
        this.transferFrom = new File(transferFrom.getFilename(), transferFrom.getContent(), false);
        this.transferTo = new File(transferTo.getFilename());
    }
    public void removeTBytes() {
        String tRemovedContent = transferFrom.getContent().replaceAll("t", "");
        transferFrom.setContent(tRemovedContent);
    }
    private void transferOneByte() {
        if (!transferFrom.getContent().isEmpty()) {
            String toTransfer = Character.toString(transferFrom.getContent().charAt(0));
            transferTo.setContent(transferTo.getContent().concat(toTransfer));
        } else {
            isFinishedTransfer = true;
        }
    }
    public void transferNextBytes(int numBytes) {
        for (int i = 0; i < numBytes && !isFinishedTransfer; i++) {
            transferOneByte();
        }
    }

    public boolean isFinishedTransfer() {
        return isFinishedTransfer;
    }
}
