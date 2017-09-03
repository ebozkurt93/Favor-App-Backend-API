package com.favorapp.api.helper.log;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LogRepository extends CrudRepository<Log, UUID> {

    Log findLogById(String id);
}
