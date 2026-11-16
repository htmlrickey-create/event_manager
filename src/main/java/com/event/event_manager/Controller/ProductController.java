package com.event.event_manager.Controller;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Events;
import com.event.event_manager.Entity.Products;
import com.event.event_manager.Form.ProductForm;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class ProductController {

    private final ProductService productService;

    @GetMapping ("/booths/{boothId}/products")
    public String viewProduct(@PathVariable Long boothId, Authentication authentication, Model model){

        String email = authentication.getName();
        productService.checkBoothPermission(email, boothId);


        model.addAttribute("boothId", boothId);
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute(
            "products",
            productService.getProductsByBooth(email, boothId)
        );


        return "booths/products";
    }

    @PostMapping ("/booths/{boothId}/products/create")
    public String createProduct(@PathVariable Long boothId, @ModelAttribute ProductForm form, Authentication authentication){
        String email = authentication.getName();

        Products product = new Products();

        product.setProductName(form.getProductName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setStockQuantity(form.getStockQuantity());
        product.setIsAvailable(true);

        productService.createProduct(email, boothId, product);

        return "redirect:/booths/"+ boothId + "/products";
    }
    
}
