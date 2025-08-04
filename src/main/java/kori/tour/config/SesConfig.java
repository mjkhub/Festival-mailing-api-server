package kori.tour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
public class SesConfig {

    /**
     * Creates and configures an AWS SES v2 client for the AP_NORTHEAST_2 (Seoul) region.
     *
     * @return a SesV2Client instance configured for the Seoul AWS region
     */
    @Bean
    public SesV2Client sesClient() {
        return SesV2Client.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .build();
    }
}
