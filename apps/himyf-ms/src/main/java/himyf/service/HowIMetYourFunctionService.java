package himyf.service;

import himyf.controller.request.CharacterRequest;
import himyf.entity.Character;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface HowIMetYourFunctionService {

    Uni<Void> persist(CharacterRequest characterRequest);

    Uni<List<Character>> getAll();
}
