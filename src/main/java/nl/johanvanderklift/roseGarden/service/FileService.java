package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.exception.FileNotFoundException;
import nl.johanvanderklift.roseGarden.model.File;
import nl.johanvanderklift.roseGarden.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void uploadFile(String filename, byte[] docFile) {
        File file = new File();
        file.setFilename(filename);
        file.setDocFile(docFile);
        fileRepository.save(file);
    }

    public File getSingleFile(Long id) {
        Optional<File> opFile = fileRepository.findById(id);
        if (opFile.isEmpty()) {
            throw new FileNotFoundException(id);
        } else {
            return opFile.get();
        }
    }
}
