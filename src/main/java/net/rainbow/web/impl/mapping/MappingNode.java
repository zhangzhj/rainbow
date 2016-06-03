package net.rainbow.web.impl.mapping;

import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.MethodRef;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 主要用于Mapping的Matcher中用到的处理Node节点 ，其中包含整体的处理信息，
 * 
 * 包括Interceptor、Controller、Method、Parameter相关信息
 *
 * @author (sean)zhijun.zhang@gmail.com
 */
public class MappingNode {
	
	protected static final Log logger = LogFactory.getLog(MappingNode.class);
	
	private ControllerRef ref;
	private MethodRef mref;
	
	
	public MappingNode(ControllerRef ref, MethodRef mref) {
		this.ref = ref;
		this.mref = mref;
	}

	public ControllerRef getRef() {
		return ref;
	}

	public void setRef(ControllerRef ref) {
		this.ref = ref;
	}

	public MethodRef getMref() {
		return mref;
	}

	public void setMref(MethodRef mref) {
		this.mref = mref;
	}
}