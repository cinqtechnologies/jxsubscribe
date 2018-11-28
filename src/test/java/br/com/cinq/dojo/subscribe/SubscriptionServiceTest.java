package br.com.cinq.dojo.subscribe;

import br.com.cinq.dojo.subscribe.bean.Subscription;
import br.com.cinq.dojo.subscribe.exception.SubscriptionAlreadyExistsException;
import br.com.cinq.dojo.subscribe.service.SubscriptionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriptionServiceTest {

  public static final String TESTING_SUBSCRIBER = "SubscriptionService.testingSubscriber";

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private SubscriptionService service;

  @Before
  public void beforeMethod() {
    mongoTemplate.dropCollection(Subscription.class);
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void shouldSubscribeAndGetASubscription() {
    final Subscription subscription = service.subscribe(TESTING_SUBSCRIBER);
    Assert.assertNotNull(subscription);
    Assert.assertEquals(TESTING_SUBSCRIBER, subscription.getName());
    Assert.assertEquals(subscription, service.getSubscription(TESTING_SUBSCRIBER));
  }

  @Test(expected = SubscriptionAlreadyExistsException.class)
  public void shouldShouldFailWhenTryingToSubscribeTwice() {
    final Subscription subscription = service.subscribe(TESTING_SUBSCRIBER);
    Assert.assertNotNull(subscription);
    Assert.assertEquals(TESTING_SUBSCRIBER, subscription.getName());
    Assert.assertEquals(subscription, service.getSubscription(TESTING_SUBSCRIBER));

    service.subscribe(TESTING_SUBSCRIBER);
  }
}
