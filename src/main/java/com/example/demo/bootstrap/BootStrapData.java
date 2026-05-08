package com.example.demo.bootstrap;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository, OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository = outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<OutsourcedPart> outsourcedParts = (List<OutsourcedPart>) outsourcedPartRepository.findAll();
        for (OutsourcedPart part : outsourcedParts) {
            System.out.println(part.getName() + " " + part.getCompanyName());
        }

        //The following helped me to bulk add parts to the DB. It's single use code, but I left it uncommented to show.
        if (partRepository.count() == 0){

            InhousePart cat6Patch3ft = new InhousePart();
            cat6Patch3ft.setName("Cat6 Patch Cable 3ft (Blue)");
            cat6Patch3ft.setPrice(2.25);
            cat6Patch3ft.setInv(120);
            cat6Patch3ft.setMinInv(50);
            cat6Patch3ft.setMaxInv(200);

            InhousePart cat6Keystone = new InhousePart();
            cat6Keystone.setName("Cat6 Keystone Jack (Toolless)");
            cat6Keystone.setPrice(1.95);
            cat6Keystone.setInv(200);
            cat6Keystone.setMinInv(50);
            cat6Keystone.setMaxInv(400);

            OutsourcedPart patchPanel24 = new OutsourcedPart();
            patchPanel24.setName("24-Port Blank Patch Panel");
            patchPanel24.setPrice(18.50);
            patchPanel24.setInv(75);
            patchPanel24.setCompanyName("RackWorks");
            patchPanel24.setMinInv(10);
            patchPanel24.setMaxInv(150);

            OutsourcedPart cableMgr = new OutsourcedPart();
            cableMgr.setName("19\" Horizontal Cable Manager");
            cableMgr.setPrice(9.99);
            cableMgr.setInv(100);
            cableMgr.setCompanyName("CableFlow");
            cableMgr.setMinInv(20);
            cableMgr.setMaxInv(250);

            OutsourcedPart wallRack12u = new OutsourcedPart();
            wallRack12u.setName("12U Wall-Mount Rack");
            wallRack12u.setPrice(79.00);
            wallRack12u.setInv(30);
            wallRack12u.setCompanyName("RackForge");
            wallRack12u.setMinInv(5);
            wallRack12u.setMaxInv(60);

            partRepository.saveAll(List.of(
                    cat6Patch3ft, cat6Keystone, patchPanel24, cableMgr, wallRack12u
            ));
        }
        if (productRepository.count() == 0) {
            Product cableKit = new Product();
            cableKit.setName("Basic Cable Kit");
            cableKit.setPrice(49.99);
            cableKit.setInv(10);

            Product rackStarter = new Product();
            rackStarter.setName("Rack Starter Bundle");
            rackStarter.setPrice(149.99);
            rackStarter.setInv(5);

            Product wallRackBundle = new Product();
            wallRackBundle.setName("Wall Rack Bundle");
            wallRackBundle.setPrice(199.99);
            wallRackBundle.setInv(3);

            Product installerPack = new Product();
            installerPack.setName("Installer Essentials Pack");
            installerPack.setPrice(79.99);
            installerPack.setInv(8);

            Product dataCenterPack = new Product();
            dataCenterPack.setName("Mini Data Center Pack");
            dataCenterPack.setPrice(299.99);
            dataCenterPack.setInv(2);


            productRepository.saveAll(List.of(
                    cableKit,
                    rackStarter,
                    wallRackBundle,
                    installerPack,
                    dataCenterPack
            ));

        } else {
            System.out.println("Data already present.");
        }

        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products " + productRepository.count());
        System.out.println("Number of Parts " + partRepository.count());
    }

}
