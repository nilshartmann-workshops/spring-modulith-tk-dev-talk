package nh.demo.plantify.billing;

import nh.demo.plantify.TestcontainersConfiguration;
import nh.demo.plantify.care.CareService;
import nh.demo.plantify.owner.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.*;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(TestcontainersConfiguration.class)
class BillingModuleTest {

    @Autowired
    ApplicationContext context;

    @Test
    void add_usage_record_with_setup_fee_for_registered_plant() {

        // 🔎 Zeigen
        // Startet NUR Billing-Module
        //  das "used" aber owner
        // -> keine owner-Services hier
        // -> Fehler

        // 🔎 Zeigen
        // Nur Beweis, welche Module gestartet werden
        // CareService/care-Modul ist keine direkte Abhängigkeit
        // -> nicht vorhanden
        assertThat(context.getBeansOfType(CareService.class)).isEmpty();

        // OwnerRepository/owner-Modul IST direkte Abhängigkeit
        assertThat(context.getBeansOfType(OwnerRepository.class)).isNotEmpty();

    }

}
