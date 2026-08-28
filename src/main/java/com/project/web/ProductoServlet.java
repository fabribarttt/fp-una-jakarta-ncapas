package com.project.web;

import java.io.IOException;
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.project.config.ThymeleafConfig;
import com.project.model.Producto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class ProductoServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private EntityManagerFactory emf;

    @Override
    public void init() {
        this.templateEngine = ThymeleafConfig.getTemplateEngine(getServletContext());
        this.emf = Persistence.createEntityManagerFactory("mi-unidad-persistencia");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        var app = JakartaServletWebApplication.buildApplication(getServletContext());
        var exchange = app.buildExchange(req, resp);
        WebContext context = new WebContext(exchange);

        EntityManager em = emf.createEntityManager();
        try {
            // Consulta JPQL para obtener todos los productos de PostgreSQL
            List<Producto> listaProductos = em.createQuery("SELECT p FROM Producto p ORDER BY p.id DESC", Producto.class)
                                              .getResultList();

            // Pasamos la lista a la variable 'productos' para que la lea Thymeleaf
            context.setVariable("productos", listaProductos);

            // Renderizamos la plantilla productos.html
            templateEngine.process("index", context, resp.getWriter());
        } finally {
            em.close(); // Siempre cerrar el EntityManager
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nombreProducto = req.getParameter("nombreProducto");
        Integer cantidadStr = Integer.parseInt(req.getParameter("cantidad"));
        Double precioStr = Double.parseDouble(req.getParameter("precio"));

        EntityManager em = emf.createEntityManager();

        try {
            // Iniciar transacción de BD
            em.getTransaction().begin();

            // Crear objeto e indicarle a JPA que lo persista
            Producto nuevoProducto = new Producto(nombreProducto, cantidadStr, precioStr);
            em.persist(nuevoProducto);

            // Confirmar transacción (Aplica el INSERT en PostgreSQL)
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Revertir si ocurre un error
            }
            throw new ServletException("Error al guardar en la base de datos", e);
        } finally {
            em.close();
        }

        resp.sendRedirect(req.getContextPath() + "/");

    }
    
    @Override
    public void destroy() {
        // Cerramos el EntityManagerFactory al apagar el servidor
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.parseLong(idStr);
            EntityManager em = emf.createEntityManager();

            try {
                em.getTransaction().begin();
                Producto producto = em.find(Producto.class, id);
                if (producto != null) {
                    em.remove(producto);
                }
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }

        resp.setStatus(HttpServletResponse.SC_OK);

    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    Long id = Long.parseLong(req.getParameter("id"));
    String nombreProducto = req.getParameter("nombreProducto");
    Integer cantidad = Integer.parseInt(req.getParameter("cantidad"));
    Double precio = Double.parseDouble(req.getParameter("precio"));

    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();
        Producto producto = em.find(Producto.class, id);
        if (producto != null) {
            producto.setProducto(nombreProducto);
            producto.setCantidad(cantidad);
            producto.setPrecio(precio);
        }
        em.getTransaction().commit(); // JPA detecta los cambios solo (dirty checking), no hace falta merge()
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new ServletException("Error al actualizar en la base de datos", e);
    } finally {
        em.close();
    }

    resp.setHeader("HX-Refresh", "true"); // le dice a htmx que recargue la página
    resp.setStatus(HttpServletResponse.SC_OK);
}
}
