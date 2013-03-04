package com.gmail.nossr50.mcmmopermgen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

public class Main {
	public static void main(String[] args) {
		if(args.length != 5) {
			System.out.println("Usage is: java -jar mcMMO-perms-gen.jar [path to plugin.yml] [path to main.template] [path to child.template] [path to parent.template] [path to child_list.template]");
			return;
		}

		String pluginYamlPath = args[0];
		String mainTemplatePath = args[1];
		String childTemplatePath = args[2];
		String parentTemplatePath = args[3];
		String childListTemplatePath = args[4];

		String pluginYaml = null;
		String mainTemplate = null;
		String childTemplate = null;
		String parentTamplate = null;
		String childListTemplate = null;

		try {
			pluginYaml = readFileAsString(pluginYamlPath);
			mainTemplate = readFileAsString(mainTemplatePath);
			childTemplate = readFileAsString(childTemplatePath);
			parentTamplate = readFileAsString(parentTemplatePath);
			childListTemplate = readFileAsString(childListTemplatePath);
		} catch(IOException e) {
			e.printStackTrace();
			return;
		} finally {
			if(pluginYaml == null || mainTemplate == null || childTemplate == null || parentTamplate == null || childListTemplate == null) {
				System.out.println("Could not read all files");
				return;
			}
		}

		Yaml pluginData = new Yaml();
		Map<?, ?> map = (Map<?, ?>) pluginData.load(pluginYaml);

		for(Object entrySet : map.entrySet()) {
			Entry<?, ?> entry = (Entry<?, ?>) entrySet;
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}

	private static String readFileAsString(String fileName) throws IOException {
		java.io.InputStream is = new FileInputStream(fileName);
		try {
			final int bufsize = 4096;
			int available = is.available();
			byte[] data = new byte[available < bufsize ? bufsize : available];
			int used = 0;

			while(true) {
				if(data.length - used < bufsize) {
					byte[] newData = new byte[data.length << 1];
					System.arraycopy(data, 0, newData, 0, used);
					data = newData;
				}

				int got = is.read(data, used, data.length - used);

				if(got <= 0) {
					break;
				}

				used += got;
			}

			return new String(data, 0, used);
		} finally {
			is.close();
		}
	}
}
