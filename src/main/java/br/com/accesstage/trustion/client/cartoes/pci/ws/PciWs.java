package br.com.accesstage.trustion.client.cartoes.pci.ws;

import java.net.URL;

import javax.xml.namespace.QName;

import org.jboss.logging.Logger;

public class PciWs {

    public static String PATH_PCI_WS = "br.com.accesstage.pci.ws.adress";
    private static PciWs instance;
    private KeysManagerWS decrypt;

    private static final Logger LOGGER = Logger.getLogger(PciWs.class);

    public PciWs() {
        String url_pci = System.getProperty(PATH_PCI_WS);
        // String url_pci = "192.168.42.185:11110";
        if (url_pci != null) {

            QName qName = new QName("http://ws.pci.acesstage.com.br", "KeysManagerWSService");

            StringBuilder novaUrl = new StringBuilder("http://");
            novaUrl.append(url_pci);

            novaUrl.append("/ASPCIWS/services/KeysManagerWS?WSDL");

            LOGGER.info("URL do ASPCI: " + novaUrl);

            URL url = null;
            try {
                url = new URL(novaUrl.toString());

                KeysManagerWSService service = new KeysManagerWSService(url, qName);
                decrypt = service.getKeysManagerWS();
            } catch (Exception e) {
                LOGGER.error("O parametro de URL do ASPCI esta invalido: " + url);
            }

        } else {
            LOGGER.error("O parametro de URL do ASPCI e invalido!");
        }
    }

    public static PciWs getIntance() {
        if (instance == null) {
            instance = new PciWs();
        }
        return instance;
    }

    public String obterValor(String valor) {
        String result = decrypt.decrypt(valor);

        return result;
    }
}
