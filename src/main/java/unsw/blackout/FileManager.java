package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;

//If a device is transferring to a satellite that teleports mid-transfer, all 't' bytes will be deleted from the device
// and the transfer will be cancelled
//
//If a teleporting satellite is transferring to a device and teleports mid-transfer, the download is completed
// instantly and all 't' bytes are removed from the device but kept on the satellite
//
//The behaviour from satellite to satellite should be same as sending file from satellite to device.
// The download is completed instantly and all 't' bytes are removed from the device but kept on the satellite.
public class FileManager {
    private int remainingFiles;
    private int remainingBytes;
    private int remainingBandwidth;
    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<HashMap<Entity, File>> filesTransferring = new ArrayList<>();

    public FileManager(int maxFiles, int maxBytes, int maxBandwidth) {
        this.remainingFiles = maxFiles;
        this.remainingBytes = maxBytes;
        this.remainingBandwidth = maxBandwidth;
    }

    public void addFile(File file) {
        files.add(file);
    }
    public void addFileToTransfer(File file, Entity dest) {

    }

    public void newTransfer(File file, Entity destination) {

    }
    public void transferNextBytes(int numBytes) {

    }

    public int getRemainingFiles() {
        return remainingFiles;
    }

    public int getRemainingBytes() {
        return remainingBytes;
    }

    public int getRemainingBandwidth() {
        return remainingBandwidth;
    }

    public ArrayList<File> getFiles() {
        return files;
    }
}
