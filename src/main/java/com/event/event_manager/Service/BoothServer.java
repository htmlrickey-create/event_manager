package com.event.event_manager.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Users;
import com.event.event_manager.Form.BoothsForm;
import com.event.event_manager.Repository.BoothsRepository;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.UsersRepository;
import java.util.List;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class BoothServer {

    private final BoothsRepository repository;
    private final UsersRepository usersRepository;
    private final EventMemberRepository eventMemberRepository;

    @Transactional 
        public void  boothCreate(BoothsForm form, Long eventId){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            Users user = usersRepository.findByEmail(email).orElseThrow(()-> new IllegalStateException("ログインユーザーが見つかりません。"));

            EventMembers members = eventMemberRepository.findByUser_IdAndEvent_Id(user.getId(), eventId).orElseThrow(()-> new IllegalStateException("このイベントのメンバーではありません。"));

            if(!"ADMIN".equals(members.getRole())){
                throw new IllegalStateException("出店を作成する権限がありません");
            }

            Booths booth = new Booths();

            booth.setBoothName(form.getBoothName());
            booth.setCategoryId(form.getCategoryId());
            booth.setDescription(form.getDescription());
            booth.setLocation(form.getLocation());
            booth.setEventId(eventId);

            repository.save(booth);

        }

        public List<Booths> getBoothsByEventId(Long eventId){
            return repository.findByEventId(eventId);
        }
    
}
