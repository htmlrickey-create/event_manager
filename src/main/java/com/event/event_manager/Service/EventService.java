package com.event.event_manager.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.EventRepository;
import com.event.event_manager.Repository.SchoolRepository;
import com.event.event_manager.Repository.UsersRepository;
import com.event.event_manager.Form.EventForm;

import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Events;
import com.event.event_manager.Entity.SchoolGet;
import com.event.event_manager.Entity.Users;
import org.springframework.security.core.Authentication;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.yaml.snakeyaml.events.Event;

@Service 
@RequiredArgsConstructor 
public class EventService {

    private final EventRepository repository;
    private final SchoolRepository schoolRepository;
    private final UsersRepository usersRepository;
    private final EventMemberRepository eventMemberRepository;

    @Transactional 
    public void createEvent( EventForm form){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new IllegalStateException("ログインユーザーが見つかりません。"));

        SchoolGet school = schoolRepository.findBySchoolName(form.getSchoolName())
                  .orElseGet(()-> {
                    SchoolGet newSchool = new SchoolGet();
                    newSchool.setSchoolName(form.getSchoolName());
                    return schoolRepository.save(newSchool);
                  });

        Events event = new Events();

        event.setSchool(school);
        event.setEventName(form.getEventName());
        event.setDescription(form.getDescription());
        event.setStatus(form.getStatus());
        event.setStartAt(form.getStartAt().atOffset(ZoneOffset.ofHours(9)));
        event.setEndAt(form.getEndAt().atOffset(ZoneOffset.ofHours(9)));
        
        Events savedEvent = repository.save(event);

        EventMembers members = new EventMembers();
        members.setEvent(savedEvent);
        members.setUser(user);
        members.setRole("ADMIN");

        eventMemberRepository.save(members);
    }

    @Transactional (readOnly = true)
    public List<Events> getAdminEvent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new IllegalStateException("ログインユーザーが見つかりません"));

        return eventMemberRepository.findByUser_IdAndRole(user.getId(), "ADMIN")
                .stream()
                .map(EventMembers::getEvent)
                .toList();
    }


    @Transactional 
    public void deleteEvent(Long eventId){

        Events event = repository.findById(eventId).orElseThrow(()-> new RuntimeException("イベントが見つかりません"));

        eventMemberRepository.deleteByEvent_Id(eventId);

        repository.delete(event);
    }
    
}
