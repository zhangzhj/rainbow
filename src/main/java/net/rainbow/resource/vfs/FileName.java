package net.rainbow.resource.vfs;


import java.io.IOException;

public interface FileName {

    /**
     * 返回所代表的文件或实体对象
     * 
     * @return
     * @throws IOException
     */
    public FileObject getFileObject() throws IOException;

    /**
     * 基础文件名,即使是目录也不以'/'开始或结尾
     * 
     * @return
     * @throws IOException
     */
    public String getBaseName() throws IOException;

    /**
     * 一个下级文件或实体相对于本文件或实体的路径，得到的返回字符串不以'/'开始，如果subFileName是一个子目录的话，返回的结果将以
     * '/'结尾
     * 
     * @param subFileName
     * @return
     * @throws IOException
     */
    public String getRelativeName(FileName subFileName) throws IOException;

}
