package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    private ParticipantService participantService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findMeeting(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findMeeting(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity("already exist.", HttpStatus.CONFLICT);
        }

        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findMeeting(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") Long id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findMeeting(id);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        foundMeeting.setTitle(meeting.getTitle());
        meetingService.update(foundMeeting);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findMeeting(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting.getParticipants(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(
            @PathVariable("id") Long id,
            @RequestBody Participant participantRequest) {

        Meeting meeting = meetingService.findMeeting(id);
        if (meeting == null) {
            return new ResponseEntity<>("Meeting not found", HttpStatus.NOT_FOUND);
        }

        Participant participant = participantService.findByLogin(participantRequest.getLogin());
        if (participant == null) {
            return new ResponseEntity<>("Participant not found", HttpStatus.NOT_FOUND);
        }

        meeting.addParticipant(participant);
        meetingService.update(meeting);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(
            @PathVariable("id") Long id,
            @PathVariable("login") String login) {

        Meeting meeting = meetingService.findMeeting(id);
        if (meeting == null) {
            return new ResponseEntity<>("Meeting not found", HttpStatus.NOT_FOUND);
        }

        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity<>("Participant not found", HttpStatus.NOT_FOUND);
        }

        meeting.removeParticipant(participant);
        meetingService.update(meeting);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}