package projeto.multimidia.filesbiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="projeto.multimidia.controller")
public class FilesbibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesbibliotecaApplication.class, args);
	}

}
