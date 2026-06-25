package com.codewithnabi.ecomapplication.controller;

import com.codewithnabi.ecomapplication.dto.CartItemRequest;
import com.codewithnabi.ecomapplication.model.CartItem;
import com.codewithnabi.ecomapplication.service.CartService;
import com.codewithnabi.ecomapplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request){
        if (!cartService.addToCart(userId, request))
            return ResponseEntity.badRequest().body("Product out of stock or User/Product not found");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("item/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId){
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
