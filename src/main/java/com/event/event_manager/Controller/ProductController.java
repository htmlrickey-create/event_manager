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

import com.event.event_manager.Entity.Products;
import com.event.event_manager.Form.ProductForm;
import com.event.event_manager.Service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class ProductController {

    private final ProductService productService;

    @GetMapping ("/booths/{boothId}/products")
    public String viewProduct(@PathVariable Long boothId, Authentication authentication, Model model){

        String email = authentication.getName();


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

    @PostMapping ("/booths/{boothId}/products/{productId}/delete")
    public String deleteProduct(@PathVariable Long boothId, @PathVariable Long productId, Authentication authentication,Model model){
        String email = authentication.getName();

        productService.deleteProduct(email, boothId, productId);

        return "redirect:/booths/"+ boothId + "/products";
    }

    @PostMapping ("/booths/{boothId}/products/{productId}/edit")
    public String editProduct(@PathVariable Long boothId, @PathVariable Long productId, Authentication authentication , Model model, @ModelAttribute  ProductForm form){
        String email = authentication.getName();

        Products updateProduct  = new Products();
        
        updateProduct.setProductName(form.getProductName());
        updateProduct.setDescription(form.getDescription());
        updateProduct.setPrice(form.getPrice());
        updateProduct.setStockQuantity(form.getStockQuantity());

        productService.updateProduct(email, boothId, productId, updateProduct);

        return "redirect:/booths/" + boothId + "/products";

    }

    @GetMapping("/booths/{boothId}/products/{productId}/edit")
        public String showEditProduct(@PathVariable Long boothId,@PathVariable Long productId,Authentication authentication,Model model) {

            String email = authentication.getName();

            Products product = productService.getProduct(email, boothId, productId);

            ProductForm form = new ProductForm();
            form.setProductName(product.getProductName());
            form.setDescription(product.getDescription());
            form.setPrice(product.getPrice());
            form.setStockQuantity(product.getStockQuantity());

            model.addAttribute("boothId", boothId);
            model.addAttribute("productId", productId);
            model.addAttribute("productForm", form);

            return "booths/product-edit";
        }
    
}
