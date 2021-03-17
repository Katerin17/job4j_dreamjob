package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RemoveServlet extends HttpServlet {

    private static final String pathName = "/Users/ekaterinamironenko/Desktop/images";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String mode = req.getParameter("mode");
        if (mode.equals("2")) {
            Candidate removedCandidate = PsqlStore.instOf().findCanById(Integer.parseInt(id));
            PsqlStore.instOf().deleteCan(removedCandidate);
        }
        for (File file : Objects.requireNonNull(new File(pathName).listFiles())) {
            String shortFileName = file.getName();
            if (id.equals(shortFileName.substring(0, shortFileName.lastIndexOf(".")))) {
                file.delete();
                break;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
