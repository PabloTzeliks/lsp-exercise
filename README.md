# ISP Exercise - SOLID Principles in Java

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://www.java.com/)
[![SOLID](https://img.shields.io/badge/SOLID-Principles-blue)](https://en.wikipedia.org/wiki/SOLID)
[![Design Pattern](https://img.shields.io/badge/Design%20Pattern-Decorator-green)](https://refactoring.guru/design-patterns/decorator)

## 📋 Table of Contents
- [Overview](#overview)
- [Author](#author)
- [SOLID Principles](#solid-principles)
- [Interface Segregation Principle (ISP)](#interface-segregation-principle-isp)
- [Decorator Design Pattern](#decorator-design-pattern)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [References](#references)

## 🎯 Overview

This project demonstrates the practical application of **SOLID principles**, with a particular focus on the **Interface Segregation Principle (ISP)**, and the implementation of the **Decorator design pattern** in Java. The example simulates an order processing system with different shipping options.

## 👤 Author

**Pablo Tzeliks**
- GitHub: [@PabloTzeliks](https://github.com/PabloTzeliks)

### Academic Reference

This project was developed as part of a learning activity proposed by:
- **Professor:** Lucas
- **GitHub:** [@engineer-lucas](https://github.com/engineer-lucas)

## 🏗️ SOLID Principles

SOLID is an acronym representing five fundamental principles of object-oriented programming and design, aimed at creating more maintainable, understandable, and flexible software:

### 1. **S**ingle Responsibility Principle (SRP)
> A class should have only one reason to change.

**Application in this project:**
- `PedidoBase`: Responsible only for representing a base order with its product value
- `ProcessadorPagamento`: Responsible solely for processing payment calculations
- `FreteGratis` and `FretePadrao`: Each responsible for adding their specific shipping behavior

### 2. **O**pen/Closed Principle (OCP)
> Software entities should be open for extension but closed for modification.

**Application in this project:**
- The `IPedido` interface and `PedidoDecorator` abstract class allow new shipping types to be added without modifying existing code
- New decorators (like `FreteExpresso`, `FreteInternacional`) can be created by extending `PedidoDecorator` without changing the base classes

### 3. **L**iskov Substitution Principle (LSP)
> Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

**Application in this project:**
- Any implementation of `IPedido` (whether `PedidoBase`, `FreteGratis`, or `FretePadrao`) can be used interchangeably
- The `ProcessadorPagamento` works with any `IPedido` implementation without needing to know the concrete type

### 4. **I**nterface Segregation Principle (ISP)
> Clients should not be forced to depend on interfaces they do not use.

**Application in this project:**
- The `IPedido` interface is lean and focused, containing only the essential methods needed by all order types: `getValorFinal()` and `getNome()`
- No class is forced to implement methods it doesn't need
- This is detailed further in the [ISP section](#interface-segregation-principle-isp)

### 5. **D**ependency Inversion Principle (DIP)
> High-level modules should not depend on low-level modules. Both should depend on abstractions.

**Application in this project:**
- `Main` depends on the `IPedido` abstraction, not on concrete implementations
- `ProcessadorPagamento` depends on `IPedido` interface, allowing it to work with any order type
- Decorators depend on `IPedido` abstraction rather than concrete order classes

## 🎯 Interface Segregation Principle (ISP)

### What is ISP?

The **Interface Segregation Principle** states that **no client should be forced to depend on methods it does not use**. In other words, it's better to have many specific, smaller interfaces than one large, general-purpose interface.

### Why is ISP Important?

1. **Reduces coupling**: Classes only depend on the methods they actually need
2. **Improves maintainability**: Changes to one interface don't affect unrelated classes
3. **Enhances flexibility**: Easier to refactor and extend the system
4. **Prevents interface pollution**: Avoids "fat interfaces" with many unused methods

### ISP in This Project

#### ✅ Good Design (Current Implementation)

```java
public interface IPedido {
    double getValorFinal();
    String getNome();
}
```

**Why this is good:**
- The interface is **small and focused**
- Every implementation uses **all methods** defined in the interface
- `PedidoBase` needs both methods ✓
- `FreteGratis` needs both methods ✓
- `FretePadrao` needs both methods ✓
- No class is forced to implement unused methods

#### ❌ Violation Example (What NOT to do)

Imagine if we had a "fat interface" that violated ISP:

```java
// BAD EXAMPLE - Violates ISP
public interface IPedidoCompleto {
    double getValorFinal();
    String getNome();
    void aplicarDesconto(double percentual);    // Not all orders need discounts
    void adicionarSeguro(double valor);         // Not all orders need insurance
    void calcularImpostos();                     // Not all orders calculate taxes
    String gerarNotaFiscal();                    // Not all orders generate invoices
    void rastrearEntrega();                      // Not all orders need tracking
}
```

**Problems with this approach:**
- Classes like `FreteGratis` would be forced to implement methods like `aplicarDesconto()` even if they don't need it
- This creates unnecessary dependencies and code bloat
- Violates the "don't force clients to depend on methods they don't use" principle

#### ✅ Better Approach (Following ISP)

```java
// GOOD EXAMPLE - Follows ISP
public interface IPedido {
    double getValorFinal();
    String getNome();
}

public interface IDescontavel {
    void aplicarDesconto(double percentual);
}

public interface ISeguravel {
    void adicionarSeguro(double valor);
}

public interface IRastreavel {
    void rastrearEntrega();
}

// Classes implement only what they need
public class PedidoComDesconto implements IPedido, IDescontavel {
    // Only implements what it needs
}

public class PedidoRastreavel implements IPedido, IRastreavel {
    // Only implements what it needs
}
```

### Real-World Benefits in This Project

1. **Easy to extend**: Adding a new shipping type requires implementing only `getValorFinal()` and `getNome()`
2. **No dead code**: No unused methods that return null or throw `UnsupportedOperationException`
3. **Clear contracts**: Each class clearly states what it can do through its implemented interfaces
4. **Better testing**: Only test methods that are actually implemented and used

## 🎨 Decorator Design Pattern

### What is the Decorator Pattern?

The **Decorator pattern** is a structural design pattern that allows behavior to be added to individual objects, either statically or dynamically, without affecting the behavior of other objects from the same class.

### Key Characteristics

1. **Wraps an object**: Decorators wrap the original object
2. **Same interface**: Decorators implement the same interface as the objects they decorate
3. **Adds responsibility**: Decorators add new behavior before/after delegating to the wrapped object
4. **Stackable**: Multiple decorators can be stacked on top of each other

### Decorator Pattern in This Project

#### Structure

```
IPedido (Interface)
    ↑
    ├─ PedidoBase (Concrete Component)
    └─ PedidoDecorator (Abstract Decorator)
           ↑
           ├─ FreteGratis (Concrete Decorator)
           └─ FretePadrao (Concrete Decorator)
```

#### Implementation Details

**1. Component Interface (`IPedido`)**
```java
public interface IPedido {
    double getValorFinal();
    String getNome();
}
```

**2. Concrete Component (`PedidoBase`)**
```java
public class PedidoBase implements IPedido {
    private double valorProdutos;
    
    public PedidoBase(double valorProdutos) {
        this.valorProdutos = valorProdutos;
    }
    
    @Override
    public double getValorFinal() {
        return valorProdutos;  // Base value, no additions
    }
    
    @Override
    public String getNome() {
        return "Pedido Base";
    }
}
```

**3. Abstract Decorator (`PedidoDecorator`)**
```java
public abstract class PedidoDecorator implements IPedido {
    protected IPedido pedidoBase;  // The wrapped object
    
    public PedidoDecorator(IPedido pedidoBase) {
        this.pedidoBase = pedidoBase;
    }
    
    @Override
    public double getValorFinal() {
        return pedidoBase.getValorFinal();  // Delegates to wrapped object
    }
    
    @Override
    public String getNome() {
        return pedidoBase.getNome();  // Delegates to wrapped object
    }
}
```

**4. Concrete Decorators**

`FretePadrao` - Adds standard shipping cost:
```java
public class FretePadrao extends PedidoDecorator {
    private double valorFrete;
    
    public FretePadrao(IPedido pedidoBase, double valorFrete) {
        super(pedidoBase);
        if (valorFrete < 0) {
            throw new IllegalArgumentException("Valor inválido para o Frete Padrão.");
        }
        this.valorFrete = valorFrete;
    }
    
    @Override
    public double getValorFinal() {
        return pedidoBase.getValorFinal() + valorFrete;  // Adds shipping cost
    }
    
    @Override
    public String getNome() {
        return pedidoBase.getNome() + " | Frete: Padrão | Valor Frete: [ R$" + valorFrete + " ]";
    }
}
```

`FreteGratis` - Adds free shipping (no additional cost):
```java
public class FreteGratis extends PedidoDecorator {
    public FreteGratis(IPedido pedidoBase) {
        super(pedidoBase);
    }
    
    @Override
    public double getValorFinal() {
        return pedidoBase.getValorFinal();  // No additional cost
    }
    
    @Override
    public String getNome() {
        return pedidoBase.getNome() + " | Frete: Gratis";
    }
}
```

### Usage Example

```java
// Create a base order
IPedido p1 = new PedidoBase(1000);

// Wrap it with a decorator to add standard shipping
p1 = new FretePadrao(p1, 34.25);

// Output:
// Nome: "Pedido Base | Frete: Padrão | Valor Frete: [ R$34.25 ]"
// Valor: 1034.25

// Create another order with free shipping
IPedido p2 = new PedidoBase(150);
p2 = new FreteGratis(p2);

// Output:
// Nome: "Pedido Base | Frete: Gratis"
// Valor: 150.0
```

### Advantages of the Decorator Pattern

1. ✅ **Flexibility**: Add or remove responsibilities dynamically at runtime
2. ✅ **Single Responsibility**: Each decorator has a single, well-defined purpose
3. ✅ **Open/Closed Principle**: Add new decorators without modifying existing code
4. ✅ **Composability**: Stack multiple decorators (e.g., `new Desconto(new FretePadrao(new PedidoBase(100)))`)
5. ✅ **Alternative to subclassing**: Avoid class explosion from multiple inheritance

### Decorator vs Inheritance

| Aspect | Inheritance | Decorator |
|--------|------------|-----------|
| **Flexibility** | Static (compile-time) | Dynamic (runtime) |
| **Combinations** | Requires many subclasses | Compose at runtime |
| **Modification** | Affects all instances | Affects individual objects |
| **Complexity** | Can lead to class explosion | More objects, but simpler classes |

## 📁 Project Structure

```
isp-exercise/
├── src/
│   ├── Main.java                        # Entry point
│   ├── model/
│   │   ├── IPedido.java                 # Order interface (ISP compliant)
│   │   └── PedidoBase.java              # Concrete order implementation
│   └── service/
│       ├── PedidoDecorator.java         # Abstract decorator
│       ├── ProcessadorPagamento.java    # Payment processor
│       └── concrete/
│           ├── FreteGratis.java         # Free shipping decorator
│           └── FretePadrao.java         # Standard shipping decorator
├── .gitignore
├── pablo.tzeliks.iml
└── README.md
```

### File Descriptions

- **`IPedido`**: Interface defining the contract for all order types (ISP principle)
- **`PedidoBase`**: Base order implementation with product value
- **`PedidoDecorator`**: Abstract base class for all decorators
- **`FreteGratis`**: Decorator that adds free shipping information
- **`FretePadrao`**: Decorator that adds standard shipping with cost
- **`ProcessadorPagamento`**: Utility class to process payments
- **`Main`**: Demonstrates the usage of the decorator pattern

## 🚀 How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Compilation

```bash
# Navigate to the src directory
cd src

# Compile all Java files
javac Main.java model/*.java service/*.java service/concrete/*.java
```

### Execution

```bash
# Run from the src directory
java Main
```

### Expected Output

```
Pedido Base | Frete: Padrão | Valor Frete: [ R$34.25 ]
1034.25

Pedido Base | Frete: Gratis
150.0
```

## 📚 References

### Academic
- **Professor:** Lucas ([@engineer-lucas](https://github.com/engineer-lucas))

### SOLID Principles
- [SOLID Principles - Wikipedia](https://en.wikipedia.org/wiki/SOLID)
- [Uncle Bob's SOLID Principles](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
- [Interface Segregation Principle - Martin Fowler](https://martinfowler.com/bliki/InterfaceSegregationPrinciple.html)

### Design Patterns
- [Decorator Pattern - Refactoring Guru](https://refactoring.guru/design-patterns/decorator)
- [Design Patterns: Elements of Reusable Object-Oriented Software (Gang of Four)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Decorator Pattern - SourceMaking](https://sourcemaking.com/design_patterns/decorator)

### Java Documentation
- [Oracle Java Documentation](https://docs.oracle.com/en/java/)
- [Java Tutorials - Oracle](https://docs.oracle.com/javase/tutorial/)

---

## 📝 License

This project is for educational purposes as part of academic activities.

---

**© 2024 Pablo Tzeliks** | Academic Project | Professor: Lucas ([@engineer-lucas](https://github.com/engineer-lucas))
