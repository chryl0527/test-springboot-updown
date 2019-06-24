package com.chryl.oth;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created By Chr on 2019/6/24.
 */
@Component
public class StorageServiceImpl {
    public final Path rootLocation;

    public StorageServiceImpl() {
        this.rootLocation = Paths.get("D:/test2");
    }

    public Path load(String filename) {

        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                // throw new StorageFileNotFoundException("Could not read file: " + filename)
            }
        } catch (MalformedURLException e) {
            //throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
        return null;
    }
}
