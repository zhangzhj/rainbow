package net.rainbow.web.impl.view;

import java.io.IOException;

import net.rainbow.web.Invocation;

public interface ViewDispatcher {

	public void render(Invocation inv, String viewPath) throws IOException;
	
	String jspViewResolverName = "jspViewResolver";
    String velocityViewResolverName = "velocityViewResolver";
}
