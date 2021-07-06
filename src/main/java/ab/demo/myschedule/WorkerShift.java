package ab.demo.myschedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id"})
public class WorkerShift {
    @Id
    @GeneratedValue
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private long id;

    @NonNull
    private String name;
    @NonNull
    private LocalDate date;
    @NonNull
    private String shift;
}
