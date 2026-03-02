# So Sánh: Anemic vs Rich Domain Model

## 📊 Kiến Trúc Project

### Hiện Tại
```
✅ Order Module    → Rich Domain Model (OrderAggregate) ✅
❌ Cart Module     → Anemic Domain Model (Cart entity) ❌
❌ Product Module  → Anemic Domain Model (Product entity) ❌
❌ User Module     → Anemic Domain Model (User entity) ❌

➡️ INCONSISTENT APPLICATION OF DDD
```

### Sau Refactoring
```
✅ Order Module    → Rich Domain Model (OrderAggregate) ✅
✅ Cart Module     → Rich Domain Model (CartAggregate) ✅
✅ Product Module  → Rich Domain Model (ProductAggregate) ✅
✅ User Module     → Rich Domain Model (UserAggregate) ✅

➡️ CONSISTENT DDD ACROSS ALL MODULES
```

---

## 🔴 ANEMIC DOMAIN MODEL (Before)

### Cart Example - BAD ❌

**Cart.java (Entity)**
```java
@Getter @Setter
public class Cart {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    // Chỉ là data container - NO BEHAVIOR!
}
```

**CartServiceImpl (Service)**
```java
public void addToCart(String username, AddToCartRequest request) {
    // ❌ BUSINESS LOGIC TRONG SERVICE (Sai!)
    
    // Validation ở service layer
    if (request.quantity() == null || request.quantity() <= 0) {
        throw new InvalidOperationException(ErrorCode.INVALID_QUANTITY);
    }
    
    // Stock validation ở service layer
    if (product.getStock() < newQuantity) {
        throw new InvalidOperationException(ErrorCode.INSUFFICIENT_STOCK);
    }
    
    // Manual manipulation
    cartItem.setQuantity(newQuantity);
    cartItem.setSelectedColor(request.selectedColor());
    cartItemRepository.save(cartItem);
}
```

**Vấn đề:**
- ❌ Business logic scattered ở service layer
- ❌ Không có encapsulation
- ❌ Có thể set invalid state với setters
- ❌ Khó test business logic riêng biệt
- ❌ Duplicate validation logic ở nhiều nơi
- ❌ Không protect invariants

---

## 🟢 RICH DOMAIN MODEL (After)

### Cart Example - GOOD ✅

**CartAggregate.java (Aggregate Root)**
```java
public class CartAggregate {
    private Long id;
    private final Long userId;
    private final List<CartItem> items = new ArrayList<>();
    
    private CartAggregate(Long userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
    
    // ✅ BUSINESS LOGIC TRONG DOMAIN (Đúng!)
    public void addItem(Long productId, String productName, BigDecimal productPrice, 
                       String productColor, Integer productStock, 
                       Integer quantity, String selectedColor) {
        validateNotDeleted();
        validateQuantity(quantity);
        validateProductStock(productStock, quantity);
        
        Optional<CartItem> existingItem = findItemByProductId(productId);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            validateProductStock(productStock, newQuantity);
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
            items.add(newItem);
        }
    }
    
    // Encapsulated validations
    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_QUANTITY,
                "Quantity must be greater than 0"
            );
        }
    }
    
    private void validateProductStock(Integer stock, Integer requested) {
        if (stock < requested) {
            throw new InvalidOperationException(
                ErrorCode.INSUFFICIENT_STOCK,
                "Insufficient stock"
            );
        }
    }
}
```

**CartServiceUsingAggregate (Service)**
```java
public void addToCart(String username, AddToCartRequest request) {
    // ✅ SERVICE CHỈ ORCHESTRATE (Đúng!)
    
    // 1. Fetch dependencies
    User user = getUser(username);
    Product product = getProduct(request.productId());
    CartAggregate cart = getOrCreateCartAggregate(user);
    
    // 2. Delegate to DOMAIN for business logic
    cart.addItem(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getColor(),
        product.getStock(),
        request.quantity(),
        request.selectedColor()
    );
    
    // 3. Persist
    saveCartAggregate(cart);
}
```

**Lợi ích:**
- ✅ Business logic centralized trong domain
- ✅ Full encapsulation (private fields, unmodifiable lists)
- ✅ Cannot create invalid state
- ✅ Easy to test domain logic isolated
- ✅ Single source of truth
- ✅ Invariants protected by aggregate

---

## 📋 Side-by-Side Comparison

| Aspect | Anemic Model ❌ | Rich Domain Model ✅ |
|--------|----------------|---------------------|
| **Business Logic** | In Service Layer | In Domain Layer |
| **Validation** | Scattered everywhere | Centralized in Aggregate |
| **Encapsulation** | Public setters | Private fields + methods |
| **Testability** | Must test through service | Can test domain directly |
| **Invariants** | Not protected | Protected by aggregate |
| **Code Location** | `CartServiceImpl` | `CartAggregate` |
| **Reusability** | Hard to reuse logic | Easy to reuse aggregate |
| **Maintainability** | Low (scattered logic) | High (single place) |

---

## 🔄 Logic Flow Comparison

### Anemic Model Flow ❌
```
Controller 
   ↓
Service (BUSINESS LOGIC HERE ❌)
   ↓
Entity (Data Only)
   ↓
Repository
   ↓
Database
```

### Rich Domain Model Flow ✅
```
Controller 
   ↓
Service (Orchestration Only)
   ↓
Aggregate (BUSINESS LOGIC HERE ✅)
   ↓
Repository
   ↓
Database
```

---

## 🎯 Concrete Examples

### Example 1: Add Item to Cart

**Anemic (Old):**
```java
// Service has all the logic
if (quantity <= 0) throw exception;
if (stock < quantity) throw exception;
cartItem.setQuantity(newQuantity);
repository.save(cartItem);
```

**Rich Domain (New):**
```java
// Aggregate has all the logic
cart.addItem(productId, name, price, color, stock, quantity, selectedColor);
// All validation inside aggregate!
```

### Example 2: Decrease Product Stock

**Anemic (Old):**
```java
// Service has validation
Product product = getProduct(id);
if (product.getStock() < quantity) {
    throw new InvalidOperationException(INSUFFICIENT_STOCK);
}
product.setStock(product.getStock() - quantity);
repository.save(product);
```

**Rich Domain (New):**
```java
// Aggregate handles everything
product.decreaseStock(quantity);
// Validation inside aggregate automatically!
```

### Example 3: Change User Email

**Anemic (Old):**
```java
// Service has validation
if (repository.existsByEmail(newEmail)) {
    throw new DuplicateResourceException(EMAIL_EXISTS);
}
user.setEmail(newEmail);
repository.save(user);
```

**Rich Domain (New):**
```java
// Aggregate validates
user.changeEmail(newEmail);
// Email format validation in aggregate!
// Uniqueness check still in service (repository access)
```

---

## 📁 Files Created

### New Aggregate Files
```
✅ modules/cart/domain/model/CartAggregate.java
✅ modules/product/domain/model/ProductAggregate.java
✅ modules/user/domain/model/UserAggregate.java
```

### Example Implementation
```
✅ modules/cart/application/service/CartServiceUsingAggregate.java
```

### Documentation
```
✅ DDD_AGGREGATES_GUIDE.md
✅ ANEMIC_VS_RICH_COMPARISON.md (this file)
```

---

## 🚀 Next Steps

1. **Review** the aggregate implementations
2. **Compare** with existing anemic entities
3. **Migrate** services one by one using CartServiceUsingAggregate as example
4. **Test** thoroughly after each migration
5. **Remove** old anemic services once aggregates are stable

---

**Remember:** Order module already uses Rich Domain Model correctly! Use it as reference.
