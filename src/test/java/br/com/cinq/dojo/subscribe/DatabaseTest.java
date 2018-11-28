package br.com.cinq.dojo.subscribe;

import br.com.cinq.dojo.subscribe.bean.Subscription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseTest {

  public static final String TESTING_SUBSCRIBER = "DatabaseTest.testingSubscriber";

  @Autowired
  private MongoTemplate mongoTemplate;

  @Before
  public void beforeMethod() {
    mongoTemplate.dropCollection(Subscription.class);
  }

  @Test
  public void contextLoads() { }

  @Test
  public void shouldPersistASubscription() {
    final Subscription subscription = mongoTemplate.save(new Subscription(TESTING_SUBSCRIBER));

    Assert.assertNotNull(subscription);
    Assert.assertEquals(TESTING_SUBSCRIBER, subscription.getName());
    Assert.assertEquals(subscription, mongoTemplate.findById(subscription.getId(), Subscription.class));
  }

  @Test
  public void shouldPersistAndFindSubscriptionBySubscriberName() {
    final Subscription subscription = mongoTemplate.save(new Subscription(TESTING_SUBSCRIBER));

    Assert.assertNotNull(subscription);
    Assert.assertEquals(TESTING_SUBSCRIBER, subscription.getName());
    Assert.assertTrue(mongoTemplate.exists(Query.query(Criteria.where("subscriberName").is(
        TESTING_SUBSCRIBER)), Subscription.class));
  }
}
