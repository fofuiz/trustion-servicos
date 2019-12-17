package br.com.accesstage.trustion.util;

import org.springframework.stereotype.Service;

import com.accesstage.hikerfnxregeventclient.module.Status;
import com.accesstage.hikerfnxregeventclient.module.Track;
import com.accesstage.hikerfnxregeventclient.resource.TrackResource;
import com.accesstage.hikerfnxregeventclient.resource.TrackResourceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegistroEventoService {

    public void call(boolean status, String message, Long trackingId) {
        Track track = null;
        TrackResource resource = new TrackResourceImpl();
        try {
            // Limitação do Fornax.
            message = message.length() > 255 ? message.substring(0, 255) : message;
            if (status) {
                track = Track.builder().id(trackingId).status(Status.I).author("hiker-bulk-file-doc").message(message)
                        .build();
                resource.forceStatus(track);
            } else {
                track = Track.builder().id(trackingId).status(Status.E).author("hiker-bulk-file-doc").message(message)
                        .build();
            }
            resource.add(track);
        } catch (Exception e) {
            log.error("Erro ao registrar o evento no fornax trackingId: " +  trackingId + " ERRO >>" + e.getMessage(), e);
        }

    }
}
