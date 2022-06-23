package unsw.blackout;

public interface SendingAndReceiving {
    public void checkExceptions() throws FileTransferException;
    // bandwidth // numFiles
    public int getOutgoingBandwidth(int numFiles);
    public int getIncomingBandwidth(int numFiles);
}
