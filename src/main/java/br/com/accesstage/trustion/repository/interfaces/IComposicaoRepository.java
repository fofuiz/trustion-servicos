package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.Composicao;

@Repository
public interface IComposicaoRepository extends JpaRepository<Composicao, Long> {

    Page<Composicao> findByNumeroGVT(String numeroGVT, Pageable pageable);
}