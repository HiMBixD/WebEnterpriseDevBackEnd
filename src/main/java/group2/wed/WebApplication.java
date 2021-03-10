package group2.wed;

import group2.wed.services.FilesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {
	@Resource
	FilesService filesService;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		filesService.deleteAll();
		filesService.init();
	}
}
