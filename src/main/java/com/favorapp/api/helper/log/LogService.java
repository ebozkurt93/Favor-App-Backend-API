package com.favorapp.api.helper.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<>();
        logRepository.findAll().forEach(logs::add);
        return logs;
    }

    public void addLog(Log l) {
        logRepository.save(l);
    }

    public Log getLogById(String id) {
        return logRepository.findLogById(id);
    }
}
