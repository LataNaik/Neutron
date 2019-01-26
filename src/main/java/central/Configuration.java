package central;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;

import enums.ConfigType;
import enums.Platform;

public class Configuration {

	private Map<ConfigType, String> options = new HashMap<ConfigType, String>();

	private Configuration(Configuration baseConf, Map<String, String> overrideOptions) {
		if (baseConf != null)
			options.putAll(baseConf.getAsMap());
		for (String k : overrideOptions.keySet()) {
			options.put(ConfigType.valueOf(k.toUpperCase()), overrideOptions.get(k));
		}
	}

	public Configuration(Configuration baseConf, ITestContext context) {
		this(baseConf, context.getCurrentXmlTest().getAllParameters());
	}

	public Configuration(ITestContext context) {
		this(null, context);
	}

	public Configuration(Map<String, String> overrideOptions) {
		this(null, overrideOptions);
	}

	public Map<ConfigType, String> getAsMap() {
		return this.options;
	}

	public String getValue(ConfigType name) {
		return options.get(name);
	}

	public Platform getPlatform() {
//  	return Platform.valueOf(this.getValue(ConfigType.PLATFORM).toUpperCase());	
//		return Platform.valueOf(System.getProperty("platform").toUpperCase());
		return Platform.valueOf(getProperty("platform", this).toUpperCase());
	}

	public String getAppFilePath() throws Exception {
//		return String.format("%s%s%s",  File.separator,System.getProperty("user.dir"),this.getValue(ConfigType.APP_FILE_NAME));
		return String.format(getProperty("app_file_path", this));
	}
	
	public String getProperty(String arg, Configuration config) {
        String ret_val="";
 
        if ( arg.equalsIgnoreCase("app_file_path") ) {          
            if ( System.getProperty("app_file_path") != null )
                ret_val=System.getProperty("app_file_path");
            else
                ret_val=String.format("%s%s%s",  File.separator,System.getProperty("user.dir"),this.getValue(ConfigType.APP_FILE_NAME));
        } 
        
        if ( arg.equalsIgnoreCase("platform") ) {
            if ( System.getProperty("platform") != null )
                ret_val=System.getProperty("platform");
            else
                ret_val=config.getValue(ConfigType.PLATFORM);
        }

        if(arg.equalsIgnoreCase("app_browser_url")){
        	if(System.getProperty("app_browser_url")!=null)
				ret_val=System.getProperty("app_browser_url");
			else
				ret_val=getValue(ConfigType.BASE_URL);
		}
		if ( arg.equalsIgnoreCase("browser_resolution_height") ) {
			if ( System.getProperty("browser_resolution_height") != null )
				ret_val=System.getProperty("browser_resolution_height");
			else
				ret_val=String.format("%s%s%s",  File.separator,System.getProperty("user.dir"),this.getValue(ConfigType.BROWSER_RESOLUTION_HEIGHT));
		}
		if(arg.equalsIgnoreCase("browser_resolution_width")){
			if (System.getProperty("browser_resolution_width")!=null)
				ret_val=System.getProperty("browser_resolution_width");
			else
				ret_val=String.format("%s%s%s",  File.separator,System.getProperty("user.dir"),this.getValue(ConfigType.BROWSER_RESOLUTION_WIDTH));
		}
        return ret_val;
    }
}
