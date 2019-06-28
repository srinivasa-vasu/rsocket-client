package io.humourmind.rsocketclient.config;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;

@Configuration
public class RSocketClientConfig {

	private final ClientConfigProp clientConfigProp;

	public RSocketClientConfig(ClientConfigProp clientConfigProp) {
		this.clientConfigProp = clientConfigProp;
	}

	@Bean
	RSocketRequester requester(RSocketStrategies strategies) throws URISyntaxException {
		return RSocketRequester.builder().rsocketStrategies(strategies)
				.rsocketFactory(factory -> {
					factory.dataMimeType(MimeTypeUtils.ALL_VALUE)
							.frameDecoder(PayloadDecoder.ZERO_COPY);
				})
				.connect(TcpClientTransport.create(new InetSocketAddress(
						clientConfigProp.getHost(), clientConfigProp.getPort())))
				.retry().block();
	}

}
