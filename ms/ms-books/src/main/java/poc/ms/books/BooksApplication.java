package poc.ms.books;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableMongoRepositories
@EnableEurekaClient
@SpringBootApplication
public class BooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksApplication.class, args);
	}
}

@Document
class Book {
	private @Id Integer id;
	private String name;

	public Book() {}

	public Book(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() { return id; }
	public String getName() { return name; }

}

@RestController
@RequestMapping("/books")
class BookController{

	private final BookRepository bookRepository;

	@Autowired
	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@PostConstruct
	public void init(){
		if(((List)this.bookRepository.findAll()).isEmpty()){
			this.bookRepository.save(new Book(1,"alalal"));
		}
	}

	@GetMapping
	public List<Book> getBooks(){
		return (List<Book>) this.bookRepository.findAll();
	}

	@PostMapping
	public void saveBook(Book book){
		this.bookRepository.save(book);
	}

}

interface BookRepository extends CrudRepository<Book, Integer>{}