package pl.tdelektro.CarRental.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime timestamp;
    public List<String> messageList;

    public ErrorResponse(List<String> messageList) {
        this.timestamp = LocalDateTime.now();
        this.messageList = messageList;
    }

}
