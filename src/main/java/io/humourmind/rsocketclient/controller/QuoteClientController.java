package io.humourmind.rsocketclient.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.humourmind.rsocketclient.domain.Quote;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class QuoteClientController {

	private final RSocketRequester requester;
	private Publisher<Quote> allQuoteStream;

	public QuoteClientController(RSocketRequester requester) {
		this.requester = requester;
		this.allQuoteStream = this.requester.route("all-quote-stream").data(Mono.empty())
				.retrieveFlux(Quote.class).share().cache();
	}

	@GetMapping(value = "/v1/stream/quotes", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Publisher<Quote> getAllQuotesStream() {
		return this.allQuoteStream;
	}

	@GetMapping(value = "/v1/stream/quotes/faang", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Publisher<Quote> getFilteredQuotesStream() {
		return requester.route("filtered-quote-stream")
				.data(Flux.just("FB", "AMZN", "AAPL", "NFLX", "GOOGL"))
				.retrieveFlux(Quote.class);

		// return requester.route("filtered-quote-stream")
		// .data(Flux.interval(Duration.ofSeconds(1))
		// .compose(e -> Flux.just("FB", "AMZN", "AAPL", "NFLX", "GOOGL")))
		// .retrieveFlux(Quote.class);
	}

	@GetMapping(value = "/v1/stream/quotes/{symbol}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Publisher<Quote> getAQuoteStream(@PathVariable String symbol) {
		return requester.route("a-quote-stream").data(Mono.just(symbol))
				.retrieveFlux(Quote.class);
	}

	@GetMapping(value = "/v1/quote/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher<Quote> getAQuote(@PathVariable String symbol) {
		return requester.route("a-quote").data(Mono.just(symbol))
				.retrieveMono(Quote.class);
	}

}
