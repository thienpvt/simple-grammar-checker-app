package ss2.group15.hanu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Persistent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub")
    private String sub;

    @Column(name = "type")
    private String type;

    @Column(name = "content",columnDefinition = "text")
    private String inputText;

    @Column(name = "result",columnDefinition = "text")
    private String result;

    @Column(name = "date_request")
    private LocalDateTime dateRequest;

    @PrePersist
    void prePersist() {
        dateRequest = LocalDateTime.now();
    }
}
