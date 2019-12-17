package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Pac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INumerarioBananinhaRepository extends JpaRepository<Pac, Integer> {

    public Page<Pac> findByGtv(int gtv, Pageable paginacao);

    public List<Pac> findByGtv(int gtv);

    @Query("SELECT SUM(p.vlrDeclarado) FROM PacEntity p WHERE p.gtv = :gtv")
    public double findTotalVlrDeclaradoPorGtv(@Param("gtv") int gtv);

    @Query("SELECT SUM(p.vlrConferido) FROM PacEntity p WHERE p.gtv = :gtv")
    public double findTotalVlrConferidoPorGtv(@Param("gtv") int gtv);

}
