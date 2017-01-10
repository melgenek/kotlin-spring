package sample;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class PersonHandler {

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		return Mono.empty();
	}

	public static Mono<ServerResponse> getOtherPerson(ServerRequest request) {
		return Mono.empty();
	}

}
