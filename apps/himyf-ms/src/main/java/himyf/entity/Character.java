package himyf.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection="characters")
public class Character extends ReactivePanacheMongoEntity {
    private String fileId;
    private String name;
    private String surname;
    private String email;
    private String birthdate;
}