package unsw.blackout.files;

import unsw.blackout.FileTransferException;
import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Satellite;

import java.util.ArrayList;

public class FileManagement {
    private ArrayList<File> transferred = new ArrayList<>();
    private ArrayList<FilesTransferring> transferringFiles = new ArrayList<>();

    public void transferFile(Device source, Satellite destination, String fileName) {
        for (var each : source.getFiles())
        var newTransfer = new FilesTransferring();
    }
}
