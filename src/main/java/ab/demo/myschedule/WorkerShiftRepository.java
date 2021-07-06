package ab.demo.myschedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkerShiftRepository extends CrudRepository<WorkerShift, Long> {

    List<WorkerShift> findByNameAndDate(String name, LocalDate date);

    List<WorkerShift> findByName(String name);

    void deleteByNameAndDate(String name, LocalDate date);

    void deleteByName(String name);
}
