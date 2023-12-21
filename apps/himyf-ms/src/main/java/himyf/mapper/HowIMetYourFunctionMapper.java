package himyf.mapper;

import himyf.entity.Character;
import himyf.controller.request.CharacterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HowIMetYourFunctionMapper {

    Character toEntity(CharacterRequest characterRequest);
}
