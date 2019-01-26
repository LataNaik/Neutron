package testcore.scenarios;

import agent.AgentFactory;
import agent.IAgent;
import central.AppachhiCentral;
import central.Configuration;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import testcore.pages.HomePage;

import java.lang.reflect.Method;
import java.util.Map;

public class SupportTest {
    protected static Logger logger = AppachhiCentral.getLogger();
    private Configuration conf = null;
    protected HomePage home;
    protected TradeLicense tradeLicense;
    protected ComplaintsPage complaints;
    protected PaymentPage payment;
    private IAgent agent;
    private ITestContext context = null;
    public String testName = null;

    @BeforeSuite(alwaysRun = true)
    public void runOncePerSuite() throws Exception {
        AppachhiCentral.INSTANCE.init();
        logger = AppachhiCentral.getLogger();
        logger.info("Central setup completed.");
    }

    @BeforeTest(alwaysRun = true)
    public void runOncePerContext(ITestContext context) throws Exception {
        //logger.info(String.format("Test context setup started for %s test.", context.getName()));
        AppachhiCentral.INSTANCE.registerContext(context);
        logger.info(String.format("Test context setup completed for %s test.", context.getName()));
    }

    @BeforeClass(alwaysRun = true)
    public void runOncePerClass(ITestContext context) throws Exception {
        this.context = context;
        this.conf = AppachhiCentral.INSTANCE.getContextConfig(context);
    }

    @BeforeMethod(alwaysRun = true)
    public void runOncePerMethod(ITestContext context, Method method) throws Exception {
        testName = method.getName();
        logger.info(String.format("Set up for test method [%s] started.", testName));
        logger.debug(String.format("Creating agent for %s", this.conf.getPlatform()));
        agent = AgentFactory.createAgent(this.conf);
        logger.debug(String.format("Test Method Name Started :: %s", testName));
        Map<String, String> testData = AppachhiCentral.INSTANCE.getTestData(context, testName);
        home = new HomePage(this.conf, agent, testData);
        tradeLicense = new TradeLicense(this.conf, agent, testData);
        complaints = new ComplaintsPage(this.conf,agent,testData);
        payment= new PaymentPage(this.conf,agent,testData);
        logger.info(String.format("Set up for test method [%s] ended.", testName));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws Exception {
        logger.info(String.format("Tear down for test method [%s] started.", testName));
        if (ITestResult.FAILURE == result.getStatus()) {
            agent.takeSnapShot();
        }
        agent.quit();
        logger.info(String.format("Tear down for test method [%s] ended.", testName));
    }

    protected String getTestInfoMessage(String stage, String method) {
        return String.format("Test method [%s] %s", method, stage);
    }

    protected String getTestStartInfoMessage(String method) {
        return getTestInfoMessage("start", method);
    }

    protected String getTestEndInfoMessage(String method) {
        return getTestInfoMessage("end", method);
    }

}
