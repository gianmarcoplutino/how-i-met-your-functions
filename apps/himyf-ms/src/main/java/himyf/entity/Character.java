package himyf.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection="characters")
public class Character extends ReactivePanacheMongoEntity {
    private String fileId;
    private String nome;
    private String cognome;
    private String mail;
    private Date dataDiNascita;
}