package se.vgregion.portal.csiframe.svc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class HttpMailInfServiceTest {

    @BeforeClass
    public static void initMockServerNameService() throws Exception {
        initMockServerNameService(true);
    }

    public static void initMockServerNameService(boolean stopAtShutdown) throws Exception {
        Server server = new Server(8081);
        server.setStopAtShutdown(stopAtShutdown);

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setResourceBase("war");
        wac.setClassLoader(HttpMailInfServiceTest.class.getClassLoader());
        wac.addServlet(MockService.class.getName(), "/msrv");
        server.addHandler(wac);
        server.start();
    }

    @Test
    public void testFindServerName() {
        String serviceUrl = "http://localhost:8081/msrv?";
        HttpMailInfService controller = new HttpMailInfService();
        controller.setServiceUrl(serviceUrl);
        String result = controller.findServerName("aUserId");
        assertEquals("liv", result);
    }

    @SuppressWarnings("serial")
    public static class MockService extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                IOException {
            PrintWriter writer = resp.getWriter();
            writer.write("<html><head></head><body text=\"#000000\">liv.vgregion.se;mail/aUserId.nsf;W</body></html>");
            writer.flush();
            writer.close();
        }
    }

    /**
     * Use this to run a stand alone impl-service for your dev-environment if the real service is out of order.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        initMockServerNameService(false);
    }
}
