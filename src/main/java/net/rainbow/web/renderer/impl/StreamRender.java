package net.rainbow.web.renderer.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.rainbow.web.Invocation;
import net.rainbow.web.renderer.Renderer;

public class StreamRender implements Renderer{

	
	private InputStream is;
	
	public StreamRender(InputStream is){
		this.is = is;
	}
	@Override
	public void render(Invocation inv) throws Exception {
		HttpServletResponse response = inv.getResponse();
		if(response.getContentType() == null){
			response.setContentType("application/octet-stream");
		}
        response.setContentLength(is.available());
        InputStream input = null;
        try {
            input = new BufferedInputStream(is);
            OutputStream output = response.getOutputStream();
            byte[] buffer = new byte[4096];
            for (;;) {
                int n = input.read(buffer);
                if (n==(-1))
                    break;
                output.write(buffer, 0, n);
            }
            output.flush();
        }
        finally {
            if (input!=null) {
                input.close();
            }
        }
	}
}
