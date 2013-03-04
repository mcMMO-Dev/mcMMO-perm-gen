package com.gmail.nossr50.mcmmopermgen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
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

		TreeMap<String, Map<?, ?>> parentTemp = new TreeMap<String, Map<?, ?>>();
		TreeMap<String, Permission> permissions = new TreeMap<String, Permission>();

		for(Object entrySet : map.entrySet()) {
			Entry<?, ?> entry = (Entry<?, ?>) entrySet;
			if(entry.getKey().equals("permissions")) {
				Map<?, ?> permissionsMap = (Map<?, ?>) entry.getValue();

				NODE: for(Object permissionsEntrySet : permissionsMap.entrySet()) {
					Entry<?, ?> permissionsEntry = (Entry<?, ?>) permissionsEntrySet;

					String node = (String) permissionsEntry.getKey();
					String wiki = "";

					for(Object permissionEntrySet : ((Map<?, ?>) permissionsEntry.getValue()).entrySet()) {
						Entry<?, ?> permissionEntry = (Entry<?, ?>) permissionEntrySet;
						if(permissionEntry.getKey().equals("noparse") && ((Boolean) permissionEntry.getValue() == true)) {
							continue NODE;
						}

						if(permissionEntry.getKey().equals("children")) {
							parentTemp.put(node, (Map<?, ?>) permissionsEntry.getValue());
							continue NODE;
						}

						if(permissionEntry.getKey().equals("wiki")) {
							wiki = (String) permissionEntry.getValue();
						}
					}

					Permission permission = new Permission(node, wiki);
					permissions.put(node, permission);
					System.out.println(permission.toString());
				}
			}
		}

		// Now go through and load parent nodes
		int passes = 0;
		while(!parentTemp.isEmpty() && passes < 5) {
			Iterator<String> iterator = parentTemp.keySet().iterator();
			NODE: while(iterator.hasNext()) {
				String key = iterator.next();
				String wiki = "";
				HashSet<Permission> children = new HashSet<Permission>();

				for(Object permissionEntrySet : parentTemp.get(key).entrySet()) {
					Entry<?, ?> permissionEntry = (Entry<?, ?>) permissionEntrySet;
					if(permissionEntry.getKey().equals("noparse") && ((Boolean) permissionEntry.getValue() == true)) {
						continue;
					}

					if(permissionEntry.getKey().equals("children")) {
						for(Entry<?, ?> childrenEntrySet : ((Map<?, ?>) permissionEntry.getValue()).entrySet()) {
							if(permissions.get(childrenEntrySet.getKey()) ==  null) {
								continue NODE;
							}
							children.add(permissions.get(childrenEntrySet.getKey()));
						}
						continue;
					}

					if(permissionEntry.getKey().equals("wiki")) {
						wiki = (String) permissionEntry.getValue();
					}
				}

				Permission permission = new Permission(key, wiki, children);
				permissions.put(key, permission);
				iterator.remove();
				System.out.println(permission.toString());
			}
			passes++;
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
