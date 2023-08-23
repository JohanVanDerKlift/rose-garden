package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.model.File;
import nl.johanvanderklift.roseGarden.repository.FileRepository;
import nl.johanvanderklift.roseGarden.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/pdf/{username}")
    public ResponseEntity<Long> singleFileUpload(@RequestParam("file")MultipartFile file, @PathVariable String username) throws IOException {
        Long newId = fileService.uploadFile(file.getOriginalFilename(), file.getBytes(), username);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + newId).toUriString());
        return ResponseEntity.created(uri).body(newId);
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

    @DeleteMapping("/file/delete/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok().build();
    }
}