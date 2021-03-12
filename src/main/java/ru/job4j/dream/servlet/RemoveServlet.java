package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class RemoveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        for (File file : Objects.requireNonNull(new File("/Users/ekaterinamironenko/Desktop/images").listFiles())) {
            String shortFileName = file.getName();
            if (id.equals(shortFileName.substring(0, shortFileName.lastIndexOf(".")))) {
                resp.setContentType("text/html");
                PrintWriter writer = resp.getWriter();
                if (file.delete()) {
                    writer.println("File deleted successfully.");
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Candidate removedCandidate = PsqlStore.instOf().findCanById(id);
        PsqlStore.instOf().deleteCan(removedCandidate);
        req.setAttribute("id", id);
        doGet(req, resp);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
