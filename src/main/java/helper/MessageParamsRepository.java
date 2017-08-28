package helper;

import org.springframework.data.repository.CrudRepository;

public interface MessageParamsRepository extends CrudRepository<MessageParams, Long> {
    public MessageParams findMessageParamsByMessageCodeAndLanguageCode(String messageCode, LanguageCode languageCode);
}
