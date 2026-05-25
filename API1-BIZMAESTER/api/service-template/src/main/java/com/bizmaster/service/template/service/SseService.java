package com.bizmaster.service.template.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter createEmitter(Long companyId);
    void publishEvent(Long companyId, String eventName, Object payload);
}
