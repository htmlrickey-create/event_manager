
package com.event.event_manager.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.Category;
import com.event.event_manager.Entity.Users;
import com.event.event_manager.Form.BoothsForm;
import com.event.event_manager.Repository.BoothsRepository;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.UsersRepository;
import com.event.event_manager.Service.BoothServer;
import com.event.event_manager.Service.CategoryADMINService;
import com.event.event_manager.Service.EventPermissionService;
import com.event.event_manager.Service.StaffAcountCreateService;
import com.event.event_manager.Service.StaffCredential;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AllEventManageController {

    private final BoothServer server;
    private final CategoryADMINService categoryADMINService;
    private final EventPermissionService permissionService;
    private final UsersRepository repository;
    private final StaffAcountCreateService staffAcountCreateService;
    private final BoothsRepository boothsRepository;
    private final EventMemberRepository eventMemberRepository;

   
    @GetMapping("/events/{eventId}/manage")
    public String manage(
            @PathVariable Long eventId,
            Model model, Authentication authentication) {

                if (authentication == null) {
                    return "redirect:/login";                    
                }
                String email = authentication.getName();

                if(permissionService.isStaff(email, eventId)){
                    return "redirect:/events/"+ eventId + "/staff/manage";
                }

                if (!permissionService.isAdmin(email, eventId)) {
                    return  "redirect:/notEvent";
                    
                }

            
        Users user = repository.findByEmail(authentication.getName()).orElseThrow(()-> new IllegalStateException("ユーザーが見つかりません。"));

        model.addAttribute("eventId", eventId);
        model.addAttribute("username", user.getUsername());

        model.addAttribute(
            "categories",
            categoryADMINService.getCategoriesByEventId(eventId)
        );

        List<Booths> booths = server.getBoothsByEventId(eventId);

        Map<Long, Long> staffCounts = new HashMap<>();

        for(Booths booth : booths){
            long staffCount = eventMemberRepository.countByEvent_IdAndBooth_IdAndRole(eventId, booth.getId(), "STAFF");

            staffCounts.put(booth.getId(), staffCount);
        }

        model.addAttribute("boothsForm", new BoothsForm());

        model.addAttribute("booths", server.getBoothsByEventId(eventId));

        model.addAttribute("staffCount", staffCounts);

        long boothCount = boothsRepository.countByEventId(eventId);

        model.addAttribute("countBooths",boothCount);

        return "booths/createBooths";
    }



    @PostMapping("/events/{eventId}/categories")
    public String categoryCreate(
            @PathVariable Long eventId,
            @RequestParam String categoryName,
            RedirectAttributes redirectAttributes) {

        Category category = new Category();

        category.setEventId(eventId);
        category.setCategoryName(categoryName);

        categoryADMINService.createCategory(category);

        redirectAttributes.addFlashAttribute(
            "message",
            "カテゴリーを追加しました"
        );

        return "redirect:/events/" + eventId + "/manage";
    }


   
    @PostMapping("/events/{eventId}/booths")
    public String boothCreate(
            @PathVariable Long eventId,
            @ModelAttribute BoothsForm form,
            RedirectAttributes redirectAttributes) {

        server.boothCreate(form, eventId);

        redirectAttributes.addFlashAttribute(
            "message",
            "出店の登録に成功しました"
        );

        return "redirect:/events/" + eventId + "/manage";
    }

    @PostMapping ("/events/{eventId}/staff/create")
    public String staffCreate(@PathVariable Long eventId, @RequestParam int count, @RequestParam Long boothId, Authentication authentication, RedirectAttributes redirectAttributes){
        if(authentication == null|| !permissionService.isAdmin(authentication.getName(), eventId)){
            return "redirect:/notEvent";
        }

        try{

            String loginEmail = authentication.getName();

            List<StaffCredential> credentials = staffAcountCreateService.createStaff(eventId,boothId, count, loginEmail);

            redirectAttributes.addFlashAttribute("message", "スタッフアカウントを生成しました");
            redirectAttributes.addFlashAttribute("staffCredentials", credentials);
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
        }
        return "redirect:/events/"+ eventId + "/manage";
    }

}
