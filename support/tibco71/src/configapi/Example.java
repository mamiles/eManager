
import com.tibco.tibrv.config.*;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Iterator;

public class Example {
    /**
     * This example shows how to use the Rendezvous configuration API
     * trying to retrieve some configuration of the Rendezvous routing daemon accessible
     * by the url http://localhost:7580
     */

    /*
     * The classes MyHostnameVerifier and MyX509TrustManager are defined
     * to communicate to daemon that uses self signed certificates.
     * These are just basic examples removing all SSL authentication.
     * One may want to check a specific value on the certificate or
     * a specific hostname.
     */

    class MyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String s, SSLSession session) {
            return true;
        }
    }

    class MyX509TrustManager implements X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] certificates, String s) throws CertificateException {
        }
    }

    private void init() {
        /**
         * This function initialize SSL context. See previous comment.
         */
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        try {
            TrustManager[] trustManager = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null,
                            trustManager,
                            null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }

    public Example() {
        init();
    }

    public void run() {
        try {

            // Create one instance of DaemonManager for each daemon you want to configure

            DaemonManager daemonManager = new DaemonManager("http://localhost:7580");

            // The daemon proxy is the instance that permits the configuration.
            // This proxy implements one or multiple proxies (RvdProxy, RvrdProxy,
            // SecureDaemonProxy, SecurityProxy, RvcacheProxy, RvaProxy).
            // In this case (a rvrd) the proxy implements RvdProxy, RvrdProxy,
            // SecurityProxy.

            // First lets use some common functions

            DaemonProxy daemonProxy = (DaemonProxy) daemonManager.getDaemonProxy();

            String componentName = daemonProxy.getComponentName();
            ComponentInformation componentInformation = daemonProxy.getComponentInformation();

            System.out.println("The name of this component is: "
                               + componentName);

            Map componentInformationMap = componentInformation.getAsMap();

            System.out.println("This component is decribed by the following information...");
            Iterator iterator = componentInformationMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                System.out.println(key
                                   + ": "
                                   + componentInformationMap.get(key));
            }

            // Now lets use some rvd specific functions we must convert the proxy
            // to an RvdProxy

            RvdProxy rvdProxy = (RvdProxy) daemonProxy;

            ClientTransport[] clientTransports = rvdProxy.getClientTransports();
            for (int i = 0; i < clientTransports.length; i++) {
                System.out.println(clientTransports[i].getDescription());
            }

            Service[] services = rvdProxy.getServices();
            for (int i = 0; i < services.length; i++) {
                System.out.println(services[i].getPortNumber());
            }

            // Now lets use some rvrd specific functions we must convert the proxy
            // to an RvrdProxy

            RvrdProxy rvrdProxy = (RvrdProxy) daemonProxy;

            // Lets see the logging configuration

            LoggingParams loggingParams = rvrdProxy.getLoggingParams();

            // Lets see if the connections are logged

            if (loggingParams.connections())
                System.out.println("Connections are logged.");

            // Lets see if there is routers configured

            Router[] routers = rvrdProxy.getRouters();

            if (routers != null
                    && routers.length != 0) {
                System.out.println("routers defined:");
                for (int i = 0; i < routers.length; i++) {
                    System.out.println(routers[i].getName());
                }

                // Lets look if the first one defines local networks
                Router firstRouter = routers[0];

                LocalNetworkInterface[] localNetworkInterfaces = firstRouter.getLocalNetworkInterfaces();
                if (localNetworkInterfaces != null
                        && localNetworkInterfaces.length != 0) {

                    System.out.println("Local network interfaces defined for router "
                                       + firstRouter.getName()
                                       + ": ");

                    for (int i = 0; i < localNetworkInterfaces.length; i++) {
                        LocalNetworkInterface anInterface = localNetworkInterfaces[i];
                        System.out.println(anInterface.getName());
                        // Lets print imported subjects for each interface
                        ImportSubject[] importedSubjects = anInterface.getImportSubjects();
                        if (importedSubjects != null
                                && importedSubjects.length != 0) {
                            System.out.println("Imported subjects:");
                            for (int j = 0; j < importedSubjects.length; j++) {
                                ImportSubject subject = importedSubjects[j];
                                System.out.println(subject.getSubject()
                                                   + "/"
                                                   + subject.getWeight());
                            }
                        }
                    }
                }
            }
        } catch (ConfigurationException exception) {
            System.err.println(exception.toString());
        }
    }

    public static void main(String[] args) {
        new Example().run();
    }
}
