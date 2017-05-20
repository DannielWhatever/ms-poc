package poc.ms.bff.booksclient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableDiscoveryClient
@EnableZuulProxy
@EnableFeignClients
@SpringBootApplication
@EnableCircuitBreaker
public class BooksClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksClientApplication.class, args);
	}

}

@RestController
class BffController{

	private final ApiBooks apiBooks;

	@Autowired
	public BffController(ApiBooks apiBooks) {
		this.apiBooks = apiBooks;
	}

	@HystrixCommand(fallbackMethod = "fallbackGetBookNames")
	@GetMapping("/books/names")
	public List<String> getBookNames(){
		return this.apiBooks.getBooks().stream().map(b-> b.getName()).collect(Collectors.toList());
	}

	public List<String> fallbackGetBookNames() { return Collections.EMPTY_LIST; }

}

@FeignClient("ms-books")
interface ApiBooks {
	@RequestMapping(method = RequestMethod.GET, value = "/books")
	List<Book> getBooks();
}

class Book {
	private Integer id;
	private String name;

	public Book() {}

	public Integer getId() { return id; }
	public String getName() { return name; }

}