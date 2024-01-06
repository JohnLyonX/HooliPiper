package com.johnlyon.hoolipiper.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class DownloadService {

    public String[] showFileWarehouse(String warehousePath, String filetype, String filename) {
        File files = new File(warehousePath);
        if (filename != null) {
            String[] searchResults = Arrays.stream(files.listFiles())
                    .filter(file -> file.getName().contains(filename))
                    .map(File::getName)
                    .toArray(String[]::new);

            switch (filetype) {
                case "Environment":
                    return Arrays.stream(searchResults)
                            .filter(name -> name.endsWith(".zip") || name.endsWith(".rar") || name.endsWith(".tar") || name.endsWith(".gz"))
                            .toArray(String[]::new);
                case "Jar":
                    return Arrays.stream(searchResults)
                            .filter(name -> name.endsWith(".jar"))
                            .toArray(String[]::new);
                default:
                    return searchResults;
            }
        } else {
            switch (filetype) {
                case "All":
                    return files.list();
                case "Environment":
                    return files.list((dir, name) -> name.endsWith(".zip") || name.endsWith(".rar") || name.endsWith(".tar") || name.endsWith(".gz"));
                case "Jar":
                    return files.list((dir, name) -> name.endsWith(".jar"));

                default:
                    return new String[0];
            }
        }
    }

    public void download(HttpServletResponse response, String PATH, String filename) throws IOException {
        File file = new File(PATH, filename);
        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream inputStream = new FileInputStream(file);
        ) {
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(StandardCharsets.UTF_8),
                    "ISO8859-1"));
            response.setContentLengthLong(file.length());
            WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
            FileChannel channel = inputStream.getChannel();
            long position = 0;
            long count = channel.size();
            while (position < count) {
                position += channel.transferTo(position, count - position, writableByteChannel);
            }
            outputStream.flush();

        } catch (IOException e) {
            if (!(e instanceof org.apache.catalina.connector.ClientAbortException)) {
                // handle exception
                e.printStackTrace();
            }
        }
    }

}
