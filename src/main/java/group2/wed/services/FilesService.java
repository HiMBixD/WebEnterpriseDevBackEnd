package group2.wed.services;

import group2.wed.services.interfaces.FilesServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesService implements FilesServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(FilesService.class);
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            } else LOG.info("Root existed");
        } catch (IOException e) {
            throw new RuntimeException("Create root folder failed");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
//            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Failed to read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
        LOG.info("Root Deleted");
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load all of the files!");
        }
    }
}
