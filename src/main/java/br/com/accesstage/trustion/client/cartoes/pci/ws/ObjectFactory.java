package br.com.accesstage.trustion.client.cartoes.pci.ws;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the br.com.acesstage.pci.ws package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: br.com.acesstage.pci.ws
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Decrypt }
     */
    public Decrypt createDecrypt() {
        return new Decrypt();
    }

    /**
     * Create an instance of {@link DecryptResponse }
     */
    public DecryptResponse createDecryptResponse() {
        return new DecryptResponse();
    }

    /**
     * Create an instance of {@link EncryptResponse }
     */
    public EncryptResponse createEncryptResponse() {
        return new EncryptResponse();
    }

    /**
     * Create an instance of {@link Encrypt }
     */
    public Encrypt createEncrypt() {
        return new Encrypt();
    }

}
