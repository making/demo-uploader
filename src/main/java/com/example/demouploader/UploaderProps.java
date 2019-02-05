package com.example.demouploader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "uploader")
public class UploaderProps {

    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String fileSystemPathPattern() {
        return String.format("file://%s/*", this.getDir());
    }
}
