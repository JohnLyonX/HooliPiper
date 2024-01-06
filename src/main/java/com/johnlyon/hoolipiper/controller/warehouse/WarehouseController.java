package com.johnlyon.hoolipiper.controller.warehouse;

import com.johnlyon.hoolipiper.service.DownloadService;
import com.johnlyon.hoolipiper.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class WarehouseController {
    @Value("${warehouse.path}")
    private String warehousePath;


    @RequestMapping("/files")
    @ResponseBody
    public String[] showFiles(String filetype, String filename) {
        DownloadService downloadService = new DownloadService();
        return downloadService.showFileWarehouse(warehousePath, filetype, filename);
    }

    @RequestMapping("/download")
    @ResponseBody
    public void downloadFile(HttpServletResponse response, String filename) throws IOException {
        DownloadService downloadService = new DownloadService();
        downloadService.download(response, warehousePath, filename);
    }
}
