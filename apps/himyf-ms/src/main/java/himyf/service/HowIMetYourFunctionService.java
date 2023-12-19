package himyf.service;

import himyf.controller.request.CharacterRequest;
import io.smallrye.mutiny.Uni;

public interface HowIMetYourFunctionService {

    Uni<Void> persist(CharacterRequest characterRequest);
}
