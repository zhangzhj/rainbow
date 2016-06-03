package net.rainbow.web.ve.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class CacheDirective extends Directive {

	@Override
	public String getName() {
		return "cache";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		// 获得缓存信息
		SimpleNode sn_key = (SimpleNode) node.jjtGetChild(0);
		String key = (String) sn_key.value(context);
		System.out.println(key);
		Node body = node.jjtGetChild(2);
		//检查内容是否有变化
		StringWriter sw = new StringWriter();
		body.render(context, sw);
		String cache_html = sw.toString();
		writer.write(cache_html);
		System.out.println(cache_html);
		return true;
	}

}
