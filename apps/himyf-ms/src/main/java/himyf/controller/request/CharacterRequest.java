package himyf.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class CharacterRequest {
        private String nome;
        private String cognome;
        private String mail;
        private Date dataDiNascita;
}
