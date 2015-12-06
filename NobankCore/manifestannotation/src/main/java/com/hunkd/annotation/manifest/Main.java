package com.hunkd.annotation.manifest;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        Path source = Paths.get("J:\\GitHub\\Awwl\\NobankCore\\manifestannotation\\AndroidManifest.xml");
        Path target = Paths.get("J:\\GitHub\\Awwl\\NobankCore\\astub\\src\\main\\AndroidManifest.xml");
        CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
        };
        try {
            Files.copy(source, target, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
