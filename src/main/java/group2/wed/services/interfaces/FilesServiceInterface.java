package group2.wed.services.interfaces;

import group2.wed.controllers.um.request.GetFilesRequest;
import group2.wed.controllers.um.request.UploadFileRequest;
import group2.wed.entities.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesServiceInterface {
    public void init();

    public File save(UploadFileRequest request) throws IOException;
    public List<File> getListFiles(GetFilesRequest request);
    public Resource load(Integer fileId);

    public void deleteAll() throws IOException;

    public Stream<Path> loadAll();
}
