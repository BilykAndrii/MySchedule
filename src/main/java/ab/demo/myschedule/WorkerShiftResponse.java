package ab.demo.myschedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "with")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkerShiftResponse {
    @NonNull
    private String message;
    private List<WorkerShift> data;
}
