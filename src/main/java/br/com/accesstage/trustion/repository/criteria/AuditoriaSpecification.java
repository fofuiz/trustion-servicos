package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.model.Auditoria;
import static br.com.accesstage.trustion.util.SpecificationUtils.equal;
import static br.com.accesstage.trustion.util.SpecificationUtils.greaterThanOrEqualTo;
import static br.com.accesstage.trustion.util.SpecificationUtils.like;
import static br.com.accesstage.trustion.util.SpecificationUtils.in;
import static br.com.accesstage.trustion.util.SpecificationUtils.lessThanOrEqualTo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AuditoriaSpecification {

    public static Specification<Auditoria> byCriterio(AuditoriaDTO dto, List<Long> empresas) {
        return (Root<Auditoria> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            equal(builder, root, predicates, "idUsuario", dto.getIdUsuario());
            equal(builder, root, predicates, "idGrupoEconomico", dto.getIdGrupoEconomico());
            
            like(builder, root, predicates, "grupoEconomico", dto.getGrupoEconomico());
            like(builder, root, predicates, "empresa", dto.getEmpresa());
            like(builder, root, predicates, "usuario", dto.getUsuario());
            like(builder, root, predicates, "nroOcorrencia", dto.getNroOcorrencia());
            
            in(builder, root, predicates, "idEmpresa", empresas);

            greaterThanOrEqualTo(builder, root, predicates, "dataAcao", dto.getDataInicial());
            lessThanOrEqualTo(builder, root, predicates, "dataAcao", dto.getDataFinal());

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
