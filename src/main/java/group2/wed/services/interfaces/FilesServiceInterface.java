package group2.wed.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesServiceInterface {
    public void init();

    public void save(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll() throws IOException;

    public Stream<Path> loadAll();
}
