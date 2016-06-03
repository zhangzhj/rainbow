package net.rainbow.web.controller;

import net.rainbow.web.Invocation;
import net.rainbow.web.annotation.MappingPath;

public class DefaultHtmlController {
	
	@MappingPath("(.*)\\.html")
	public String index(Invocation inv,String path){
		return path;
	}
}
