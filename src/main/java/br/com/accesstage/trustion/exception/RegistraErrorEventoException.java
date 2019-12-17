package br.com.accesstage.trustion.exception;

import br.com.accesstage.trustion.util.RegistroEventoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegistraErrorEventoException extends Exception {



    /**
     *
     */
    private static final long serialVersionUID = 4145132767493343066L;

    public RegistraErrorEventoException(String message) {
        super(message);
    }

    public RegistraErrorEventoException(String message, Long trackingId) {
        RegistroEventoService registroEventoService = new RegistroEventoService();
        log.error("<<trackingId>> : " +  trackingId + " <<Message:>>" +  message);
        registroEventoService.call(false, message, trackingId);
    }

}
