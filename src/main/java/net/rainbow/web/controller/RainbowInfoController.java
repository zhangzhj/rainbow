package net.rainbow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.rainbow.RainConstants;
import net.rainbow.web.Invocation;
import net.rainbow.web.annotation.Ignored;
import net.rainbow.web.annotation.MappingPath;
import net.rainbow.web.impl.Mapping;
import net.rainbow.web.ref.ControllerRef;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @Description: 用于获取运行中的Controller信息
 * 
 * 
 * @author (sean)zhijun.zhang@gmail.com
 * @date 2011-8-29
 * @version V1.0
 */
public class RainbowInfoController implements RainConstants {

	protected Log logger = LogFactory.getLog("net.rainbow");

	@Ignored
	@MappingPath("/rainbow-controllers")
	public String controllers(Invocation inv) {
		Object infos = inv.getServletContext().getAttribute(
				"$$rainbow-controller-infos");
		if (infos == null) {

			// 不显示自己的工具方法
			List<Class<?>> passControllers = new ArrayList<Class<?>>();
			passControllers.add(DefaultHtmlController.class);
			passControllers.add(RainbowInfoController.class);
			@SuppressWarnings("unchecked")
			List<Mapping> mappings = (List<Mapping>) inv.getServletContext()
					.getAttribute(MAPPING_CONTEXT);
			StringBuffer sb = new StringBuffer(1000);
			Map<ControllerRef, List<Mapping>> mappingRef = new HashMap<ControllerRef, List<Mapping>>();
			for (Mapping mapping : mappings) {
				List<Mapping> ls = null;
				if (passControllers.contains(mapping.getNode().getRef()
						.getControllerClass())) {
					continue;
				}
				if (mappingRef.containsKey(mapping.getNode().getRef())) {
					ls = mappingRef.get(mapping.getNode().getRef());
					ls.add(mapping);
				} else {
					ls = new ArrayList<Mapping>();
					ls.add(mapping);
				}
				mappingRef.put(mapping.getNode().getRef(), ls);
			}

			sb.append("<root>");
			ControllerRef ref = null;
			List<Mapping> ls = null;
			for (Iterator<ControllerRef> it = mappingRef.keySet().iterator(); it
					.hasNext();) {
				ref = it.next();

				String contollerName = "";
				if (!StringUtils.isBlank(ref.getRelationPath())) {
					contollerName = contollerName + ref.getRelationPath();
				}
				contollerName = contollerName + "/" + ref.getControllerName();

				sb.append("<controller controllerName='").append(contollerName)
						.append("'").append(" clazz='").append(
								ref.getControllerClass().getName()).append("'")
						.append(">");

				ls = mappingRef.get(ref);
				for (Mapping mapping : ls) {
					sb.append("<mapping path='").append(mapping.getUrl())
							.append("'");
					sb.append(" method='").append(
							ref.getControllerClass().getName()
									+ "."
									+ mapping.getNode().getMref().getMethod()
											.getName()).append("' />");
				}
				sb.append("</controller>");
			}
			sb.append("</root>");

			infos = sb.toString();
			inv.getServletContext().setAttribute("$$rainbow-controller-infos",
					infos);

		}

		inv.getResponse().setContentType("text/xml");
		return "@:" + (String) infos;
	}
}
