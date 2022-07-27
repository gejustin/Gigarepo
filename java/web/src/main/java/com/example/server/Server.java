package com.example.server;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Server extends HttpServlet {

    static final String DB_URL = "jdbc:mysql://localhost/users";
    static Connection conn;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie c = new Cookie("uid", req.getSession().getId());
        // For older browsers?
        c.setSecure(false);
        resp.addCookie(c);
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Boolean b = Boolean.parseBoolean(req.getParameter("winCondition"));

        int ticketNumber = Integer.parseInt(req.getParameter("ticket"));
        try {
            Statement s = conn.createStatement();
            s.execute("SELECT userName, isWin FROM users WHERE uid = " + ticketNumber + ";");
            ResultSet r = s.getResultSet();

            String id = r.getString(0);

            if (r.getBoolean("isWin") && b) {
                resp.getWriter().print("You win, " + r.getString("userName") + "!<br>You can fill your details in with this link: " + req.getParameter(id));
            } else {
                resp.getWriter().print("You lose, " + r.getString("userName"));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            conn = DriverManager.getConnection(DB_URL, "user", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
