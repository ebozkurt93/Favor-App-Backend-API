package com.favorapp.api.helper;

import org.springframework.data.repository.CrudRepository;

public interface MessageParamsRepository extends CrudRepository<MessageParams, Long> {

    MessageParams findByKeyAndLanguageCode(String key, LanguageCode languageCode);
}
