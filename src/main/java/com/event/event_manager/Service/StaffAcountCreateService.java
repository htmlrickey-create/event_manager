package com.event.event_manager.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.event.event_manager.Entity.AcountType;
import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Events;
import com.event.event_manager.Entity.Users;
import com.event.event_manager.Repository.BoothsRepository;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.EventRepository;
import com.event.event_manager.Repository.UsersRepository;
import java.util.ArrayList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class StaffAcountCreateService {

    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;
    private final EventMemberRepository eventMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoothsRepository boothsRepository;
    private final SecureRandom random = new SecureRandom();

    private String generatePassword(){
        String avaaliablePass = "ABCDEFGHIJKLMNOPQRSTUWXZ" + "abcdefghijklmnopqrstuwxz" + "234567890"+ "!@#$%";

        StringBuilder password = new StringBuilder();

        for (int i =0; i < 16; i++){
            int index = random.nextInt(avaaliablePass.length());
            password.append(avaaliablePass.charAt(index));
        }

        return password.toString();
    }

    private String generateStaffEmail(){
        
        String email; 

        do{
            email = "staff-"+ UUID.randomUUID()+"@event.staff-manage.AC";
        }while (usersRepository.existsByEmail(email));

        return email;
    }    

    @Transactional 
    public List<StaffCredential> createStaff(Long eventId, Long boothsId, int count, String loginEmail){
        
        if(count < 1|| count > 50){
            throw new IllegalArgumentException("Staffアカウントの生成数は1~50にしてください。");
        }

        Events event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("イベントが見つかりません。")) ;

        Booths booth = boothsRepository.findByIdAndEventId(boothsId, eventId).orElseThrow(()-> new IllegalArgumentException("指定された出店が見つかりません"));


        checkAdmin(eventId, loginEmail);

        List<StaffCredential> credentials = new ArrayList<>();

        for (int i = 0; i < count; i++){
            String rawPassword = generatePassword();

            Users user = new Users();

            user.setUsername("Staff"+ UUID.randomUUID().toString().substring(0,8));

            user.setEmail(generateStaffEmail());

            user.setPasswordHash(passwordEncoder.encode(rawPassword));

            user.setAcountType(AcountType.GENERATED_STAFF);

            usersRepository.save(user);

            EventMembers member = new EventMembers();

            member.setUser(user);
            member.setEvent(event);
            member.setRole("STAFF");
            member.setBooth(booth);

            eventMemberRepository.save(member);

            credentials.add(new StaffCredential(user.getUsername(),user.getEmail(),rawPassword));
        }

        return  credentials;
    }

    private void checkAdmin(Long eventId, String email){

        Users loginUser = usersRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("ログインユーザーが見つかりません"));

        EventMembers member = eventMemberRepository.findByUser_IdAndEvent_Id(loginUser.getId(), eventId).orElseThrow(()-> new IllegalArgumentException("このイベントのアクセス権限がありません。"));

        if(!"ADMIN".equals(member.getRole())){
            throw new IllegalArgumentException(
                "この操作はイベント管理者のみ実行できます。");
        }
    } 
}
