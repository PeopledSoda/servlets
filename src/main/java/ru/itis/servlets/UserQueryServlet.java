package ru.itis.servlets;

import ru.itis.models.User;
import ru.itis.repositories.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user-query")
public class UserQueryServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String field = request.getParameter("field");
        String value = request.getParameter("value");
        if (field == null || value == null || value.trim().isEmpty()) {
            request.getRequestDispatcher("/jsp/user-query.jsp").forward(request, response);
            return;
        }

        try {
            List<User> users;
            if ("email".equals(field)) {
                users = findByEmail(value.trim());
            } else if ("city".equals(field)) {
                users = findByCity(value.trim());
            } else if ("phone".equals(field)) {
                users = findByPhone(value.trim());
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Field must be email, city or phone");
                return;
            }
            for (User user : users) {
                System.out.println(user);
            }
            response.sendRedirect(request.getContextPath() + "/html/query-completed.html");
        } catch (SQLException e) {
            throw new ServletException("Could not execute user query", e);
        }
    }

    private List<User> findByEmail(String email) throws SQLException {
        return userRepository.findByEmail(email);
    }

    private List<User> findByCity(String city) throws SQLException {
        return userRepository.findByCity(city);
    }

    private List<User> findByPhone(String phone) throws SQLException {
        return userRepository.findByPhone(phone);
    }
}
