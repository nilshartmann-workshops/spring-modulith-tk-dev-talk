package nh.demo.plantify.billing;

import nh.demo.plantify.TestIds;
import nh.demo.plantify.TestcontainersConfiguration;
import nh.demo.plantify.care.CareService;
import nh.demo.plantify.owner.OwnerRepository;
import nh.demo.plantify.plant.PlantRegisteredEvent;
import nh.demo.plantify.shared.PlantType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.*;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(TestcontainersConfiguration.class)
class BillingModuleTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private UsageRepository usageRepository;

    @Test
    void add_usage_record_with_setup_fee_for_registered_plant(Scenario scenario) {

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

        // Probleme:
        //   - UsageTracker arbeitet asynchron
        //   - TransactionalEventListener, d.h. wir können im Test nicht einfach den ApplicationEventPublisher
        //      verwenden, denn wir haben keine Transaktion
        //   - Wir müssen also auf das Ergebnis warten (Awaitility etc.)

        scenario.publish(new PlantRegisteredEvent(
                TestIds.randomPlantId,
                TestIds.existingOwnerId,
                PlantType.SUMMER_FLOWERS,
                "Schlafzimmer"))
            .andWaitForStateChange(
                () -> {
                    // wartet per Default solange bis nicht-null-Wert zurück kommt (oder nicht-empty Optional)
                    return usageRepository.findByOwnerId(TestIds.existingOwnerId);
                },
                usageRecords -> {
                    // Akzeptanz-Kriterium in diesem Fall:
                    //  Liste ist nicht nur nicht-null sondern auch befüllt
                    return !usageRecords.isEmpty();
                }
            )
            .andVerify(
                usageRecords -> assertThat(usageRecords)
                    .singleElement()
                    .satisfies(ur -> {
                        assertThat(ur.getUsageType()).isEqualTo(UsageRecord.UsageType.SETUP_FEE);
                        assertThat(ur.getCostCents()).isPositive();
                    })
            );


    }

}
