package br.com.cinq.dojo.subscribe;

import br.com.cinq.dojo.subscribe.bean.Subscription;
import java.net.URI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriptionControllerTest {

  public static final String TESTING_SUBSCRIBER = "SubscriptionController.testingSubscriber";

  @Value("${local.server.port}")
  private int port;

  @Autowired
  private MongoTemplate mongoTemplate;

  private RestTemplate rest = new RestTemplate();

  @Before
  public void beforeMethod() {
    mongoTemplate.dropCollection(Subscription.class);
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void shouldSubscribeAPerson() {
    final String uriString = String
        .format("http://localhost:%d/jx/subscribe/%s", port, TESTING_SUBSCRIBER);
    final ResponseEntity<Subscription> response = rest
        .postForEntity(URI.create(uriString), null, Subscription.class);
    Assert.assertTrue(response.getHeaders().get("Location").stream().anyMatch(uriString::equals));
  }

  @Test
  public void shouldSubscribeAPersonAndReturnSameSubscriptionInTheSecondAttempt() {
    final String uriString = String
        .format("http://localhost:%d/jx/subscribe/%s", port, TESTING_SUBSCRIBER);
    final ResponseEntity<Subscription> response = rest
        .postForEntity(URI.create(uriString), null, Subscription.class);
    Assert.assertTrue(response.getHeaders().get("Location").stream().anyMatch(uriString::equals));

    final Subscription subscription = rest.getForEntity(URI.create(uriString), Subscription.class)
        .getBody();

    final ResponseEntity<Subscription> secondResponse = rest
        .postForEntity(URI.create(uriString), null, Subscription.class);
    Assert.assertTrue(response.getHeaders().get("Location").stream().anyMatch(uriString::equals));

    final Subscription secondSubscription = rest
        .getForEntity(URI.create(uriString), Subscription.class).getBody();

    Assert.assertEquals(subscription, secondSubscription);

    Assert.assertTrue(mongoTemplate.getCollection("subscriptions").count() == 1);

  }
}
