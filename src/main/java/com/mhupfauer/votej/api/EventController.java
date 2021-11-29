package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.EventEnt;
import com.mhupfauer.votej.persistence.Repo.EventEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private EventEntRepository eventEntRepository;

    @Autowired
    private Helper helper;

    @GetMapping("/")
    public List<EventEnt> getAllEvents() {
        return eventEntRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getEventById(@PathVariable(name = "id") Long id) {
        if (eventEntRepository.findById(id).isEmpty()) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
        return eventEntRepository.findById(id).get();
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> newEvent(@RequestBody Map<String, Object> fields) {
        var eventToCreate = new EventEnt();
        if (helper.setFields(fields, eventToCreate)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        eventEntRepository.save(eventToCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> patchEvent(@PathVariable(name = "id") Long id, @RequestBody Map<String, Object> fields) {
        if (eventEntRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var event = eventEntRepository.findById(id).get();
        if (helper.setFields(fields, event)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        eventEntRepository.save(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable(name = "id") Long id, @RequestBody Map<String, Object> fields) {
        if (eventEntRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventEntRepository.delete(eventEntRepository.findById(id).get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
