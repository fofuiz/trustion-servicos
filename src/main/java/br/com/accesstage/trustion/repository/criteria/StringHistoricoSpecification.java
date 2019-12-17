package br.com.accesstage.trustion.repository.criteria;

import br.com.accesstage.trustion.dto.StringHistoricoDTO;
import br.com.accesstage.trustion.model.StringHistorico;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class StringHistoricoSpecification {

    public static Specification<StringHistorico> byCriterio(StringHistoricoDTO dto) {

        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto != null) {

                if (dto.getIdListaBanco() != null && dto.getIdListaBanco() > 0L) {
                    predicates.add(builder.equal(root.get("idListaBanco"), dto.getIdListaBanco()));
                }

                if (isNotBlank(dto.getTexto())) {
                    predicates.add(builder.like(builder.lower(root.get("texto")), "%" + dto.getTexto().toLowerCase() + "%"));
                }

                if (isNotBlank(dto.getStatus())) {
                    predicates.add(builder.equal(root.get("status"), dto.getStatus()));
                }
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
