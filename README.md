This is a reproducer for the vertx issue https://github.com/eclipse-vertx/vertx-http-proxy/issues/42

FYI: The reverse proxy forwards requests to `api.datamuse.com/words`.

To reproduce
* run `./mvnw compile exec:java` to start the vertx server
* run e.g., `http :8080/words sl==jirrafe`

The line  `rc.vertx().setTimer(1, tid -> rc.next())`in `MainVerticle.java` is supposed to mock an async call within a context handler. If the line is replaced with `rc.next()` only, the reverse proxy works fine.