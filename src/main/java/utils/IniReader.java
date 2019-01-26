package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class IniReader {
	private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

	public IniReader(String path) throws Exception {
		Ini iniFile = new Ini(new File(path));
		for (String name : iniFile.keySet()) {
			Section section = iniFile.get(name);
			Map<String, String> sectionData = new HashMap<String, String>();
			for (String key : section.keySet()) {
				sectionData.put(key, section.get(key));
			}
			data.put(name.toLowerCase(), sectionData);
		}
	}

	public Set<String> getSections() {
		return this.data.keySet();
	}

	public Map<String, String> getSectionData(String section) {
		return this.data.get(section.toLowerCase());
	}
}
