package net.rainbow.resource.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

public class SimpleFileObject implements FileObject {

    private final URL url;

    private final String urlString;

    private final File file;

    private final FileName fileName;

    private final FileSystemManager fs;

    SimpleFileObject(FileSystemManager fs, URL url) throws FileNotFoundException,
            MalformedURLException {
        this.fs = fs;
        File file = ResourceUtils.getFile(url);
        String urlString = url.toString();
        this.url = url;
        this.file = file;
        this.urlString = urlString;
        this.fileName = new FileNameImpl(this, file.getName());
    }

    @Override
    public FileObject getChild(final String child) throws IOException {
        return fs.resolveFile(urlString + child);
    }

    @Override
    public FileObject[] getChildren() throws MalformedURLException, IOException {
        File[] files = file.listFiles();
        List<FileObject> children = new ArrayList<FileObject>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                children.add(fs.resolveFile(urlString + files[i].getName() + "/"));
            } else {
            	String t = urlString + files[i].getName();
            	//为了一个变态的编译问题
            	if(t.indexOf(".java") == -1){
            		children.add(fs.resolveFile(t));
            	}
            }
        }
        FileObject[] childrens = new FileObject[children.size()];
        children.toArray(childrens);
        return childrens;
    }

    @Override
    public FileContent getContent() throws IOException {
        if (!file.canRead()) {
            throw new IOException("can not read");
        }
        return new FileContent() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(file);
            }
        };
    }

    @Override
    public FileName getName() {
        return fileName;
    }

    @Override
    public FileObject getParent() throws MalformedURLException, IOException {
        File parent = file.getParentFile();
        if (parent == null) {
            return null;
        }
        return fs.resolveFile(parent.toURI().toURL());
    }

    @Override
    public FileType getType() {
        if (file.isFile()) {
            return FileType.FILE;
        } else if (file.isDirectory()) {
            return FileType.FOLDER;
        }
        return FileType.UNKNOWN;
    }

    @Override
    public URL getURL() throws MalformedURLException {
        return url;
    }

    @Override
    public boolean exists() throws IOException {
        return file.exists();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SimpleFileObject)) {
            return false;
        }
        SimpleFileObject t = (SimpleFileObject) obj;
        return this.file.equals(t.file);
    }

    @Override
    public int hashCode() {
        return file.hashCode() * 13;
    }

    @Override
    public String toString() {
        return urlString;
    }
}
