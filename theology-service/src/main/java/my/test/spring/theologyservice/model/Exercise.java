package my.test.spring.theologyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EXERCISE")
@Data @AllArgsConstructor @NoArgsConstructor
public class Exercise {
    @Id
    @Column
    private String question;
    @Column
    private String answer;
}