package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.model.Pac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface INumerarioBananinhaService {

    public Page<Pac> findAllByGtv(int gtv, Pageable paginacao);

    public List<Pac> findAllByGtv(int gtv);

}
