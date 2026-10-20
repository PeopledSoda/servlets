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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/save")
public class SaveServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int usersToAdd = userRepository.getUsersToAdd();
            request.setAttribute("usersToAdd", usersToAdd);
            request.getRequestDispatcher("/jsp/save.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not read users count from database", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int usersToAdd = userRepository.getUsersToAdd();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < usersToAdd; i++) {
                users.add(User.builder()
                        .name(required(request, "name_" + i))
                        .surname(required(request, "surname_" + i))
                        .age(parseAge(request, "age_" + i))
                        .email(required(request, "email_" + i))
                        .city(required(request, "city_" + i))
                        .phone(required(request, "phone_" + i))
                        .build());
            }
            userRepository.saveUsers(users);
            response.sendRedirect(request.getContextPath() + "/html/saved.html");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Could not save users", e);
        }
    }

    private String required(HttpServletRequest request, String parameter) {
        String value = request.getParameter(parameter);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Field " + parameter + " is required");
        }
        return value.trim();
    }

    private int parseAge(HttpServletRequest request, String parameter) {
        try {
            int age = Integer.parseInt(required(request, parameter));
            if (age < 0) {
                throw new NumberFormatException();
            }
            return age;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Field " + parameter + " must contain a non-negative integer");
        }
    }
}
