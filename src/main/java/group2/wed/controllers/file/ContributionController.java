package group2.wed.controllers.file;

import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.GetFilesRequest;
import group2.wed.controllers.um.request.UploadFileRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.File;
import group2.wed.services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/file")
public class ContributionController {

    @Autowired
    FilesService filesService;

    @PostMapping("/upload")
    public AppResponse uploadFile(UploadFileRequest request) {
        Message message = new Message();
        try {
            message.setMessage("Uploaded the file successfully");
            return new AppResponseSuccess(filesService.save(request), message);
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @PostMapping("/get-files")
    public AppResponse getFiles(@RequestBody GetFilesRequest request) {
        try {
            return new AppResponseSuccess(filesService.getListFiles(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @GetMapping("/read/{fileId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Integer fileId) {
        Resource file = filesService.load(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getListFiles() {
        List<File> fileInfos = filesService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ContributionController.class, "getFile", path.getFileName().toString()).build().toString();
            File file = new File();
            file.setFileName(filename);
            file.setFileUrl(url);
            return file;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
}
