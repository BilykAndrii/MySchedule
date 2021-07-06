package ab.demo.myschedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
public class WorkerShiftController {

    private static final String NO_DATA = "No data about shifts of this worker";
    private static final String CREATED = "Shift created";
    private static final String ALREADY_EXIST = "Shift already exist for this date";
    private static final String REMOVED = "Shift has been removed";
    private static final String ALL_REMOVED = "All shifts have been removed";
    private static final String NOTHING_TO_REMOVE = "Nothing to remove";
    private static final String NOT_EXIST = "Shift isn't exist";
    private static final String CHANGED = "Shift has been changed";
    private static final String OK = "Data exists";

    @Autowired
    private WorkerShiftService service;

    @GetMapping("/help")
    public String getHelpInfo() {
        return service.getHelpInfo();
    }

    @GetMapping("/all/{worker}")
    public ResponseEntity<WorkerShiftResponse> getAllShifts(@PathVariable String worker) {

        String validation = service.validateParameters(worker, null, null, false, false);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        List<WorkerShift> shifts = service.getAllShifts(worker);
        if (shifts.isEmpty()) {
            return ResponseEntity.ok(WorkerShiftResponse.of(NO_DATA));
        } else {
            return ResponseEntity.ok(WorkerShiftResponse.with(OK, shifts));
        }
    }

    @GetMapping("/get/{worker}/{date}")
    public ResponseEntity<WorkerShiftResponse> getShift(@PathVariable String worker, @PathVariable String date) {

        String validation = service.validateParameters(worker, date, null, true, false);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        List<WorkerShift> shifts = service.getShift(worker, date);
        if (shifts.isEmpty()) {
            return ResponseEntity.ok(WorkerShiftResponse.of(NO_DATA));
        } else {
            return ResponseEntity.ok(WorkerShiftResponse.with(OK, shifts));
        }
    }

    @PostMapping("/add/{worker}/{date}/{shift}")
    public ResponseEntity<WorkerShiftResponse> createShift(@PathVariable String worker, @PathVariable String date, @PathVariable String shift) {

         String validation = service.validateParameters(worker, date, shift, true, true);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        boolean success = service.createShift(worker, date, shift);
        if (success) {
            return ResponseEntity.ok(WorkerShiftResponse.of(CREATED));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(ALREADY_EXIST));
        }
    }

    @DeleteMapping("/remove/{worker}/{date}")
    public ResponseEntity<WorkerShiftResponse> removeShift(@PathVariable String worker, @PathVariable String date) {

        String validation = service.validateParameters(worker, date, null, true, false);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        boolean success = service.removeShift(worker, date);
        if (success) {
            return ResponseEntity.ok(WorkerShiftResponse.of(REMOVED));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(NOT_EXIST));
        }
    }

    @DeleteMapping("/removeAll/{worker}")
    public ResponseEntity<WorkerShiftResponse> removeShift(@PathVariable String worker) {

        String validation = service.validateParameters(worker, null, null, false, false);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        boolean success = service.removeAllShifts(worker);
        if (success) {
            return ResponseEntity.ok(WorkerShiftResponse.of(ALL_REMOVED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WorkerShiftResponse.of(NOTHING_TO_REMOVE));
        }
    }

    @PutMapping("/change/{worker}/{date}/{shift}")
    public ResponseEntity<WorkerShiftResponse> changeShift(@PathVariable String worker, @PathVariable String date, @PathVariable String shift) {

        String validation = service.validateParameters(worker, date, shift, true, true);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkerShiftResponse.of(validation));
        }

        boolean success = service.changeShift(worker, date, shift);
        if (success) {
            return ResponseEntity.ok(WorkerShiftResponse.of(CHANGED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WorkerShiftResponse.of(NOT_EXIST));
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(false);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}
