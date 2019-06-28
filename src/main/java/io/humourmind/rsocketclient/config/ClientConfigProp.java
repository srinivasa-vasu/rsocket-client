package io.humourmind.rsocketclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "client-prop")
public class ClientConfigProp {

	private String host;
	private int port;

}
