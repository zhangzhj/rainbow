
package net.rainbow.resource.vfs;

import java.io.IOException;
import java.io.InputStream;
/**
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24 
 * @version V1.0
 */
public interface FileContent {

    public InputStream getInputStream() throws IOException;

}
