package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.exception.FileNotFoundException;
import nl.johanvanderklift.roseGarden.exception.UserNotFoundException;
import nl.johanvanderklift.roseGarden.model.File;
import nl.johanvanderklift.roseGarden.model.User;
import nl.johanvanderklift.roseGarden.repository.FileRepository;
import nl.johanvanderklift.roseGarden.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FileService(FileRepository fileRepository, UserRepository userRepository, UserService userService) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Long uploadFile(String filename, byte[] docFile, String username) {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            User user = opUser.get();
            if (!user.getHasCredit()) {
                userService.toggleHasCredit(user.getUsername());
            }
            File file = new File();
            file.setFilename(filename);
            file.setDocFile(docFile);
            file.setUser(user);
            File newFile = fileRepository.save(file);
            return newFile.getId();
        }
    }

    public File getSingleFile(Long id) {
        Optional<File> opFile = fileRepository.findById(id);
        if (opFile.isEmpty()) {
            throw new FileNotFoundException(id);
        } else {
            return opFile.get();
        }
    }

    public void deleteFile(Long id) {
        Optional<File> opFile = fileRepository.findById(id);
        if (opFile.isEmpty()) {
            throw new FileNotFoundException(id);
        } else {
            fileRepository.deleteById(id);
        }
    }
}
