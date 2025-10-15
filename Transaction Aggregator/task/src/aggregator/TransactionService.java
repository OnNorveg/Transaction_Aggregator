package aggregator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService  {

    RestTemplate restTemplate = new RestTemplate();
    int attempts = 0;

    @Async
    @Cacheable(cacheNames = "transactions", key = "#param")
    List<Transaction> getTransactions(String URL, Map<String,String> param){
        try {
            ResponseEntity<List<Transaction>> response = restTemplate.exchange(URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Transaction>>() {
                    }, param);
            attempts = 0;
            return response.getBody();
        } catch (Exception e) {
            attempts++;
            if(attempts < 6){
                return getTransactions(URL,param);
            } else {
                attempts = 0;
                return new LinkedList<>();
            }
        }
    }

}
