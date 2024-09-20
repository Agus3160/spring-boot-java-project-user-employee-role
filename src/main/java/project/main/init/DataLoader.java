package project.main.init;

import com.fiuni.distri.project.fiuni.domain.Puesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.main.repo.PuestoRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PuestoRepo puestoRepo;

    @Override
    public void run(String... args) throws Exception {

        //CREAR UN NUEVO PUESTO EN CASO DE QUE NO EXISTA
        if (puestoRepo.count() != 0) return;

        Puesto nuevoPuesto = new Puesto(
                1,
                "DEV",
                new BigDecimal(50000),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        puestoRepo.save(nuevoPuesto);
    }
}