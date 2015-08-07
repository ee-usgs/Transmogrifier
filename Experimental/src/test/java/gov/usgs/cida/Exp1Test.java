package gov.usgs.cida;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;

import junit.framework.TestCase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.util.URISupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
@RunWith(value = Parameterized.class)
public class Exp1Test {
	private static final Logger LOG = LoggerFactory.getLogger(Exp1Test.class);

    Properties secProps;

    private String configUrl;

    @Parameterized.Parameters
    public static Collection<String[]> getTestParameters() {
        List<String[]> configUrls = new ArrayList<String[]>();
        configUrls.add(new String[]{"xbean:src/main/resources/activemq.xml"});

//        File sampleConfDir = new File("target/conf");
//        for (File xmlFile : sampleConfDir.listFiles(new FileFilter() {
//            public boolean accept(File pathname) {
//                return pathname.isFile() &&
//                        pathname.getName().startsWith("activemq-") &&
//                        pathname.getName().endsWith("xml");
//            }})) {
//
//            configUrls.add(new String[]{"xbean:" + sampleConfDir.getAbsolutePath() + "/" + xmlFile.getName()});
//        }

        return configUrls;
    }


    public Exp1Test(String config) {
        this.configUrl = config;
    }

    @Test
    public void testStartBrokerUsingXmlConfig1() throws Exception {
        BrokerService broker = null;
        LOG.info("Broker config: " + configUrl);
        System.err.println("Broker config: " + configUrl);
        broker = BrokerFactory.createBroker(configUrl);
        // alive, now try connect to connect
        try {
            for (TransportConnector transport : broker.getTransportConnectors()) {
                final URI UriToConnectTo = URISupport.removeQuery(transport.getConnectUri());

                if (UriToConnectTo.getScheme().startsWith("stomp")) {
                    LOG.info("validating alive with connection to: " + UriToConnectTo);
                    StompConnection connection = new StompConnection();
                    connection.open(UriToConnectTo.getHost(), UriToConnectTo.getPort());
                    connection.close();
                    break;
                } else if (UriToConnectTo.getScheme().startsWith("tcp")) {
                    LOG.info("validating alive with connection to: " + UriToConnectTo);
                    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(UriToConnectTo);
                    Connection connection = connectionFactory.createConnection(secProps.getProperty("activemq.username"),
                            secProps.getProperty("activemq.password"));
                    connection.start();
                    connection.close();
                    break;
                } else {
                    LOG.info("not validating connection to: " + UriToConnectTo);
                }
            }
        } finally {
            if (broker != null) {
                broker.stop();
                broker.waitUntilStopped();
                broker = null;
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("activemq.base", "target");
        System.setProperty("activemq.home", "target"); // not a valid home but ok for xml validation
        System.setProperty("activemq.data", "target");
        System.setProperty("activemq.conf", "target/conf");
        secProps = new Properties();
        secProps.load(new FileInputStream(new File("src/main/resources/credentials.properties")));
    }

    @After
    public void tearDown() throws Exception {
        TimeUnit.SECONDS.sleep(1);
    }
}
