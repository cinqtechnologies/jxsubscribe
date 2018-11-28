package br.com.cinq.dojo.subscribe.controller;

import br.com.cinq.dojo.subscribe.bean.Subscription;
import br.com.cinq.dojo.subscribe.exception.SubscriptionAlreadyExistsException;
import br.com.cinq.dojo.subscribe.service.SubscriptionService;
import javax.xml.ws.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

  private final SubscriptionService service;

  public SubscriptionController(final SubscriptionService service) {
    this.service = service;
  }

  @RequestMapping(
      value = "/{name}",
      method = RequestMethod.OPTIONS,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<?> subscribe(final @PathVariable("name") String subscriber,
      final UriComponentsBuilder uriComponentsBuilder) {
    try {
      final Subscription result = service.subscribe(subscriber);
      return ResponseEntity.created(uriComponentsBuilder.path("/subscribe/{name}").buildAndExpand(result.getName()).toUri()).build();
    } catch(SubscriptionAlreadyExistsException e) {
      return ResponseEntity.created(uriComponentsBuilder.path("/subscribe/{name}").buildAndExpand(service.getSubscription(subscriber)).toUri()).build();
    }
  }

  @RequestMapping(
      value = "{name}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<Subscription> getSubscription(final @PathVariable("name") String subscriber) {
    final Subscription result = service.getSubscription(subscriber);
    if(result == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }
}