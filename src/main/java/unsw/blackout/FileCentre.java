package unsw.blackout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FileCentre {
    // Ok so if it is FILEALREADYEXISTS then the file exists on the entity you want to transfer to.
    // Sat1 : Sat2 : File1
    // Sat2 : Dev1 : File1
    // Dev1 : Dev1 : File1
    //
    /*
        HashMap<EntityThatTheFilesExistOn, <EntityFromWhereTheFilesCameFrom, [ListOfTheFiles]>
        {
            ToEntity: {
                FromEntity : [Files],
                FromEntity : [Files],
                FromEntity : [Files],
            },
            ToEntity: {
                FromEntity : [Files],
                FromEntity : [Files],
                FromEntity : [Files],
            },
        }
     */
    /*
            [{
                FromEntity : [Files],
                FromEntity : [Files],
                FromEntity : [Files],
            },
            {
                FromEntity : [Files],
                FromEntity : [Files],
                FromEntity : [Files],
            }]
     */
    // We can know that there will never be an overlap in files because we will check that
    // i.e. {Satellite1 : [File1], Satellite 2 : [File1]} will never exist
    private HashMap<Entity, HashMap<Entity, ArrayList<File>>> allFiles = new HashMap<>();
    private int getTotalBytesOfFiles(Entity entity) {
        int totalBytes = 0;
        var entityFiles = allFiles.get(entity).values();
        for (ArrayList<File> files : entityFiles) {
            for (File file : files) {
                totalBytes += file.getSize();
            }
        }
        return totalBytes;
    }
    private int getTotalNumFiles(Entity entity) {
        int totalFiles = 0;
        var entityFiles = allFiles.get(entity).values();
        for (ArrayList<File> files : entityFiles) {
            totalFiles += files.size();
        }
        return totalFiles;
    }

    /**
     * Gets the file regardless if it is fully transferred on entity
     * @param entity
     * @param fileName
     * @return null if it doesn't exist else file
     */
    private File getFileFromEntity(Entity entity, String fileName) {

        File ret = null;

        Collection<ArrayList<File>> entityFiles;
        // If entity has not been inducted into the system
        try {
            entityFiles = allFiles.get(entity).values();
        } catch (NullPointerException e) {
            return null;
        }

        for (ArrayList<File> files : entityFiles) {
            if (ret != null) return ret;
            ret = files
                    .stream()
                    .filter(file -> file.getFileName().equals(fileName))
                    .findFirst()
                    .orElse(null);
        }
        return ret;
    }

    private int getNumberFilesBeingTransferredOutwards(Entity entity) {
        int numFiles = 0;
        // Key: Entity where the list of files came from
        // Value: List of files
        for (HashMap<Entity, ArrayList<File>> previousOwnerAndFiles : allFiles.values()) {
            var filesPreviouslyOwnedByGivenEntity = previousOwnerAndFiles.get(entity);
            // Count the files not yet transferred from the given entity aka, those being transferred by given entity
            if (filesPreviouslyOwnedByGivenEntity != null) {
                numFiles += filesPreviouslyOwnedByGivenEntity.stream().filter(file -> !file.isTransferred()).count();
            }
        }
        return numFiles;
    }

    private int getNumberFilesBeingTransferredInwards(Entity entity) {
        int numFiles = 0;
        var filesThatExistOnTheEntity = allFiles.get(entity).values();
        for (ArrayList<File> filesOnEntity : filesThatExistOnTheEntity) {
            // Count the files not yet received to the given entity, aka files transferred inwards
            if (filesOnEntity != null) {
                numFiles += filesOnEntity.stream().filter(file -> !file.isTransferred()).count();
            }
        }
        return numFiles;
    }

    private void checkExceptions(Entity from, Entity to, String fileName) throws FileTransferException {

        File file = getFileFromEntity(from, fileName);

        // Check file exits on entity which is going to transfer to another device
        if (file == null || !file.isTransferred()) {
            throw new FileTransferException.VirtualFileNotFoundException(fileName);
        }
        // Checks file doesn't exist on entity to be transferred to regardless of transferred state
        if (getFileFromEntity(to, fileName) != null) {
            throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
        }
        // Check total bytes won't be exceeded if it is transferred in

        if (getTotalBytesOfFiles(to) + file.getSize() > to.getMaxStorageBytes()) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Storage Reached");
        }
        // Check total number files won't be exceeded if it is transferred in
        if (getTotalNumFiles(to) + 1 > to.getMaxStorageFiles()) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Files Reached");
        }
        // Check bandwidth outgoing bandwidth isn't full
        if (getNumberFilesBeingTransferredOutwards(from) + 1 > from.getMaxOutgoing()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(from.getId());
        }
        // Check bandwidth incoming bandwidth isn't full
        if (getNumberFilesBeingTransferredInwards(to) + 1 > to.getMaxIncoming()) {
            throw new FileTransferException.VirtualFileNoBandwidthException(to.getId());
        }
    }

    public void sendFile(Entity from, Entity to, String fileName) throws FileTransferException {

        // Make sure toEntity: {} exists
        if (!allFiles.containsKey(to)) {
            allFiles.put(to, new HashMap<>());
        }
        // Make sure toEntity: {fromEntity: []} exists
        if (!allFiles.get(to).containsKey(from)) {
            allFiles.get(to).put(from, new ArrayList<>());
        }
        checkExceptions(from, to, fileName);
        File file = getFileFromEntity(from, fileName);
        allFiles.get(to).get(from).add(new File(file));
    }
    public void updateTransfer(ArrayList<Entity> allEntities) {
        // Sat1 : Dev1 : File1, Dev2 : File2
        // Remove all that cannot be transferred
        System.out.println(allFiles);
        allFiles
                .entrySet()
                .forEach(toEntityEntry ->
                        toEntityEntry
                            .getValue()
                            .entrySet()
                            .forEach(fromEntityEntry -> {
                                        System.out.println(fromEntityEntry.getKey());
                                        System.out.println(toEntityEntry.getKey());
//                                        System.out.println(!communicableEntities(fromEntityEntry.getKey(), allEntities).contains(toEntityEntry.getKey()));
                                        fromEntityEntry
                                                .getValue()
                                                .removeIf(file -> {
                                                    System.out.println(file);
                                                    System.out.println(!file.isTransferred() &&
                                                            !communicableEntities(fromEntityEntry.getKey(), allEntities).contains(toEntityEntry.getKey()));
                                                    return !file.isTransferred() &&
                                                        !communicableEntities(fromEntityEntry.getKey(), allEntities).contains(toEntityEntry.getKey());
                                                });
                                    }
//                                !communicableEntities(fromEntityEntry.getKey(), allEntities).contains(toEntityEntry.getKey())
                            )
                );
        // To calculate the bandwidth, I need floor(max bandwidth / files being sent)
        // First retrieve the owner of the files, and the previous owner of the files.
        // Calculate their available bandwidth and then advance each of those files by the bandwidth
        allFiles
                .entrySet()
                .forEach(toEntityEntry ->
                        toEntityEntry
                            .getValue()
                            .entrySet()
                            .forEach(fromEntityEntry -> {
                                Entity toEntity = toEntityEntry.getKey();
                                Entity fromEntity = fromEntityEntry.getKey();
                                int outgoingFiles = getNumberFilesBeingTransferredOutwards(fromEntity);
                                int incomingFiles = getNumberFilesBeingTransferredInwards(toEntity);
                                // Get the smallest of each
                                int outBandwidth = outgoingFiles != 0 ? fromEntity.getMaxOutgoing() / outgoingFiles : 0;
                                int inBandwidth = incomingFiles != 0 ? toEntity.getMaxIncoming() / incomingFiles : 0;
                                int bytesToSend = Math.min(inBandwidth, outBandwidth);
                                fromEntityEntry.getValue().forEach(file -> file.makeNextBytesAvailable(bytesToSend));
                            })
                );
    }
    private ArrayList<Entity> communicableEntities(Entity source, ArrayList<Entity> allEntities) {
        var ret = source.getAllCommunicableEntities(allEntities);
        ret.removeIf(entity -> entity.getType().equals("RelaySatellite"));
        return ret;
    }
    public void addFileInstantly(Entity to, File file) {


        if (!allFiles.containsKey(to)) {
            allFiles.put(to, new HashMap<>());
        }
        // Make sure toEntity: {fromEntity: []} exists
        if (!allFiles.get(to).containsKey(to)) {
            allFiles.get(to).put(to, new ArrayList<>());
        }
        allFiles.get(to).get(to).add(file);
    }
    public ArrayList<File> getFiles(Entity entity) {
        if (allFiles.get(entity) == null) return new ArrayList<>();
        return allFiles
                .get(entity)
                .values()
                .stream()
                .reduce(new ArrayList<>(), (files, files2) -> {
                    files2.addAll(files);
                    return files2;
                });
    }
    // If device is transferring to this, and teleport then remove all t from device and cancel.
    // If this is transferring to device, download is completed instantly and all t bytes removed from device only
    // If this is transferring to satellite, download is completed instantly and all t bytes removed from other satellite only
