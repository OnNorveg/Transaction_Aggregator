package aggregator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
public class TransactionController implements CommandLineRunner {

    private final TransactionService transactionService;
    private final String URL1 = "http://localhost:8888/transactions?account={account}";
    private final String URL2 = "http://localhost:8889/transactions?account={account}";

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/aggregate")
    public ResponseEntity<List<Transaction>> resendGet(@RequestParam String account){
        Map<String,String> param = new HashMap<>();
        param.put("account",account);
        List<Transaction> transactions = new LinkedList<>();
        transactions.addAll(transactionService.getTransactions(URL1, param));
        transactions.addAll(transactionService.getTransactions(URL2, param));
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Override
    public void run(String... args) throws Exception {

        CompletableFuture<List<Transaction>> transactions1 = transactionService.getTransactions(URL1, param);

    }
}
