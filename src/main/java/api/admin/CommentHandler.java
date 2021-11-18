package api.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import api.ApiUtils;
import api.Constants;
import api.Handler;
import api.ResponseEntity;
import api.StatusCode;
import domain.admin.*;
import errors.ApplicationExceptions;
import errors.GlobalExceptionHandler;

public class CommentHandler extends Handler{

    private final CommentService commentService;

    public CommentHandler(CommentService commentService, ObjectMapper objectMapper,
                                   GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.commentService = commentService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response=null;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());

        } else if ("GET".equals(exchange.getRequestMethod())) {

            ResponseEntity e = doGet();
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());

        } else if ("PUT".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPut(exchange);
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());

        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doDelete(exchange);
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }


        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();

    }

    private ResponseEntity<CommentResponse> doPost(InputStream is) {
    	CommentRequest commentRequest = super.readRequest(is, CommentRequest.class);

    	Comment comment = Comment.builder()
    			.id(commentRequest.getId())
                .commenter(commentRequest.getCommenter())
                .text(commentRequest.getText())
                .build();

        String newComment = commentService.create(comment);

        CommentResponse response = new CommentResponse(newComment);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity<CommentListResponse> doGet() {

             CommentListResponse commentListResponse = new CommentListResponse(commentService.getComments());
        return new ResponseEntity<>(commentListResponse,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);

    }

    private ResponseEntity<Comment> doPut(HttpExchange exchange) {

        Map<String, List<String>> params = ApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
        String commentId = params.getOrDefault("id", List.of("")).stream().findFirst().orElse("");
        CommentRequest commentRequest = super.readRequest(exchange.getRequestBody(), CommentRequest.class);
        Comment commentForUpdate = Comment.builder()
    			.id(commentRequest.getId())
                .commenter(commentRequest.getCommenter())
                .text(commentRequest.getText())
                .build();
        Comment commentAfterUpdate= commentService.updateComment(commentForUpdate);
        return new ResponseEntity<>(commentAfterUpdate,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);

    }

    private ResponseEntity<String> doDelete(HttpExchange exchange) {
        Map<String, List<String>> params = ApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
        String deleteComment = params.getOrDefault("id", List.of("")).stream().findFirst().orElse(null);
        commentService.deleteComment(deleteComment);
       return new ResponseEntity<>("Comment successfully deleted",
                getHeaders(Constants.CONTENT_TYPE, Constants.PLAIN_TXT), StatusCode.OK);
    }
	
}