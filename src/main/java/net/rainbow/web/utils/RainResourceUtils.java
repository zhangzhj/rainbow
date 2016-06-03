package net.rainbow.web.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import net.rainbow.web.ref.ResourceRef;

public class RainResourceUtils {

	public static String converterFile(ResourceRef ref) throws IOException {
		String urlString = "";
		if (ref != null) {
			File resourceFile = ref.getResource().getFile();

			if ("jar".equals(ref.getProtocol()))
				urlString = ResourceUtils.URL_PROTOCOL_JAR + ":"
						+ resourceFile.toURI()
						+ ResourceUtils.JAR_URL_SEPARATOR;
			else
				urlString = resourceFile.toURI().toString();
		}
		return urlString;
	}
}
