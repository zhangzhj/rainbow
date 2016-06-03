package net.rainbow.web.impl.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rainbow.RainConstants;
import net.rainbow.web.annotation.MappingPath;
import net.rainbow.web.controller.DefaultHtmlController;
import net.rainbow.web.impl.Mapping;
import net.rainbow.web.impl.module.ResourceProvider;
import net.rainbow.web.ref.ControllerRef;
import net.rainbow.web.ref.MethodRef;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author (sean)zhijun.zhang@gmail.com
 */
public class MappingBuilderImpl implements MappingBuilder, RainConstants {

	protected static final Log logger = LogFactory.getLog(MappingBuilder.class);

	private List<Mapping> mappings = new ArrayList<Mapping>();

	@Override
	public synchronized List<Mapping> builder(WebApplicationContext context)
			throws Exception {
		
		ResourceProvider provider = ResourceProvider.getInstance();
		List<ControllerRef> refs = provider.provideControllerAndRegedit();
		//sort Controller des order
		refs = sort(refs);
		for (ControllerRef ref : refs) {
			List<MethodRef> ms = ref.getActions();
			if (ms != null) {
				for (MethodRef m : ms) {
					addMapping(ref, m);
				}
			}
		}
		return mappings;
	}

	private void addMapping(ControllerRef cref, MethodRef mref) {
		MappingPath path = mref.getMethod().getAnnotation(MappingPath.class);
		if (path != null) {
			addPatternMapping(path.value(), cref, mref);
		} else {
			addStatisMapping(cref, mref);
		}
	}

	private void addStatisMapping(ControllerRef cref, MethodRef mref) {
		StringBuffer path = new StringBuffer("");
		if (!StringUtils.isBlank(cref.getRelationPath())) {
			path.append(PATH_DOT + cref.getRelationPath());
		}
		path.append(PATH_DOT + cref.getControllerName());
		path.append(STATIC_MAPPING_DOT + mref.getMethodName());
		Mapping mapping = new StaticsMapping(path.toString(), new MappingNode(
				cref, mref));
		mappings.add(mapping);
	}

	private void addPatternMapping(String path, ControllerRef cref,
			MethodRef mref) {
		Mapping mapping = new PatternMapping(path, new MappingNode(cref, mref));
		mappings.add(mapping);
	}

	/** 特殊处理一下如果存在默认的Controller放到最后 */
	private List<ControllerRef> sort(List<ControllerRef> ControllerRefclazz) {
		if (ControllerRefclazz != null) {
			ControllerRef def = null;
			for (ControllerRef ref : ControllerRefclazz) {
				if (ref.getControllerClass() == DefaultHtmlController.class) {
					def = ref;
					break;
				}
			}
			if (def != null) {
				int index = ControllerRefclazz.indexOf(def);
				Collections.swap(ControllerRefclazz, index, ControllerRefclazz
						.size() - 1);
			}
		}
		return ControllerRefclazz;
	}

}