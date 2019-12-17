package br.com.accesstage.trustion.client.cartoes.pci.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.jboss.logging.Logger;

/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.4
 * Generated source version: 2.1
 */
@WebServiceClient(name = "KeysManagerWSService", targetNamespace = "http://ws.pci.acesstage.com.br", wsdlLocation = "http://192.168.41.24:11110/ASPCIWS/services/KeysManagerWS?WSDL")
public class KeysManagerWSService extends Service {

    private final static URL KEYSMANAGERWSSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(KeysManagerWSService.class);

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = KeysManagerWSService.class.getResource(".");
            url = new URL(baseUrl, "http://192.168.41.24:11110/ASPCIWS/services/KeysManagerWS?WSDL");
        } catch (MalformedURLException e) {
            logger.error("Failed to create URL for the wsdl Location: 'http://192.168.41.24:11110/ASPCIWS/services/KeysManagerWS?WSDL', retrying as a local file");
            logger.error(e.getMessage());
        }
        KEYSMANAGERWSSERVICE_WSDL_LOCATION = url;
    }

    public KeysManagerWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public KeysManagerWSService() {
        super(KEYSMANAGERWSSERVICE_WSDL_LOCATION, new QName("http://ws.pci.acesstage.com.br", "KeysManagerWSService"));
    }

    /**
     * @return
     *         returns KeysManagerWS
     */
    @WebEndpoint(name = "KeysManagerWS")
    public KeysManagerWS getKeysManagerWS() {
        return super.getPort(new QName("http://ws.pci.acesstage.com.br", "KeysManagerWS"), KeysManagerWS.class);
    }

    /**
     * @param features
     *            A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
     *            Supported features not in the <code>features</code> parameter will have their
     *            default values.
     * @return
     *         returns KeysManagerWS
     */
    @WebEndpoint(name = "KeysManagerWS")
    public KeysManagerWS getKeysManagerWS(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.pci.acesstage.com.br", "KeysManagerWS"), KeysManagerWS.class, features);
    }

}
