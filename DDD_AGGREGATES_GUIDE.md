# DDD Aggregates Implementation Guide

## 📋 Overview

Dự án đã được refactor để áp dụng **Rich Domain Models (Aggregates)** thay vì Anemic Domain Models cho tất cả modules.

## ✅ Đã Implement

### 1. **CartAggregate** 
📁 `modules/cart/domain/model/CartAggregate.java`

**Business Logic:**
- `addItem()` - Thêm sản phẩm với validation stock & quantity
- `updateItemQuantity()` - Update số lượng với validation
- `removeItem()` - Xóa item khỏi cart
- `clearAllItems()` - Clear toàn bộ giỏ hàng
- `calculateTotal()` - Tính tổng giá trị
- `getTotalItemCount()` - Đếm tổng số items

**Ví dụ sử dụng:**
```java
CartAggregate cart = CartAggregate.create(userId);
cart.addItem(productId, name, price, color, stock, quantity, selectedColor);
cart.calculateTotal(); // Returns BigDecimal
```

### 2. **ProductAggregate**
📁 `modules/product/domain/model/ProductAggregate.java`

**Business Logic:**
- `increaseStock()` / `decreaseStock()` - Quản lý inventory
- `reserveStock()` / `releaseStock()` - Reserve cho orders
- `changePrice()` - Đổi giá với validation
- `updateInformation()` - Update thông tin product
- `hasInStock()`, `isOutOfStock()`, `isLowInStock()` - Stock queries

**Value Objects:**
- Sử dụng `Money` Value Object thay vì `BigDecimal` trần

**Ví dụ sử dụng:**
```java
ProductAggregate product = ProductAggregate.create(name, price, stock, color, category);
product.decreaseStock(5); // Validates stock availability
product.changePrice(newPrice); // Validates price > 0
```

### 3. **UserAggregate**
📁 `modules/user/domain/model/UserAggregate.java`

**Business Logic:**
- `register()` / `createAdmin()` - Factory methods
- `changePassword()` - Đổi password với validation
- `changeEmail()` - Đổi email với regex validation
- `updateProfile()` - Update user profile
- `promoteToAdmin()` / `demoteToUser()` - Role management
- `validateRawPassword()` - Static validation method

**Validations:**
- Email regex pattern
- Username rules (3-50 chars, alphanumeric + _ -)
- Password minimum 8 characters

**Ví dụ sử dụng:**
```java
UserAggregate user = UserAggregate.register(email, username, hashedPassword, fullName);
user.changeEmail(newEmail); // Validates email format
user.promoteToAdmin(); // Changes role to ADMIN
```

## 🔄 Migration Pattern

### Example Implementation
📁 `modules/cart/application/service/CartServiceUsingAggregate.java`

**Pattern:**

#### ❌ BEFORE (Anemic Model):
```java
@Service
public class CartServiceImpl {
    public void addToCart(String username, AddToCartRequest request) {
        // Business logic IN SERVICE (❌ Bad)
        validateQuantity(request.quantity());
        if (product.getStock() < quantity) {
            throw new InvalidOperationException(...);
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
    }
}
```

#### ✅ AFTER (Rich Domain Model):
```java
@Service
public class CartServiceUsingAggregate implements CartService {
    public void addToCart(String username, AddToCartRequest request) {
        // 1. Fetch dependencies (orchestration)
        User user = getUser(username);
        Product product = getProduct(request.productId());
        CartAggregate cart = getOrCreateCartAggregate(user);
        
        // 2. Business logic IN DOMAIN (✅ Good)
        cart.addItem(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getColor(),
            product.getStock(),
            request.quantity(),
            request.selectedColor()
        );
        
        // 3. Persist aggregate
        saveCartAggregate(cart);
    }
}
```

## 🎯 Key Benefits

### 1. **Encapsulation**
- Business rules protected trong domain
- Không thể invalid state từ bên ngoài
- Unmodifiable collections

### 2. **Single Source of Truth**
- Logic ở một nơi (domain), không scattered
- Dễ maintain và test

### 3. **Rich Behavior**
- Entities có methods, không chỉ data
- Self-validating objects

### 4. **Type Safety**
- Value Objects như `Money` thay vì primitive types
- Compile-time safety

## 📝 TODO: Refactoring Steps

### Phase 1: Cart Module (Example Completed ✅)
- [x] Create `CartAggregate`
- [x] Create `CartServiceUsingAggregate` (example)
- [ ] Replace `CartServiceImpl` với aggregate version
- [ ] Update tests

### Phase 2: Product Module
- [x] Create `ProductAggregate`
- [ ] Refactor `ProductServiceImpl` to use aggregate
- [ ] Create mapper helpers for reconstitution
- [ ] Update tests

### Phase 3: User Module
- [x] Create `UserAggregate`
- [ ] Refactor `AuthServiceImpl` và `UserServiceImpl`
- [ ] Move validation logic to domain
- [ ] Update tests

### Phase 4: Order Module (Already Done ✅)
- [x] `OrderAggregate` already implemented
- [x] Services already using aggregate pattern

## 🔧 Helper Patterns

### Aggregate Reconstitution
```java
private CartAggregate reconstitutedCartAggregate(Cart cart) {
    List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
    
    return CartAggregate.reconstitute(
        cart.getId(),
        cart.getUserId(),
        items,
        cart.getCreatedAt(),
        cart.getUpdatedAt(),
        cart.isDeleted(),
        cart.getDeletedAt(),
        cart.getDeletedBy()
    );
}
```

### Aggregate Persistence
```java
private void saveCartAggregate(CartAggregate aggregate) {
    // Convert to entity
    Cart cartEntity = toCartEntity(aggregate);
    cartRepository.save(cartEntity);
    
    // Save children (respecting aggregate boundary)
    for (CartItem item : aggregate.getItems()) {
        item.setCartId(aggregate.getId());
        cartItemRepository.save(item);
    }
}
```

## 🎓 DDD Principles Applied

1. **Aggregate Root** - Cart, Product, User are aggregate roots
2. **Consistency Boundary** - Aggregate ensures invariants
3. **Encapsulation** - Private fields, business logic methods
4. **Factory Methods** - `create()`, `register()`, `createAdmin()`
5. **Reconstitution** - Rebuild from persistence
6. **Value Objects** - `Money` for type safety
7. **Domain Events** (Future) - Can emit events on state changes

## 📚 References

- **Order Module**: Best example of aggregate pattern already in use
- **CartServiceUsingAggregate**: Migration example for other services
- **Aggregates**: `CartAggregate`, `ProductAggregate`, `UserAggregate`

## ⚠️ Important Notes

1. **Legacy entities still exist** - `Cart.java`, `Product.java`, `User.java` vẫn giữ cho infrastructure layer
2. **Dual implementations** - `CartServiceImpl` (old) và `CartServiceUsingAggregate` (new) để so sánh
3. **Gradual migration** - Có thể migrate từng module một
4. **Tests needed** - Phải update tests khi migrate services

---

**Status**: Aggregates created ✅ | Services partially migrated ⏳ | Tests pending ⏳
