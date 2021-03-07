package group2.wed.controllers.file;

import group2.wed.controllers.other.Message;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.File;
import group2.wed.services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/file")
public class ContributionController {

    @Autowired
    FilesService filesService;

    @PostMapping("/upload")
    public ResponseEntity<AppResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();
        try {
            filesService.save(file);

            message.setMessage("Uploaded the file successfully: " + file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(new AppResponseSuccess(message));
        } catch (Exception e) {
            message.setMessage("Could not upload the file: " + file.getOriginalFilename() + "!");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new AppResponseFailure(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getListFiles() {
        List<File> fileInfos = filesService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ContributionController.class, "getFile", path.getFileName().toString()).build().toString();

            return new File(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = filesService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
