package com.johnlyon.hoolipiper.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

@Service
public class UploadService {
    public void oneFile(MultipartFile cloudFile, String PATH) {
        File passFile = new File(PATH, Objects.requireNonNull(cloudFile.getOriginalFilename()));
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(cloudFile.getInputStream());
            FileChannel fileChannel = FileChannel.open(
                    passFile.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            readableByteChannel.close();
            fileChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moreFiles(MultipartFile[] cloudFiles, String PATH) {
        ForkJoinPool customThreadPool = new ForkJoinPool(6);
        try {
            Arrays.asList(cloudFiles).parallelStream().forEach(cloudFs -> {
                customThreadPool.submit(() -> {
                    File files = new File(PATH, Objects.requireNonNull(cloudFs.getOriginalFilename()));
                    try {
                        cloudFs.transferTo(files);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        } finally {
            customThreadPool.shutdown();
        }
    }
}
