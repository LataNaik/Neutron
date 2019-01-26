package testcore.pages;

import agent.IAgent;
import central.Configuration;
import page.Page;

import java.util.Map;

public abstract class FullPage extends Page {

    public FullPage(Configuration config, IAgent agent, Map<String, String> testData) throws Exception {
        super(config, agent, testData);
    }
}
