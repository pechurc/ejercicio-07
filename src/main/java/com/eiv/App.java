package com.eiv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eiv.entities.ProvinciaEntity;
import com.eiv.repositories.ProvinciaRepository;

@Configuration
public class App {
        
    public static final ApplicationContext CTX;
    @Autowired
    private ProvinciaRepository provinciaRepo;

    static {
        CTX = new ClassPathXmlApplicationContext("app-config.xml");
    }
    
    public static void main(String[] args) throws Exception {
        App app = CTX.getBean(App.class);
        app.run();
       
    }
    
    public void run() throws Exception {        
        ProvinciaEntity provincia = provinciaRepo.findById(1L).orElseThrow(() -> 
                new RuntimeException("Provincia no encontrada"));
        
        System.out.println(provincia);
        
        ProvinciaEntity nuevaProvincia = new ProvinciaEntity(6L, "Mendoza");        
        provinciaRepo.insert(nuevaProvincia);
        
        List<ProvinciaEntity> provincias = provinciaRepo.findAll();
        
        for (ProvinciaEntity provinciaEntity : provincias) {
            System.out.println(provinciaEntity);
        }
    }
}
