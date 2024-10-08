package org.mateusjose98;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));
            var orderId = UUID.randomUUID().toString();
            sendOrder(email, amount, orderId, orderDispatcher);
            System.out.println("New order sent successfully.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent id=" + orderId);

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

    private static void sendOrder(String email, BigDecimal amount, String orderId,
                                  KafkaDispatcher<Order> orderDispatcher
                                 ) throws ExecutionException, InterruptedException {

        var orderPayload = new Order(orderId, amount, email);
        orderDispatcher.send(KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                email,
                orderPayload,
                new CorrelationId(NewOrderServlet.class.getSimpleName()),
                null);


    }
}
