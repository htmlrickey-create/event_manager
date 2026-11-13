
package com.event.event_manager.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.event.event_manager.Entity.Category;
import com.event.event_manager.Entity.Users;
import com.event.event_manager.Form.BoothsForm;
import com.event.event_manager.Repository.UsersRepository;
import com.event.event_manager.Service.BoothServer;
import com.event.event_manager.Service.CategoryADMINService;
import com.event.event_manager.Service.EventPermissionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AllEventManageController {

    private final BoothServer server;
    private final CategoryADMINService categoryADMINService;
    private final EventPermissionService permissionService;
    private final UsersRepository repository;


    // All Manager画面表示
    @GetMapping("/events/{eventId}/manage")
    public String manage(
            @PathVariable Long eventId,
            Model model, Authentication authentication) {

        if(authentication == null || !permissionService.isAdmin(authentication.getName(), eventId)){
            return "redirect:/notEvent";
        }
        Users user = repository.findByEmail(authentication.getName()).orElseThrow(()-> new IllegalStateException("ユーザーが見つかりません。"));

        model.addAttribute("eventId", eventId);
        model.addAttribute("username", user.getUsername());

        model.addAttribute(
            "categories",
            categoryADMINService.getCategoriesByEventId(eventId)
        );

        model.addAttribute("boothsForm", new BoothsForm());

        model.addAttribute("booths", server.getBoothsByEventId(eventId));

        return "booths/createBooths";
    }


    // カテゴリー登録
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


    // ブース登録
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

}
