package group2.wed.services.interfaces;

import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.GetFilesRequest;
import group2.wed.controllers.um.request.UploadFileRequest;
import group2.wed.entities.File;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesServiceInterface {
    public void init();

    public File save(UploadFileRequest request) throws Exception;
    public List<File> getListFiles(GetFilesRequest request);
    public Resource load(Integer fileId);

    public Message deleteAll() throws IOException;

    public Stream<Path> loadAll();
    public Resource downloadAll() throws Exception;
}
