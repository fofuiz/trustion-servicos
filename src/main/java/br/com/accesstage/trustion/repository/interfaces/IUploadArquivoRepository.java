package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.UploadArquivo;

@Repository
public interface IUploadArquivoRepository extends JpaRepository<UploadArquivo, Long>, JpaSpecificationExecutor<UploadArquivo> {

	UploadArquivo findOneByIdAtividade(Long idAtividade);
}
