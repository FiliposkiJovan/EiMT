package finki.ukim.mk.emt.konstantinb.lab01.services.implementation;

import finki.ukim.mk.emt.konstantinb.lab01.exceptions.CategoryNotFoundException;
import finki.ukim.mk.emt.konstantinb.lab01.exceptions.ManufacturerNotFoundException;
import finki.ukim.mk.emt.konstantinb.lab01.exceptions.ProductAlreadyExistsException;
import finki.ukim.mk.emt.konstantinb.lab01.exceptions.ProductNotFoundException;
import finki.ukim.mk.emt.konstantinb.lab01.models.Category;
import finki.ukim.mk.emt.konstantinb.lab01.models.Manufacturer;
import finki.ukim.mk.emt.konstantinb.lab01.models.Product;
import finki.ukim.mk.emt.konstantinb.lab01.repositories.ProductRepository;
import finki.ukim.mk.emt.konstantinb.lab01.repositories.persistence.PersistentProductRepository;
import finki.ukim.mk.emt.konstantinb.lab01.services.CategoryService;
import finki.ukim.mk.emt.konstantinb.lab01.services.ManufacturerService;
import finki.ukim.mk.emt.konstantinb.lab01.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Bogdanoski (konstantin.b@live.com)
 */
@Service
public class ProductServiceImpl implements ProductService {
    private PersistentProductRepository productRepository;
    private ManufacturerService manufacturerService;
    private CategoryService categoryService;

    public ProductServiceImpl(PersistentProductRepository productRepository, ManufacturerService manufacturerService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;

        if (this.categoryService.getCategories().size() == 0) {
            this.categoryService.addNewCategory("Shoes");
            this.categoryService.addNewCategory("Jackets");
        }
        if (this.manufacturerService.getAllManufacturers().size() == 0) {
            this.manufacturerService.addNewManufacturer("Nike");
            this.manufacturerService.addNewManufacturer("Adidas");
        }
    }

    public Double getPrice(Category category){
        return productRepository.getPrice(category);
    }

    public Product addNewProduct(String name, long manufacturerID, long categoryID, String description, Double price, String linkToImg) throws ProductAlreadyExistsException, ManufacturerNotFoundException, CategoryNotFoundException{
        Optional<Manufacturer> manufacturer = manufacturerService
                .getAllManufacturers()
                .stream()
                .filter(v -> {
                    return v.getID() == manufacturerID;
                }).findAny();

        if(!manufacturer.isPresent()) throw new ManufacturerNotFoundException();

        Optional<Category> category = categoryService
                .getCategories()
                .stream()
                .filter(v -> {
                    return v.getID() == categoryID;
                }).findAny();
        if(!category.isPresent()) throw new CategoryNotFoundException();

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setManufacturer(manufacturer.get());
        newProduct.setCategory(category.get());
        newProduct.setDescription(description);
        newProduct.setLinkToImg(linkToImg);

        if (productRepository.findAll().stream().anyMatch(v -> {
            return v.equals(newProduct);
        })) {
            return update(newProduct);
        }

        productRepository.save(newProduct);
        return newProduct;
    }

    public Product addNewProduct(Product product, long manufacturerID, long categoryID) throws ProductAlreadyExistsException, ManufacturerNotFoundException, CategoryNotFoundException{
        Optional<Manufacturer> manufacturer = manufacturerService.getAllManufacturers().stream().filter(v -> { return v.getID() == manufacturerID; }).findAny();
        if(!manufacturer.isPresent()) throw new ManufacturerNotFoundException();

        Optional<Category> category = categoryService.getCategories().stream().filter(v -> { return v.getID() == categoryID; }).findAny();
        if(!category.isPresent()) throw new CategoryNotFoundException();

        product.setCategory(category.get());
        product.setManufacturer(manufacturer.get());
        product.setCategory(category.get());
        product.setManufacturer(manufacturer.get());

        productRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product update(Product product) throws ProductNotFoundException{
        Optional<Product> productOptional = productRepository.findAll().stream().filter(v -> {
            return v.equals(product);
        }).findAny();
        if(!productOptional.isPresent()) throw new ProductNotFoundException();

        Product temp = productOptional.get();
        if(temp.getManufacturer() == null)
            temp.setManufacturer(product.getManufacturer());

        if(temp.getCategory() == null)
            temp.setCategory(product.getCategory());

        temp.setDescription(product.getDescription());
        temp.setName(product.getName());
        productRepository.save(temp);

        return temp;
    }

    public void deleteProduct(Product product) throws ProductNotFoundException{
        productRepository.deleteById(product.getId());
    }

    public void deleteById(Long id) throws ProductNotFoundException{
        productRepository.deleteById(id);
    }

    public Product getById(Long productID) throws ProductNotFoundException{
        Optional<Product> product = productRepository.findById(productID);
        if(!product.isPresent())
            throw new ProductNotFoundException();
        return product.get();
    }

    public Product getByName(String name) throws ProductNotFoundException{
        Optional<Product> product = productRepository.findByName(name);
        if(!product.isPresent())
            throw new ProductNotFoundException();
        return product.get();
    }
}
