package io.humourmind.rsocketclient.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.StringJoiner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

	private String ticker = "404";
	private BigDecimal price;
	private Instant instant = Instant.now();
	private long counter;

	@Override
	public String toString() {
		return new StringJoiner(", ", Quote.class.getSimpleName() + "[", "]")
				.add("ticker='" + ticker + "'").add("price=" + price)
				.add("instant=" + instant).add("counter=" + counter).toString();
	}
}
