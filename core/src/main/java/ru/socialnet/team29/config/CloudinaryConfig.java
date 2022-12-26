package ru.socialnet.team29.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CloudinaryConfig {

    private String CloudName;
    private String apiKey;
    private String apiSecret;

    @Bean
    public Cloudinary initCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }
}
