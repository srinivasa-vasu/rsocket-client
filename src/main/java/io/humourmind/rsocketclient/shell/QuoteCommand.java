package io.humourmind.rsocketclient.shell;

import java.util.concurrent.TimeUnit;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.bind.annotation.PathVariable;

import io.humourmind.rsocketclient.domain.Quote;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@ShellComponent
public class QuoteCommand {

	private final Mono<RSocketRequester> requester;

	public QuoteCommand(Mono<RSocketRequester> requester) {
		this.requester = requester;
	}

	@ShellMethod(key = "reqquotestream", value = "key-in a symbol to get the response streamed continuously")
	public void reqStream(@PathVariable String symbol) {
		requester
				.flatMapMany(req -> req.route("a-quote-stream").data(Mono.just(symbol))
						.retrieveFlux(Quote.class))
				.subscribe(ele -> log.info(ele.toString()));
	}

	@ShellMethod(key = "reqquote", value = "key-in a symbol to get the response")
	public void reqQuote(@PathVariable String symbol) throws InterruptedException {
		requester
				.flatMap(req -> req.route("a-quote").data(Mono.just(symbol))
						.retrieveMono(Quote.class))
				.subscribe(ele -> log.info(ele.toString()));
		TimeUnit.SECONDS.sleep(2);
	}
}
