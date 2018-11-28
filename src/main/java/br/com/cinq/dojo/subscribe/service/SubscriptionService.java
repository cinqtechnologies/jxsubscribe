package br.com.cinq.dojo.subscribe.service;

import br.com.cinq.dojo.subscribe.bean.Subscription;
import br.com.cinq.dojo.subscribe.exception.SubscriptionAlreadyExistsException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  private final MongoTemplate mongoTemplate;

  public SubscriptionService(final MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Subscription subscribe(String subscriber) throws SubscriptionAlreadyExistsException {
    final Query query = Query.query(Criteria.where("subscriberName").is(subscriber));
    return mongoTemplate.save(new Subscription(subscriber));
  }

  public Subscription getSubscription(final String subscriber) {
    return mongoTemplate.findOne(Query.query(Criteria.where("subscriberName").is(subscriber)), Subscription.class);
  }
}