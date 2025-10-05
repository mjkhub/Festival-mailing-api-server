package kori.tour.email.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EmailContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_content_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String body;

    @Column(name = "ses_message_id", length = 36, unique = true)
    private String messageId;

}
