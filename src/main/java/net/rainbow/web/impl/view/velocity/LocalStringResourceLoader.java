package net.rainbow.web.impl.view.velocity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
/**
 * 简单的使用String的resource的处理类
 * 
 * @author Sean zhang.zhj85@gmail.com
 */
public class LocalStringResourceLoader extends ResourceLoader{

	@Override
	public long getLastModified(Resource arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InputStream getResourceStream(String arg0)
			throws ResourceNotFoundException {
		InputStream result = null;     
		if (arg0 == null || arg0.length() == 0) {     
		    throw new ResourceNotFoundException("not define template!");     
		}     
		try {
			result = new ByteArrayInputStream(arg0.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ResourceNotFoundException("can't support the charset");
		}     
		return result;     
	}

	@Override
	public void init(ExtendedProperties arg0) {
		
	}

	@Override
	public boolean isSourceModified(Resource arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
