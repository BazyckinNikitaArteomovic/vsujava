package ru.vsu.cs.bazykin.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.vsu.cs.bazykin.repository.ArchiveRepository;
import ru.vsu.cs.bazykin.service.ArchiveService;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        ArchiveRepository repository = new ArchiveRepository();
        ArchiveService service = new ArchiveService(repository);

        ctx.setAttribute("archiveService", service);
    }
}