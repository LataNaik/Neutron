package central;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestContext;

import enums.ConfigType;
import pagedef.PageDef;
import utils.IniReader;

public enum AppachhiCentral {
	INSTANCE;

	private static Logger logger = null;
	private Configuration centralConf = null;
	private Map<String, Configuration> contextConfigs = new HashMap<String, Configuration>();
	private Map<String, IniReader> contextTestData = new HashMap<String, IniReader>();
	private ObjectRepository pageDefs = null;
	private String screenDateFormat = null;

	public void init() throws Exception {
		String log4jConfigFile = getPath("conf/log4j.properties");
		PropertyConfigurator.configure(log4jConfigFile);
		logger = Logger.getRootLogger();
		loadCentralConf();
		loadObjectRepository();
	}

	private String getPath(String relPath) throws Exception {
		return (new File(AppachhiCentral.class.getClassLoader().getResource(relPath).toURI())).getAbsolutePath();
	}

	public String getDirPathForConf(ConfigType confConst) throws Exception {
		return getPath(centralConf.getValue(confConst));
	}

	public String getScreenShotsDir() {
//		return System.getProperty("user.dir") + File.separator + centralConf.getValue(ConfigType.SCREENSHOTS_DIR);
//		return System.getProperty("screenshot_path");
		return getProperty("screenshot_path", centralConf);
	}

	private void loadCentralConf() throws Exception {
		if (centralConf != null)
			return;
		String confPath = getPath("conf/config.ini");
		IniReader reader = new IniReader(confPath);
		this.centralConf = new Configuration(reader.getSectionData("Central"));
		this.screenDateFormat = centralConf.getValue(ConfigType.SCRREENSHOT_TSTAMP);
	}

	public String getScreenShotTimeStampFormat() {
		return this.screenDateFormat;
	}

	private void loadObjectRepository() throws Exception {
		pageDefs = new ObjectRepository(getDirPathForConf(ConfigType.PAGEDEF_DIR));
	}

	public synchronized void
	registerContext(ITestContext context) throws Exception {
		Configuration contextConf = new Configuration(centralConf, context);
		this.contextConfigs.put(context.getName().toLowerCase(), contextConf);
		String testDataFilePath = String.format("%s/%s/testdata.ini", getDirPathForConf(ConfigType.DATA_DIR),
				getProperty("platform", contextConf));
		logger.debug(String.format("Loading Test Data File at Path :: %s", testDataFilePath));
		IniReader reader = new IniReader(testDataFilePath);
		this.contextTestData.put(context.getName().toLowerCase(), reader);
	}

	public synchronized Configuration getContextConfig(ITestContext context) {
		return this.contextConfigs.get(context.getName().toLowerCase());
	}

	public synchronized Map<String, String> getTestData(ITestContext context, String section) {
		return this.contextTestData.get(context.getName().toLowerCase()).getSectionData(section);
	}

	public static Logger getLogger() {
		return logger;
	}

	public synchronized PageDef getPageDef(String pageName) {
		return pageDefs.getPageDef(pageName);
	}
	
	public String getProperty(String arg, Configuration config) {
        String ret_val="";
               
        if ( arg.equalsIgnoreCase("screenshot_path") ) {
            if ( System.getProperty("screenshot_path") != null )
                ret_val=System.getProperty("screenshot_path");
            else
                ret_val=System.getProperty("user.dir") + File.separator + centralConf.getValue(ConfigType.SCREENSHOTS_DIR);
        } 
        
        if ( arg.equalsIgnoreCase("platform") ) {
            if ( System.getProperty("platform") != null )
                ret_val=System.getProperty("platform");
            else
                ret_val=config.getValue(ConfigType.PLATFORM);
        } 
        return ret_val;
    }
}


