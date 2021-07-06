package ab.demo.myschedule;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class WorkerShiftService {

    private static final String SHIFT_NOT_VALID = "shift name is not valid, see /help";
    private static final String NAME_NOT_VALID = "worker name is empty";
    private static final String DATE_NOT_VALID = "date is not valid, format is YYYY-MM-dd";

    @Value("${help.info:}")
    private String helpInfo;

    @Autowired
    private WorkerShiftRepository workerShiftRepository;

    public String validateParameters(String name, String date, String shift, boolean dateIsRequired, boolean shiftIsRequired) {
        StringBuilder message = new StringBuilder();

        if (Strings.isBlank(name)) {
            message.append(NAME_NOT_VALID);
            message.append("; ");
        }

        if (dateIsRequired && dateNotValid(date)) {
            message.append(DATE_NOT_VALID);
            message.append("; ");
        }

        if (shiftIsRequired && shiftNotValid(shift)) {
            message.append(SHIFT_NOT_VALID);
        }

        return message.toString();
    }

    public String getHelpInfo() {
        if (Strings.isBlank(helpInfo)) {
            return Arrays.toString(Timetable.values());
        } else {
            return helpInfo;
        }
    }

    public List<WorkerShift> getAllShifts(String name) {
        return workerShiftRepository.findByName(name);
    }

    public List<WorkerShift> getShift(String name, String date) {
        return workerShiftRepository.findByNameAndDate(name, LocalDate.parse(date));
    }

    public boolean createShift(String name, String date, String shift) {
        List<WorkerShift> shifts = workerShiftRepository.findByNameAndDate(name, LocalDate.parse(date));
        if (!shifts.isEmpty()) {
            return false;
        }

        workerShiftRepository.save(WorkerShift.of(name, LocalDate.parse(date), shift));
        return true;
    }

    public boolean removeShift(String name, String date) {
        List<WorkerShift> shifts = workerShiftRepository.findByNameAndDate(name, LocalDate.parse(date));
        if (shifts.isEmpty()) {
            return false;
        }

        workerShiftRepository.deleteByNameAndDate(name, LocalDate.parse(date));
        return true;
    }

    public boolean removeAllShifts(String name) {
        List<WorkerShift> shifts = workerShiftRepository.findByName(name);
        if (shifts.isEmpty()) {
            return false;
        }

        workerShiftRepository.deleteByName(name);
        return true;
    }

    public boolean changeShift(String name, String date, String shift) {
        List<WorkerShift> shifts = workerShiftRepository.findByNameAndDate(name, LocalDate.parse(date));
        if (shifts.isEmpty()) {
            return false;
        }
        workerShiftRepository.deleteByNameAndDate(name, LocalDate.parse(date));
        workerShiftRepository.save(WorkerShift.of(name, LocalDate.parse(date), shift));
        return true;
    }

    private boolean shiftNotValid(String shift) {
        return Timetable.ofDescription(shift) == null;
    }

    private boolean dateNotValid(String dateStr) {
        try {
            LocalDate.parse(dateStr);
        } catch (Exception ex) {
            return true;
        }
        return false;
    }
}
