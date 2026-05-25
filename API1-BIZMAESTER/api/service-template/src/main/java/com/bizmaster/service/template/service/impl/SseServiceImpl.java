package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.service.SseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseServiceImpl implements SseService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createEmitter(Long companyId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(companyId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(companyId, emitter));
        emitter.onTimeout(() -> removeEmitter(companyId, emitter));
        emitter.onError((ex) -> removeEmitter(companyId, emitter));

        try {
            emitter.send(SseEmitter.event().name("connected").data(Map.of("companyId", companyId)));
        } catch (IOException e) {
            removeEmitter(companyId, emitter);
        }
        return emitter;
    }

    @Override
    public void publishEvent(Long companyId, String eventName, Object payload) {
        List<SseEmitter> companyEmitters = emitters.get(companyId);
        if (companyEmitters == null) {
            return;
        }
        companyEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (IOException e) {
                removeEmitter(companyId, emitter);
            }
        });
    }

    private void removeEmitter(Long companyId, SseEmitter emitter) {
        List<SseEmitter> companyEmitters = emitters.get(companyId);
        if (companyEmitters != null) {
            companyEmitters.remove(emitter);
            if (companyEmitters.isEmpty()) {
                emitters.remove(companyId);
            }
        }
    }
}
