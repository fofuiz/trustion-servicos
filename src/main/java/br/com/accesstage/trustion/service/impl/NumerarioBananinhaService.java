package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.model.Pac;
import br.com.accesstage.trustion.repository.interfaces.INumerarioBananinhaRepository;
import br.com.accesstage.trustion.service.interfaces.INumerarioBananinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NumerarioBananinhaService implements INumerarioBananinhaService {

    @Autowired
    private INumerarioBananinhaRepository numerarioBananinhaRepository;

    @Override
    public Page<Pac> findAllByGtv(int gtv, Pageable paginacao) {
        return this.numerarioBananinhaRepository.findByGtv(gtv, paginacao);
    }

    @Override
    public List<Pac> findAllByGtv(int gtv) {
        return this.numerarioBananinhaRepository.findByGtv(gtv);
    }

}
