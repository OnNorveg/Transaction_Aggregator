package aggregator;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class TransactionController {

    private final String URL1 = "http://localhost:8888/transactions?account={account}";
    private final String URL2 = "http://localhost:8889/transactions?account={account}";
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/aggregate")
    public ResponseEntity<List<Transaction>> resendGet(@RequestParam String account){
        Map<String,String> param = new HashMap<>();
        param.put("account",account);
        List<Transaction> transactions = new LinkedList<>();
        transactions.addAll(getTransactions(URL1, param));
        transactions.addAll(getTransactions(URL2, param));
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    private List<Transaction> getTransactions(String URL, Map<String,String> param){
        ResponseEntity<List<Transaction>> response = restTemplate.exchange(URL, HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Transaction>>() {},param);
        return response.getBody();
    }
}
