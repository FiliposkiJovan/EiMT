package finki.ukim.mk.emt.konstantinb.lab01.services.implementation;

import finki.ukim.mk.emt.konstantinb.lab01.exceptions.ManufacturerAlreadyExistsException;
import finki.ukim.mk.emt.konstantinb.lab01.exceptions.ManufacturerNotFoundException;
import finki.ukim.mk.emt.konstantinb.lab01.models.Manufacturer;
import finki.ukim.mk.emt.konstantinb.lab01.repositories.ManufacturerRepository;
import finki.ukim.mk.emt.konstantinb.lab01.repositories.persistence.PersistentManufacturerRepository;
import finki.ukim.mk.emt.konstantinb.lab01.services.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Bogdanoski (konstantin.b@live.com)
 */
@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private PersistentManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(PersistentManufacturerRepository manufacturerRepository){
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer addNewManufacturer(Manufacturer manufacturer) throws ManufacturerAlreadyExistsException{
        if (manufacturerRepository.findAll().stream().anyMatch(v -> {
            return v.equals(manufacturer);
        }))
            throw new ManufacturerAlreadyExistsException();

        manufacturerRepository.save(manufacturer);
        return manufacturer;
    }

    public Manufacturer addNewManufacturer(String manufacturerName) throws ManufacturerAlreadyExistsException{
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(manufacturerName);

        if (manufacturerRepository.findAll().stream().anyMatch(v -> {
            return v.equals(manufacturer);
        }))
            throw new ManufacturerAlreadyExistsException();

        manufacturerRepository.save(manufacturer);
        return manufacturer;
    }

    public List<Manufacturer> getAllManufacturers(){
        return manufacturerRepository.findAll();
    }

    public Manufacturer update(Manufacturer manufacturer){
        return null;
    }

    public void delete(Manufacturer manufacturer) throws ManufacturerNotFoundException{
        if (manufacturerRepository.findAll().stream().noneMatch(v -> {
            return v.equals(manufacturer);
        }))
            throw new ManufacturerNotFoundException();

        manufacturerRepository.deleteByID(manufacturer.getID());
    }

    public Manufacturer getById(Long manufacturerID) throws ManufacturerNotFoundException{
        Optional<Manufacturer> manufacturer = manufacturerRepository.findByID(manufacturerID);
        if(!manufacturer.isPresent())
            throw new ManufacturerNotFoundException();
        return manufacturer.get();
    }

    public Manufacturer getByName(String name) throws ManufacturerNotFoundException{
        Optional<Manufacturer> manufacturer = manufacturerRepository.findByName(name);
        if(!manufacturer.isPresent())
            throw new ManufacturerNotFoundException();
        return manufacturer.get();
    }
}
