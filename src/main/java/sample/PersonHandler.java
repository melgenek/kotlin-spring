package sample;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

public class PersonHandler {

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		return ServerResponse.ok().body(fromPublisher(Mono.just("getPerson"), String.class));
	}

	public static Mono<ServerResponse> getOtherPerson(ServerRequest request) {
		return ServerResponse.ok().body(fromPublisher(Mono.just("getOtherPerson"), String.class));
	}

}
