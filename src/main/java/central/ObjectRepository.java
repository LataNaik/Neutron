package central;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pagedef.PageDef;
import utils.JavaUtils;

public class ObjectRepository {
	private Map<String, PageDef> rep = new HashMap<String, PageDef>();

	public ObjectRepository(String repPath) throws Exception {
		for (File f : JavaUtils.listFiles(repPath)) {
			this.rep.put(f.getName().split("\\.")[0].toLowerCase(), new PageDef(f.getAbsolutePath()));
		}
	}

	public PageDef getPageDef(String pageName) {
		return this.rep.get(pageName.toLowerCase());
	}

}
