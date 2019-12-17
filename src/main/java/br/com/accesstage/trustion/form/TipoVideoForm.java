package br.com.accesstage.trustion.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.accesstage.trustion.model.enums.TipoVideoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TipoVideoForm {

	@NotNull
	@NotEmpty
	private TipoVideoEnum tipoVideo;

	@NotNull
	@NotEmpty
	private String link;

}
