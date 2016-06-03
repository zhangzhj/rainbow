package net.rainbow.resource.vfs;


import java.io.IOException;

public class FileNameImpl implements FileName {

    private final FileObject fileObject;

    private final String baseName;

    public FileNameImpl(FileObject fileObject, String baseName) {
        this.fileObject = fileObject;
        this.baseName = baseName;
    }

    @Override
    public String getBaseName() {
        return baseName;
    }

    @Override
    public FileObject getFileObject() {
        return fileObject;
    }

    @Override
    public String getRelativeName(FileName subFileName) throws IOException {
        String basePath = fileObject.getURL().getPath();
        String subPath = subFileName.getFileObject().getURL().getPath();
        if (!subPath.startsWith(basePath)) {
            throw new IllegalArgumentException("basePath='" + basePath + "'; subPath='" + subPath
                    + "'");
        }
        return subPath.substring(basePath.length());
    }
}
