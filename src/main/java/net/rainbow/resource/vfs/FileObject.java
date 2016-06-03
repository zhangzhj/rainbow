package net.rainbow.resource.vfs;


import java.io.IOException;
import java.net.URL;

public interface FileObject {

    /**
     * 该文件是否存在
     * 
     * @return
     * @throws IOException
     */
    boolean exists() throws IOException;

    /**
     * 返回该文件或实体的名称对象
     * 
     * @return
     * @throws IOException
     */
    FileName getName() throws IOException;

    /**
     * 返回子文件或实体(包括目录)
     * 
     * @return
     * @throws IOException
     */
    FileObject[] getChildren() throws IOException;

    /**
     * 文件的类型，目录或文件。jar文件里面实体也可能是目录或文件。
     * 
     * @return
     * @throws IOException
     */
    FileType getType() throws IOException;

    /**
     * 返回该文件或实体的URL对象
     * 
     * @return
     * @throws IOException
     */
    URL getURL() throws IOException;

    /**
     * 父目录对象
     * 
     * @return
     * @throws IOException
     */
    FileObject getParent() throws IOException;

    /**
     * 子文件或实体
     * 
     * @param name
     * @return
     * @throws IOException
     */
    FileObject getChild(String name) throws IOException;

    /**
     * 文件实体内容，如果是目录将抛出IOE异常
     * 
     * @return
     * @throws IOException
     */
    FileContent getContent() throws IOException;

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

}
