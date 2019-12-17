package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.VideoGTV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVideoGTVRepository extends JpaRepository<VideoGTV, String> {

}
