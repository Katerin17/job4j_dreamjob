package ru.job4j.dream.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class DownloadServlet extends HttpServlet {

    private static final String pathName = "/Users/ekaterinamironenko/Desktop/images";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        File folder = new File(pathName);
        if (!folder.exists()) {
           if (!folder.mkdir()) {
               throw new RuntimeException();
           }
        }
        String nameId = req.getParameter("nameId");
        if (nameId.contains(".")) {
            nameId = getNameWithoutExtension(nameId);
        }
        File downloadFile = null;
        for (File file : folder.listFiles()) {
            String shortFileName = file.getName();
            if (nameId.equals(getNameWithoutExtension(shortFileName))) {
                downloadFile = file;
                break;
            }
        }
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + Objects.requireNonNull(downloadFile).getName() + "\"");
        try (FileInputStream stream = new FileInputStream(Objects.requireNonNull(downloadFile))) {
            resp.getOutputStream().write(stream.readAllBytes());
        }
    }

    private String getNameWithoutExtension(String name) {
        int lastIndexOfPoint = name.lastIndexOf(".");
        return name.substring(0, lastIndexOfPoint);
    }
}

