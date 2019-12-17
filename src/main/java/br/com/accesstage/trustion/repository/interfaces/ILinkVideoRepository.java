package br.com.accesstage.trustion.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.accesstage.trustion.model.LinkVideo;

public interface ILinkVideoRepository extends JpaRepository<LinkVideo, Long> {

	Optional<LinkVideo> findByGtv(Long gtv);

}
