package io.wakelesstuna.userdgs.config;

import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.HttpResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author oscar.steen.forss
 */
@Configuration
public class GraphQLClientConfig {

    @Bean
    public GraphQLClient graphQLClient() {
        RestTemplate restTemplate = new RestTemplate();
        return GraphQLClient.createCustom("http://localhost:8002/graphql", (url, headers, body) -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::addAll);
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders), String.class);
            return new HttpResponse(exchange.getStatusCodeValue(), exchange.getBody());
        });
    }

}
