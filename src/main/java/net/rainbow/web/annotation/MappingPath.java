package net.rainbow.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 主要标志完全按照字段方式进行解析
 * 
 * <p>
 * Controller的主要生成URL方式其中MappingPath可以直接用正则表达式的写法
 * 
 * 然后在方法中按照匹配的顺序直接写入方法中的参数，其中，可以使用简单的数据类型
 * <p>
 * 
 * 例如：
 * <code>
 * @MappingPath("index-([0-9]*)-([0-9]*)-([a-z]*).html")
 * public String index(int type,int page,String name){
 * 		   
 * }
 * </code>
 * 对应关系
 * type ： 第一个([0-9]*)
 * page ： 第二个([0-9]*)
 * name ：([a-z]*)
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-24 
 * @version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingPath {
	public String value();
}