//    public void checkSatelliteTeleport(TeleportingSatellite teleportingSatellite) {
//
//        // Get all the files in which this is transferring to other
//        // Get all the files in which device is transferring to this
//        // Then modify files
//
//        // not finished transferring, from and to doesn't matter
//        // get all not finished transferring from the toEntity = teleportingSatellite :: ArrayList<HashMap<fromEntity, ArrayList<Files>>
//        //  And then Remove All T Bytes From Device And Cancel File Transfer
//        // get all not finished transferring from the fromEntity = teleportingSatellite :: ArrayList<Files> as this is the one transferring to
//        var filesThatExistOnTheEntity = allFiles.get(teleportingSatellite).entrySet();
//        filesThatExistOnTheEntity.forEach(entityArrayListEntry -> {
//            Entity fromEntity = entityArrayListEntry.getKey();
//            ArrayList<File> files = entityArrayListEntry.getValue();
//            if (files != null) {
//                for (File file : files) {
//                    if (!file.isTransferred()) {
//                        File fileOnDevice = getFileFromEntity(fromEntity, file.getFileName());
//                        // Need to check if this works as I am doing it by pointer reference
//                        fileOnDevice.removeTBytes();
//                    }
//                }
//            }
//        });
//        filesThatExistOnTheEntity.forEach(entityArrayListEntry -> {
//            entityArrayListEntry.getValue().removeIf(file -> !file.isTransferred());
//        });
//
//        for (HashMap<Entity, ArrayList<File>> previousOwnerAndFiles : allFiles.values()) {
//            var filesPreviouslyOwnedByGivenEntity = previousOwnerAndFiles.get(teleportingSatellite);
//            if (filesPreviouslyOwnedByGivenEntity != null) {
//                filesPreviouslyOwnedByGivenEntity.stream().forEach(file -> {
//                    if (!file.isTransferred()) {
//                        file.removeTBytes();
//                    }
//                });
//            }
//        }
//    }
}
