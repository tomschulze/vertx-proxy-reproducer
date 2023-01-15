package me.tom.reverseproxyreproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.Router;
import io.vertx.httpproxy.HttpProxy;


public class MainVerticle extends AbstractVerticle{
  @Override
  public void start() {
    HttpClient proxyClient = vertx.createHttpClient();
    HttpProxy reverseProxy = HttpProxy.reverseProxy(proxyClient);
    reverseProxy.origin(80, "api.datamuse.com");

    Router router = Router.router(vertx);

    router
      .route("/words/*")
      .handler((rc) -> {
        rc.vertx().setTimer(1, tid -> rc.next());
      })
      .handler((rc) -> {
        System.out.println("handling proxy request: " + rc.request().absoluteURI());
        reverseProxy.handle(rc.request());
      });
    
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);
  }
}
