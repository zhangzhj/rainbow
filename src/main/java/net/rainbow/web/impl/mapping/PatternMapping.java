package net.rainbow.web.impl.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.rainbow.RainConstants;
import net.rainbow.web.impl.MappingResult;
import net.rainbow.web.impl.MappingResultImpl;
import net.rainbow.web.impl.RequestPath;

import org.apache.commons.lang.StringUtils;
/**
 * 使用正则表达式自动生成，主要用于简单的脚本语言，其中自定义环境，参数描述可以直接使用
 *
 * @author (sean)zhijun.zhang@gmail.com
 */
public class PatternMapping extends AbstractMapping implements RainConstants{

	private Pattern pattern;
	private String url;
	private MappingNode node;
	
	public PatternMapping(String url, MappingNode node) {
		setUrl(url);
		setNode(node);
		//默认为根目录处理
		if(!StringUtils.isBlank(url) && !url.startsWith(PATH_DOT)){
			url = PATH_DOT + url;
		}
		this.pattern = Pattern.compile("^"+url+"$");
	}
	
	@Override
	public MappingResult _match(RequestPath path) {
		MappingResult result = null;
		Matcher matcher = pattern.matcher(path.getActionPath());
		if(matcher.find()){
			String[] vs = null;
			int count = matcher.groupCount();
			if(count > 0){
				vs = new String[count];
				for (int i = 1; i <= count; i++) {
					vs[i-1] = matcher.group(i);
				}
			}
			result =  new MappingResultImpl(node,url,vs);
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this)
            return true;
        if (obj instanceof PatternMapping) {
            return ((PatternMapping)obj).url.equals(this.url);
        }
        return false;
	}
	
	
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MappingNode getNode() {
		return node;
	}

	public void setNode(MappingNode node) {
		this.node = node;
	}
	
	public static void main(String[] args) {
		Pattern p = Pattern.compile("^"+"/index.html"+"$");
		Matcher m = p.matcher("/index.htm");
		System.out.println(m.find());
		System.out.println(m.groupCount());
	}
}
