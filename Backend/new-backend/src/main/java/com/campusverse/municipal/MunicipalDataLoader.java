package com.campusverse.municipal;

import com.campusverse.municipal.model.MunicipalService;
import com.campusverse.municipal.repository.MunicipalServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MunicipalDataLoader implements CommandLineRunner {

    private final MunicipalServiceRepository repository;

    public MunicipalDataLoader(MunicipalServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            createService("Campus Residence Certificate", "Official proof of campus residence for administrative and consular procedures.", 2);
            createService("Student Transport & Parking Card", "Subsidized transport pass and dedicated university parking authorization.", 3);
            createService("Municipal Housing Aid & Grants", "Financial assistance and rent reduction certificates for enrolled students.", 5);
            createService("Campus Civic Engagement & Volunteer Permit", "Validation of student civic contribution and community service hours.", 4);
            createService("Municipal Sports & Cultural Facility Pass", "Access pass to town hall sports centers, pools, and public libraries.", 1);
        }
    }

    private void createService(String name, String description, int delayDays) {
        MunicipalService service = new MunicipalService();
        service.setName(name);
        service.setDescription(description);
        service.setProcessingDelayDays(delayDays);
        repository.save(service);
    }
}
