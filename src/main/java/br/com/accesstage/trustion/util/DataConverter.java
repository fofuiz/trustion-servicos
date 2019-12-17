package br.com.accesstage.trustion.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface DataConverter {
	
	static Date paraData(LocalDateTime local) {
		return local == null ? null : Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	static LocalDateTime paraLocalDateTime(Date data) {
		return data == null ? null : LocalDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());
	}

	static String paraString(LocalDateTime local, String formato){
		return local.format(DateTimeFormatter.ofPattern(formato));
	}

	static String paraString(LocalDate local, String formato){
		return local.format(DateTimeFormatter.ofPattern(formato));
	}

}
