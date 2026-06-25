package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;


@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;


    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }


    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();


        for (CartItem cartItem : cartItems)
        {
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem item = new ShoppingCartItem();


            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            cart.add(item);
        }

        return cart;
    }


    public ShoppingCart addProduct(int userId, int productId)
    {
        // creates a new CartItem named existing
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        // creates a new CartItem if there isn't a CartItem named existing
        // basically, if the cart is empty it will add an item and existing just checks to see if the cart is empty
        // so the else loop can just add to the quantity
        if  (existing == null)
        {
            CartItem newItem = new CartItem();

            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            shoppingCartRepository.save(newItem);
        }

        else
        {
            existing.setQuantity(existing.getQuantity() + 1);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }


    public ShoppingCart updateProduct(int userId, int productId, int quantity)
    {
        // see above
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        // if cart has an item (existing), it keeps the quantity the same and then saves whatever changes made
        if (existing != null)
        {
            existing.setQuantity(quantity);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }


    // deletes items from the cart based on user's ID
    public ShoppingCart clearCart(int userId)
    {
        shoppingCartRepository.deleteByUserId(userId);

        return getByUserId(userId);
    }
}
