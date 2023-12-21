package himyf.service;

import himyf.controller.request.CharacterRequest;
import himyf.entity.Character;
import himyf.mapper.HowIMetYourFunctionMapper;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openapi.quarkus.himyf_functions_json.api.HowIMetYourFunctionApi;


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
                .onItem().transformToUni(persistedCharacter -> callFunction(character));
    }

    private Uni<Void> callFunction(Character character) {
        String message = constructMessage(character);
        howIMetYourFunctionApi.apiPdfFunctionPost(message)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe()
                .with(response -> Uni.createFrom().voidItem(), throwable -> throwable.addSuppressed(throwable));

        return Uni.createFrom().voidItem();
    }

    private String constructMessage(Character character) {
        StringBuilder builder = new StringBuilder("Benvenuto ");
        builder.append(character.getNome());
        builder.append(" ");
        builder.append(character.getCognome());
        builder.append("la tua registrazione si è conclusa con successo");
        return builder.toString();
    }
}
