package agent;

import org.apache.log4j.Logger;

import agent.internal.MobileAgentFactory;
import agent.internal.WebAgentFactory;
import central.Configuration;
import central.AppachhiCentral;
import enums.Platform;

public class AgentFactory {
	private static Logger logger = AppachhiCentral.getLogger();

	public static IAgent createAgent(Configuration config) throws Exception {
		String msg = String.format("Creating agent for: %s", config.getPlatform());
		logger.debug(msg);
		try {
			IAgent agent = null;
			if (config.getPlatform() == Platform.DESKTOP_WEB) {
				agent = WebAgentFactory.createAgent(config);
			}
			else {
				agent = MobileAgentFactory.createAgent(config);
			}
			logger.debug(String.format("Success in %s", msg));
			return agent;
		} catch (Exception e) {
			logger.debug(String.format("Failure in %s", msg));
			throw e;
		}
	}
}
