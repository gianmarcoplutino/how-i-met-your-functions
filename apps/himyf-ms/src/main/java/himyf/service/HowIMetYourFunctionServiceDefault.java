package himyf.service;

import himyf.controller.request.CharacterRequest;
import himyf.entity.Character;
import himyf.mapper.HowIMetYourFunctionMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openapi.quarkus.howIMetYourFunction_functions_json.api.HowIMetYourFunctionApi;

@ApplicationScoped
@RequiredArgsConstructor
public class HowIMetYourFunctionServiceDefault implements HowIMetYourFunctionService {

    private final HowIMetYourFunctionMapper howIMetYourFunctionMapper;

    @RestClient
    @Inject
    HowIMetYourFunctionApi howIMetYourFunctionApi;

    @Override
    public Uni<Void> persist(CharacterRequest characterRequest) {
        Character character = howIMetYourFunctionMapper.toEntity(characterRequest);
        return Character.persist(character)
                .replaceWith(character)
                .onItem().transformToUni(persistedCharacter -> callFunction(persistedCharacter.getId()));
    }

    private Uni<Void> callFunction(String characterId) {
        return Uni.createFrom().item(howIMetYourFunctionApi.apiHowIMetYourFunctionGet(characterId))
                .replaceWith(Uni.createFrom().voidItem());
    }
}
