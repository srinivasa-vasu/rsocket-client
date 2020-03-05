package io.humourmind.rsocketclient.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import reactor.core.publisher.Mono;

@Configuration
public class RSocketClientConfig {

	private ClientConfigProp clientConfigProp;

	RSocketClientConfig(ClientConfigProp clientConfigProp) {
		this.clientConfigProp = clientConfigProp;
	}

	@Bean
	Mono<RSocketRequester> requester(RSocketStrategies strategies)
			throws URISyntaxException {
		return RSocketRequester.builder().rsocketStrategies(strategies)
				.rsocketFactory(factory -> {
					factory.dataMimeType(MimeTypeUtils.ALL_VALUE)
							.frameDecoder(PayloadDecoder.ZERO_COPY).resume();
				})
				.connect("tcp".equalsIgnoreCase(clientConfigProp.getProtocol())
						? TcpClientTransport.create(new InetSocketAddress(
								clientConfigProp.getHost(), clientConfigProp.getPort()))
						: WebsocketClientTransport
								.create(new URI(String.format("ws://%s:%d/rsocket",
										clientConfigProp.getHost(),
										clientConfigProp.getPort()))))
				.retry().log();
	}

}
