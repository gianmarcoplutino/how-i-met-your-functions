package himyf.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class CharacterRequest {
        private String name;
        private String surname;
        private String email;
        private String birthdate;
}
