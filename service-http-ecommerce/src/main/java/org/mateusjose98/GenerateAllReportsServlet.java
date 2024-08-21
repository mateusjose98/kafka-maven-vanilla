package org.mateusjose98;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportsServlet extends HttpServlet {
    private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {



        try {
            // manda para o tópuco SEND_MESSAGE_TO_ALL_USERS a mensagem USER_GENERATE_REPORT
            // ou seja, para todos os usuários, gerar o relatório
            // a mensagem será lida pela classe BatchSendMessageService
            batchDispatcher.send(
                    KAKFA_CONSTANTS.ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS,
                    KAKFA_CONSTANTS.ECOMMERCE_USER_GENERATE_REPORT,
                    KAKFA_CONSTANTS.ECOMMERCE_USER_GENERATE_REPORT,
                    new CorrelationId(GenerateAllReportsServlet.class.getSimpleName()),
                    null
            );
           resp.setStatus(HttpServletResponse.SC_OK);
              resp.getWriter().println("Reports scheduled");

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
