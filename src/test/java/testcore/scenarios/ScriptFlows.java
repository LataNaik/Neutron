package testcore.scenarios;


import org.testng.annotations.Test;

public class ScriptFlows extends SupportTest {

    @Test
    public void fileComplaintCitizen() throws Exception{
        logger.debug(this.getTestStartInfoMessage("fileComplaintCitizen"));
//        home.loginCitizen()
//                .navigateToComplaints()
//                .createComplaint()
//                .isComplaintRegistered();
    }


    @Test
    public void approveTradeLicenseApprover() throws Exception {
        logger.debug(this.getTestStartInfoMessage("testScript"));
//        home.loginEmployee()
//                .navigateToTradeLicense()
//                .approveTradeLicense()
//                .isApplicationApproved();
    }
}