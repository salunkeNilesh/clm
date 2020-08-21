package com.org.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.org.entity.LogsEntity;
import com.org.models.LogMessageRequestVO;
import com.org.models.LogMessageResponseVO;
import com.org.repository.LogsRepository;
import com.org.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping
public class MessageController {

    @Autowired
    LogsRepository logsRepository;

    MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping("/logMessage")
    public @ResponseBody
    ResponseEntity<LogMessageResponseVO> addLog(@Valid @RequestBody LogMessageRequestVO logs) {
        LogMessageResponseVO result = messageService.saveLogs(logs);
        return new ResponseEntity<LogMessageResponseVO>(result, HttpStatus.OK);

    }

    @GetMapping("/logMessage")
    public ResponseEntity<?> fetchLogs(@RequestParam Map<String, Object> params) {
        Collection<LogsEntity> result = (Collection<LogsEntity>) messageService.fetchLogs(params);

        if (result.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper =new ObjectMapper();
        Collection<LogMessageResponseVO> responseVO = new ArrayList<LogMessageResponseVO>();

        for (LogsEntity logEntity:result) {
            LogMessageResponseVO resVO= mapper.convertValue(logEntity,LogMessageResponseVO.class);
            responseVO.add(resVO);
        }

        return new ResponseEntity<Collection<LogMessageResponseVO>>(responseVO, HttpStatus.OK);

    }


    @GetMapping("/filterLogs")
    public ResponseEntity<?> filterLogs(@RequestParam Map<String, Object> params) {
        Collection<LogsEntity> result = (Collection<LogsEntity>) messageService.filterLogs(params);

        if (result.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper =new ObjectMapper();
        Collection<LogMessageResponseVO> responseVO = new ArrayList<LogMessageResponseVO>();

        for (LogsEntity logEntity:result) {
            LogMessageResponseVO resVO= mapper.convertValue(logEntity,LogMessageResponseVO.class);
            responseVO.add(resVO);
        }

        return new ResponseEntity<Collection<LogMessageResponseVO>>(responseVO, HttpStatus.OK);

    }
}