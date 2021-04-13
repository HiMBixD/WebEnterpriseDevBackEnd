package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.GetFilesRequest;
import group2.wed.controllers.um.request.UploadFileRequest;
import group2.wed.entities.File;
import group2.wed.entities.Submission;
import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.FileRepository;
import group2.wed.repository.SubmissionRepository;
import group2.wed.services.interfaces.FilesServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

//import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FilesService implements FilesServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(FilesService.class);
    private final Path root = Paths.get(AppConstants.ROOT_FOLDER);
    private final Path tempt = Paths.get(AppConstants.TEMP_FOLDER);


    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            } else LOG.info("Root existed");
            if (Files.exists(tempt)) {
                FileSystemUtils.deleteRecursively(tempt.toFile());
            }
            if (!Files.exists(tempt)) {
                Files.createDirectory(tempt);
            } else LOG.info("Tempt existed");
        } catch (IOException e) {
            throw new RuntimeException("Create Tempt folder failed");
        }
    }

    @Override
    public File save(UploadFileRequest request) throws Exception {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            if (request.getFile().isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "file"));
            }
            Optional<Submission> optionalSubmission = submissionRepository.findById(request.getSubmissionId().intValue());
            if (optionalSubmission.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "submissionId"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            String url = AppConstants.ROOT_FOLDER + '/' + userDetails.getUsername() + '/' + optionalSubmission.get().getSubmissionId();
            Path path = Paths.get(url);
            MultipartFile file = request.getFile();
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            java.io.File fil = new java.io.File(url+'/'+file.getOriginalFilename());
            if (fil.exists() && !fil.isDirectory()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "file"));
            }
            Files.copy(request.getFile().getInputStream(), path.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            File fileSave = new File();
            fileSave.setFileName(file.getOriginalFilename());
            fileSave.setFileUrl(url);
            fileSave.setSubmissionId(request.getSubmissionId().intValue());
            fileSave.setUsername(userDetails.getUsername());
            fileRepository.save(fileSave);
            return fileSave;
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<File> getListFiles(GetFilesRequest request) {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            Optional<Submission> optional = submissionRepository.findById(request.getSubmissionId().intValue());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "submissionId"));
            }
            List<File> files = fileRepository.getAllBySubmissionId(optional.get().getSubmissionId());
            return files;
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public Resource load(Integer fileId) {
        try {
            Optional<File> optionalFile = fileRepository.findById(fileId);
            if (optionalFile.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "fileId"));
            }
            Path file = Paths.get((optionalFile.get().getFileUrl() + "/" + optionalFile.get().getFileName()));
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
    public void downloadAll() throws Exception {
        try {
            Path zipFile = Files.createFile(Paths.get(AppConstants.TEMP_FOLDER + "/" + AppConstants.ROOT_FOLDER + ".zip"));

            Path sourceDirPath = root;
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
                 Stream<Path> paths = Files.walk(sourceDirPath)) {
                paths
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                            try {
                                zipOutputStream.putNextEntry(zipEntry);
                                Files.copy(path, zipOutputStream);
                                zipOutputStream.closeEntry();
                            } catch (IOException e) {
                                throw new AppResponseException(new Message("IOExcept", "zip file Fail"));
                            }
                        });
            }
            LOG.info("Zip is created at : "+zipFile);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Message deleteAll() {
        try {
            fileRepository.deleteAll();
            FileSystemUtils.deleteRecursively(root.toFile());
            FileSystemUtils.deleteRecursively(tempt.toFile());

            FileSystemUtils.deleteRecursively(Paths.get("backup").toFile());

            Message message = new Message();
            message.setMessage("Roots Deleted");
            LOG.info(message.getMessage());
            return message;
        } catch (Exception e){
            throw e;
        }
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
