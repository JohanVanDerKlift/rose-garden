package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.model.File;
import nl.johanvanderklift.roseGarden.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/pdf")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file")MultipartFile file) throws IOException {
        fileService.uploadFile(file.getOriginalFilename(), file.getBytes());

        return ResponseEntity.ok("File successfully uploaded");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long id) {
        File file = fileService.getSingleFile(id);
        byte[] docFile = file.getDocFile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", file.getFilename());
        headers.setContentLength(docFile.length);

        return new ResponseEntity<>(docFile, headers, HttpStatus.OK);
    }
}