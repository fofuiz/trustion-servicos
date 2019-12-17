package br.com.accesstage.trustion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "email")
public class EmailProperties {
	
	private static String link;
	private static String banner;
	
	public static String getLink() {
		return link;
	}
	public static void setLink(String link) {
		EmailProperties.link = link;
	}
	public static String getBanner() {
		return banner;
	}
	public static void setBanner(String banner) {
		EmailProperties.banner = banner;
	}
}
