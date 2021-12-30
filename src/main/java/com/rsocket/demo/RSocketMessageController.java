package com.rsocket.demo;

import java.time.Duration;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RSocketMessageController {

	/**
	 * ./rsc --debug --request --data "{\"message\":\"Hello\"}" --route
	 * request-response --stacktrace tcp://localhost:7000
	 * 
	 * @param message
	 * @param user
	 * @return
	 */
	@MessageMapping("request-response")
	Mono<Message> requestResponse(final Message message) {
		log.info("Received request-response message: {}", message);
		return Mono.just(new Message("You said: " + message.getMessage()));
	}

	/**
	 * 
	 * ./rsc --debug --fnf --data "{\"message\":\"Hello\"}" --route fire-and-forgot
	 * --stacktrace tcp://localhost:7000
	 * 
	 * @param message
	 * @param user
	 * @return
	 */
	@MessageMapping("fire-and-forget")
	public Mono<Void> fireAndForget(final Message message) {
		log.info("Received fire-and-forget request: {}", message);
		return Mono.empty();
	}

	/**
	 * ./rsc --debug --stream --data "{\"message\":\"Hello\"}" --route
	 * request-stream --stacktrace tcp://localhost:7000
	 * 
	 * 
	 * @param message
	 * @param user
	 * @return
	 */
	@MessageMapping("request-stream")
	Flux<Message> stream(final Message message) {
		log.info("Received stream request: {}", message);
		return Flux.interval(Duration.ofSeconds(1))
				.map(index -> new Message("You said: " + message.getMessage() + ". Response #" + index)).log();
	}

	/**
	 * 
	 * ./rsc --debug --channel --data - --route stream-stream --stacktrace
	 * tcp://localhost:7000
	 * 
	 * @param settings
	 * @param user
	 * @return
	 */
	@MessageMapping("stream-stream")
	Flux<Message> channel(final Flux<Integer> settings) {
		log.info("Received stream-stream (channel) request...");
		return settings.doOnNext(setting -> log.info("Requested interval is {} seconds.", setting))
				.doOnCancel(() -> log.warn("The client cancelled the channel.")).switchMap(setting -> Flux
						.interval(Duration.ofSeconds(setting)).map(index -> new Message("Stream Response #" + index)))
				.log();
	}

}
