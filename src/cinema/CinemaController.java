package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaController {

    Cinema cinema = new Cinema(9,9);

    Map<UUID,Seat> tickets = new HashMap<UUID,Seat>();

    @GetMapping("/seats")
    public Cinema seats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat requiredSeat) {

        int row = requiredSeat.getRow();
        int column = requiredSeat.getColumn();

        if(row<1 || row > cinema.getRows() || column<1 || column > cinema.getColumns()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "The number of a row or a column is out of bounds!"));
        }
        Seat selectedSeat = cinema.getSeats().stream().filter(seat->seat.getRow() == row && seat.getColumn() == column)
                .findFirst().orElse(null);

        if(!selectedSeat.isAvailable()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "The ticket has been already purchased!"));
        }

        selectedSeat.setAvailable(false);

        UUID token = UUID.randomUUID();

        tickets.put(token,selectedSeat);

        return ResponseEntity.ok(Map.of("token", token,
                                        "ticket",tickets.get(token)));
    }


    @PostMapping("/return")
    public ResponseEntity<?> returnSeat(@RequestBody Map<String,UUID> returnRequest) {
        UUID token = returnRequest.get("token");
        if(!tickets.containsKey(token)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Wrong token!"));
        }
        var response = tickets.get(token);
        response.setAvailable(true);
        tickets.remove(token);
        return ResponseEntity.ok(Map.of("ticket",response));
    }
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password) {
        if(!"super_secret".equals(password)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "The password is wrong!"));
        }
        int income = 0;
        int available = 0;
        int purchased = 0;

        for(Seat seat : cinema.getSeats()){
            if(!seat.isAvailable()){
                purchased++;
                income+=seat.getPrice();
            }
        }
        available = cinema.getSeats().size() - purchased;
        return ResponseEntity.ok(Map.of("income",income,"available",available,"purchased",purchased));
    }
}
